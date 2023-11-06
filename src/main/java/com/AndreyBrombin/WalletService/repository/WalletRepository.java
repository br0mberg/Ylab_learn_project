package com.AndreyBrombin.WalletService.repository;

import com.AndreyBrombin.WalletService.infrastructure.logger.CustomLogger;
import com.AndreyBrombin.WalletService.jdbc.ConnectionManager;
import com.AndreyBrombin.WalletService.model.WalletModel;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Репозиторий для управления кошельками.
 * Этот репозиторий позволяет добавлять, получать и обновлять информацию о кошельках, а также
 * сохранять данные о кошельках в файле.
 */
public class WalletRepository {
    private Connection connection;

    /**
     * Создает новый экземпляр репозитория кошельков.
     */
    public WalletRepository() {
        this.connection = ConnectionManager.open();
    }

    /**
     * Добавляет новый кошелек в репозиторий и сохраняет изменения в файл.
     * @param wallet Новый кошелек для добавления.
     */
    public void addWallet(WalletModel wallet) {
        String insertWalletSQL = "INSERT INTO entities_schema.wallets_table (id, owner_id, wallet_name, balance) VALUES (?, ?, ?, ?)";

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertWalletSQL)) {
                preparedStatement.setObject(1, wallet.getId());
                preparedStatement.setObject(2, wallet.getOwnerId());
                preparedStatement.setObject(3, wallet.getName());
                preparedStatement.setBigDecimal(4, wallet.getBalance());

                if (preparedStatement.executeUpdate() == 1) {
                    connection.commit();
                } else {
                    connection.rollback();
                }
            }
        } catch (SQLException e) {
            CustomLogger.logError("Ошибка записи кошелька в базу данных", e);
            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                CustomLogger.logError("Ошибка при откате транзакции.", rollbackException);
            }
        }
    }
    /**
     * Возвращает список всех существующих кошельков, загружая их из файла при необходимости.
     * @return Список всех кошельков.
     */
    public List<WalletModel> getAllWallets() {
        List<WalletModel> wallets = new ArrayList<>();
        String selectWalletsSQL = "SELECT * FROM entities_schema.wallets_table";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectWalletsSQL);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                BigInteger id = (BigInteger) resultSet.getObject("id");
                BigInteger ownerId = (BigInteger) resultSet.getObject("owner_id");
                String walletName = resultSet.getString("wallet_name");
                BigDecimal balance = resultSet.getBigDecimal("balance");
                WalletModel wallet = new WalletModel(id, ownerId,walletName, balance);
                wallets.add(wallet);
            }
        } catch (SQLException e) {
            CustomLogger.logError("Ошибка получения списка кошельков из базы данных", e);
        }
        return wallets;
    }

    /**
     * Обновляет информацию о кошельке и сохраняет изменения в файле.
     *
     * @param updatedWallet Обновленный кошелек.
     * @return true, если обновление успешно выполнено, иначе false.
     */
    public boolean updateWalletBalance(WalletModel updatedWallet) {
        String updateWalletSQL = "UPDATE entities_schema.wallets_table SET balance = ? WHERE owner_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateWalletSQL)) {
            preparedStatement.setBigDecimal(1, updatedWallet.getBalance());
            preparedStatement.setObject(2, updatedWallet.getOwnerId());
            connection.setAutoCommit(false);
            int rowsUpdated = preparedStatement.executeUpdate();
             if(rowsUpdated > 0) {
                 connection.commit();
                 return true;
             }
             connection.rollback();
            return false;
        } catch (SQLException e) {
            CustomLogger.logError("Ошибка обновления баланса кошелька", e);
            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                CustomLogger.logError("Ошибка при откате транзакции.", rollbackException);
            }
            return false;
        }
    }

    /**
     * Возвращает кошелек по указанному идентификатору владельца (ownerId).
     *
     * @param ownerId Идентификатор владельца кошелька.
     * @return Объект кошелька, соответствующий указанному ownerId, или null, если кошелек не найден.
     */
    public WalletModel getWalletById(BigInteger ownerId) {
        String selectWalletSQL = "SELECT * FROM entities_schema.wallets_table WHERE owner_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectWalletSQL)) {
            preparedStatement.setObject(1, ownerId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    BigInteger id = BigInteger.valueOf(resultSet.getLong("id"));
                    String walletName = resultSet.getString("wallet_name");
                    BigDecimal balance = resultSet.getBigDecimal("balance");
                    return new WalletModel(id, ownerId,walletName, balance);
                }
            }
        } catch (SQLException e) {
            CustomLogger.logError("Ошибка получения кошелька по id владельца", e);
        }

        return null;
    }

    /**
     * Возвращает баланс кошелька по его идентификатору.
     *
     * @param walletId Идентификатор кошелька.
     * @return Баланс кошелька или BigDecimal.ZERO, если кошелек не найден.
     */
    public BigDecimal getWalletBalance(BigInteger walletId) {
        WalletModel wallet = getWalletById(walletId);
        if (wallet != null) {
            return wallet.getBalance();
        }
        return BigDecimal.ZERO;
    }

    /**
     * Генерирует walletId с помощью Sequence.
     */
    public BigInteger generateWalletIdFromSequence() {
        BigInteger walletId = null;

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT nextval('entities_schema.wallet_id_sequence')");
            if (resultSet.next()) {
                walletId = BigInteger.valueOf(resultSet.getLong(1));
            } else {
                CustomLogger.logInfo("Ошибка в работе sequence");
            }
        } catch (SQLException e) {
            CustomLogger.logError("Ошибка при генерации accountId из Sequence", e);
        }

        return walletId;
    }
}