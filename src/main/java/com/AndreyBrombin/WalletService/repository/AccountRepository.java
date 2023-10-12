package com.AndreyBrombin.WalletService.repository;

import com.AndreyBrombin.WalletService.Logger.CustomLogger;
import com.AndreyBrombin.WalletService.model.AccountModel;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Репозиторий, ответственный за хранение и управление аккаунтами пользователей.
 * Этот репозиторий предоставляет функциональность для регистрации новых аккаунтов, загрузки существующих аккаунтов из файла,
 * сохранения аккаунтов в файл, а также поиск аккаунта по логину.
 */
public class AccountRepository {
    private Map<String, AccountModel> accountCredentials; // Хранилище аккаунтов, где ключ - логин пользователя.
    private String filePath;

    /**
     * Создает новый экземпляр репозитория аккаунтов с указанным путем к файлу.
     *
     * @param filePath Путь к файлу, в котором будут храниться данные аккаунтов.
     */
    public AccountRepository(String filePath) {
        this.filePath = filePath;
        accountCredentials = new HashMap<>();
    }

    /**
     * Регистрирует новый аккаунт пользователя с заданными данными.
     * Если аккаунт с указанным логином уже существует, регистрация не выполняется.
     *
     * @param name           Имя пользователя.
     * @param surname        Фамилия пользователя.
     * @param login          Логин пользователя.
     * @param password       Пароль пользователя.
     * @param walletRepository Репозиторий кошельков для создания кошелька для нового аккаунта.
     * @return true, если регистрация прошла успешно, в противном случае - false.
     */
    public boolean register(String name, String surname, String login, String password, WalletRepository walletRepository) {
        try {
            loadUsersFromFile();

            if (!accountCredentials.containsKey(login)) {
                accountCredentials.put(login, new AccountModel(name, surname, login, password, walletRepository));
                saveAccountsToFile();
                return true;
            }
        } catch (IOException e) {
            CustomLogger.logError( "Ошибка при регистрации аккаунта.", e);
        }
        return false;
    }

    /**
     * Загружает данные аккаунтов из файла, если файл существует и не пустой.
     *
     * @return Загруженное множество аккаунтов или новое пустое множество, если файл не существует или пустой.
     */
    private Map<String, AccountModel> loadUsersFromFile() throws IOException {
        try {
            File file = new File(filePath);

            if (file.exists() && file.length() > 0) {
                FileInputStream fileInputStream = new FileInputStream(filePath);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                accountCredentials = (Map<String, AccountModel>) objectInputStream.readObject();
                objectInputStream.close();
                CustomLogger.logInfo("Файл не пуст, объекты выгружены");
            } else {
                CustomLogger.logInfo( "Файл с аккаунтами пустой или не существует.");
            }
        } catch (ClassNotFoundException e) {
            CustomLogger.logError( "Ошибка при загрузке аккаунтов из файла.", e);
        }
        return accountCredentials;
    }

    /**
     * Сохраняет данные аккаунтов в файл.
     */
    private void saveAccountsToFile() throws IOException {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(accountCredentials);
            objectOutputStream.close();
        } catch (IOException e) {
            CustomLogger.logError( "Ошибка при сохранении аккаунтов в файл.", e);
        }
    }

    /**
     * Получает аккаунт по логину.
     *
     * @param login Логин аккаунта, который нужно найти.
     * @return Объект аккаунта, соответствующий указанному логину, или null, если аккаунт не найден.
     */
    public AccountModel getAccountByLogin(String login) {
        try {
            loadUsersFromFile();

            if (accountCredentials.containsKey(login)) {
                return accountCredentials.get(login);
            }
        } catch (IOException e) {
            CustomLogger.logError("Ошибка при получении аккаунта по логину.", e);
        }
        return null; // Пользователь с таким логином не найден
    }
}