package com.AndreyBrombin.WalletService.repository;

import com.AndreyBrombin.WalletService.Logger.CustomLogger;
import com.AndreyBrombin.WalletService.jdbc.ConnectionManager;
import com.AndreyBrombin.WalletService.model.AccountModel;
import com.AndreyBrombin.WalletService.model.WalletModel;

import java.math.BigInteger;
import java.sql.*;

/**
 * Репозиторий, ответственный за хранение и управление аккаунтами пользователей.
 * Этот репозиторий предоставляет функциональность для регистрации новых аккаунтов, загрузки существующих аккаунтов из файла,
 * сохранения аккаунтов в файл, а также поиск аккаунта по логину.
 */
public class AccountRepository {
    private Connection connection;
    private BigInteger currentId;

    /**
     * Создает новый экземпляр репозитория аккаунтов.
     */
    public AccountRepository() {
        this.connection = ConnectionManager.open();
        this.currentId = loadLastIdFromDatabase().add(BigInteger.ONE);
    }
    /**
     * Загружает последний Id из базы данных.
     */
    private BigInteger loadLastIdFromDatabase() {
        String selectMaxIdSQL = "SELECT MAX(id) FROM accounts_table";

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
     * Регистрирует новый аккаунт пользователя с заданными данными.
     * Если аккаунт с указанным логином уже существует, регистрация не выполняется.
     *
     * @param name             Имя пользователя.
     * @param surname          Фамилия пользователя.
     * @param login            Логин пользователя.
     * @param password         Пароль пользователя.
     * @param walletRepository Репозиторий кошельков для создания кошелька для нового аккаунта.
     * @return true, если регистрация прошла успешно, в противном случае - false.
     */
    public boolean register(String name, String surname, String login, String password, WalletRepository walletRepository) {
        BigInteger walletId = this.currentId;
        BigInteger accountId = this.currentId;
        this.currentId = this.currentId.add(BigInteger.ONE);
        String insertAccountSQL = "INSERT INTO accounts_table " +
                "(id, name, surname, login, password, wallet_id) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertAccountSQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setObject(1, accountId);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, surname);
            preparedStatement.setString(4, login);
            preparedStatement.setString(5, password);
            preparedStatement.setObject(6, walletId);
            connection.setAutoCommit(false);
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                WalletModel wallet = new WalletModel(walletRepository.loadLastIdFromDatabase().add(BigInteger.ONE), accountId);
                walletRepository.addWallet(wallet);
                connection.commit();
                return true;
            }
        } catch (SQLException e) {
            CustomLogger.logError("Ошибка при регистрации аккаунта.", e);
            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                CustomLogger.logError("Ошибка при откате транзакции.", rollbackException);
            }
        }
        return false;
    }

    /**
     * Получает аккаунт по логину.
     *
     * @param login Логин аккаунта, который нужно найти.
     * @return Объект аккаунта, соответствующий указанному логину, или null, если аккаунт не найден.
     */
    public AccountModel getAccountByLogin(String login) {
        String selectAccountSQL = "SELECT * FROM accounts_table WHERE login = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectAccountSQL)) {
            preparedStatement.setString(1, login);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    BigInteger id = BigInteger.valueOf(resultSet.getLong("id"));
                    String name = resultSet.getString("name");
                    String surname = resultSet.getString("surname");
                    String retrievedLogin = resultSet.getString("login");
                    String password = resultSet.getString("password");
                    BigInteger walletId = BigInteger.valueOf(resultSet.getLong("wallet_id"));
                    AccountModel account = new AccountModel(id, name, surname, retrievedLogin, password, walletId);
                    return account;
                }
            }
        } catch (SQLException e) {
            CustomLogger.logError("Ошибка при получении аккаунта по логину.", e);
        }
        return null;
    }
}