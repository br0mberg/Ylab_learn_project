package com.AndreyBrombin.WalletService.repository;

import com.AndreyBrombin.WalletService.Logger.CustomLogger;
import com.AndreyBrombin.WalletService.jdbc.ConnectionManager;
import com.AndreyBrombin.WalletService.model.TransactionModel;
import com.AndreyBrombin.WalletService.model.TransactionType;

import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import java.sql.*;

/**
 * Репозиторий, отвечающий за управление транзакциями.
 * Этот репозиторий позволяет добавлять транзакции и получать список всех существующих транзакций.
 */
public class TransactionRepository {
    private Connection connection;

    /**
     * Создает новый экземпляр репозитория транзакций.
     */
    public TransactionRepository() {
        this.connection = ConnectionManager.open();
    }

    /**
     * Добавляет новую транзакцию в репозиторий.
     *
     * @param transaction Новая транзакция для добавления.
     * @return true, если транзакция была успешно добавлена, в противном случае - false.
     */
    public boolean addTransaction(TransactionModel transaction) {
        String insertTransactionSQL = "INSERT INTO transactions_table (id, sender_account_id, receiver_account_id, amount, transaction_date, transaction_type) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertTransactionSQL)) {
            preparedStatement.setObject(1, transaction.getId());
            preparedStatement.setObject(2, transaction.getSenderAccountId());
            preparedStatement.setObject(3, transaction.getReceiverAccountId());
            preparedStatement.setBigDecimal(4, transaction.getAmount());
            preparedStatement.setDate(5, new java.sql.Date(transaction.getTransactionDate().getTime()));
            preparedStatement.setString(6, transaction.getTransactionType());
            connection.setAutoCommit(false);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                connection.commit();
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            CustomLogger.logError("Ошибка при добавлении транзакции.", e);
            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                CustomLogger.logError("Ошибка при откате транзакции.", rollbackException);
            }
        }
        return false;
    }

    /**
     * Получает последний id в базе данных.
     */
    public BigInteger loadLastIdFromDatabase() {
        String selectMaxIdSQL = "SELECT MAX(id) FROM transactions_table";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectMaxIdSQL);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                BigInteger maxId = BigInteger.valueOf(resultSet.getLong(1));
                return maxId;
            }
        } catch (SQLException e) {
            CustomLogger.logError("Ошибка при получении максимального id из базы данных.", e);
        }

        return BigInteger.ZERO;
    }

    /**
     * Возвращает список всех существующих транзакций.
     *
     * @return Список всех транзакций.
     */
    public List<TransactionModel> getAllTransactions() {
        List<TransactionModel> transactions = new ArrayList<>();
        String selectTransactionsSQL = "SELECT * FROM transactions_table";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectTransactionsSQL);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                BigInteger id = BigInteger.valueOf(resultSet.getLong("id"));
                BigInteger senderAccountId = BigInteger.valueOf(resultSet.getLong("sender_account_id"));
                BigInteger receiverAccountId = BigInteger.valueOf(resultSet.getLong("receiver_account_id"));
                BigDecimal amount = resultSet.getBigDecimal("amount");
                Date date = resultSet.getDate("transaction_date");
                TransactionType transactionType = TransactionType.fromString(resultSet.getString("transaction_type"));
                TransactionModel transaction = new TransactionModel(id, senderAccountId, receiverAccountId, amount, date, transactionType);
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            CustomLogger.logError("Ошибка при получении списка транзакций.", e);
        }

        return transactions;
    }

    /**
     * Возвращает список всех транзакций, связанных с указанным аккаунтом.
     *
     * @param accountId Идентификатор аккаунта, для которого нужно получить транзакции.
     * @return Список транзакций, связанных с указанным аккаунтом.
     */
    public List<TransactionModel> getTransactionsByAccount(BigInteger accountId) {
        List<TransactionModel> transactions = new ArrayList<>();
        String selectTransactionsSQL = "SELECT * FROM transactions_table WHERE sender_account_id = ? OR receiver_account_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectTransactionsSQL)) {
            preparedStatement.setObject(1, accountId);
            preparedStatement.setObject(2, accountId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    BigInteger id = BigInteger.valueOf(resultSet.getLong("id"));
                    BigInteger senderAccountId = BigInteger.valueOf(resultSet.getLong("sender_account_id"));
                    BigInteger receiverAccountId = BigInteger.valueOf(resultSet.getLong("receiver_account_id"));
                    BigDecimal amount = resultSet.getBigDecimal("amount");
                    Date date = resultSet.getDate("transaction_date");

                    TransactionType transactionType;
                    if (senderAccountId.equals(accountId)) {
                        transactionType = TransactionType.OUTGOING_TRANSFER;
                    } else if (receiverAccountId.equals(accountId)) {
                        transactionType = TransactionType.INCOMING_TRANSFER;
                    } else {
                        transactionType = TransactionType.UNNAMED;
                    }

                    TransactionModel transaction = new TransactionModel(id, senderAccountId, receiverAccountId, amount, date, transactionType);
                    transactions.add(transaction);
                }
            }
        } catch (SQLException e) {
            CustomLogger.logError("Ошибка при получении списка транзакций для аккаунта.", e);
        }
        return transactions;
    }

    /**
     * Проверяет, существует ли транзакция с указанным идентификатором.
     *
     * @param transactionId Идентификатор транзакции для проверки.
     * @return true, если транзакция с указанным идентификатором существует, в противном случае - false.
     */
    public boolean doesTransactionExist(BigInteger transactionId) {
        String selectTransactionSQL = "SELECT COUNT(*) FROM transactions_table WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectTransactionSQL)) {
            preparedStatement.setObject(1, transactionId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            CustomLogger.logError("Ошибка при проверке существования транзакции.", e);
        }
        return false;
    }
}
