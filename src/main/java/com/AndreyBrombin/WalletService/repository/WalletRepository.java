package com.AndreyBrombin.WalletService.repository;

import com.AndreyBrombin.WalletService.Logger.CustomLogger;
import com.AndreyBrombin.WalletService.model.WalletModel;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Репозиторий для управления кошельками.
 * Этот репозиторий позволяет добавлять, получать и обновлять информацию о кошельках, а также
 * сохранять данные о кошельках в файле.
 */
public class WalletRepository {
    private List<WalletModel> wallets;
    private String filePath;

    /**
     * Создает новый экземпляр репозитория кошельков с указанным путем к файлу.
     * @param filePath Путь к файлу, в котором хранятся данные о кошельках.
     */
    public WalletRepository(String filePath) {
        this.filePath = filePath;
        wallets = new ArrayList<>();
    }

    /**
     * Добавляет новый кошелек в репозиторий и сохраняет изменения в файл.
     * @param wallet Новый кошелек для добавления.
     */
    public void addWallet(WalletModel wallet) {
        wallets.add(wallet);
        saveWalletsToFile();
    }

    /**
     * Возвращает список всех существующих кошельков, загружая их из файла при необходимости.
     * @return Список всех кошельков.
     */
    public List<WalletModel> getAllWallets() {
        loadWalletsFromFile();
        return wallets;
    }

    /**
     * Загружает список кошельков из файла.
     *
     * @throws IOException            Если произошла ошибка ввода/вывода при чтении файла.
     * @throws ClassNotFoundException   Если возникла ошибка при десериализации объектов из файла.
     */
    private void loadWalletsFromFile() {
        try {
            File file = new File(filePath);

            if (file.exists() && file.length() > 0) {
                FileInputStream fileInputStream = new FileInputStream(filePath);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                wallets = (List<WalletModel>) objectInputStream.readObject();
                objectInputStream.close();
                CustomLogger.logInfo("Wallets loaded from file.");
            } else {
                CustomLogger.logInfo("Wallet file is empty or does not exist.");
            }
        } catch (IOException | ClassNotFoundException e) {
            CustomLogger.logError( "Error loading wallets from file.", e);
        }
    }

    /**
     * Сохраняет список кошельков в файл.
     *
     * @throws IOException Если произошла ошибка ввода/вывода при записи в файл.
     */
    private void saveWalletsToFile() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(wallets);
            objectOutputStream.close();
            CustomLogger.logInfo("Wallets saved to file.");
        } catch (IOException e) {
            CustomLogger.logError( "Error saving wallets to file.", e);
        }
    }

    /**
     * Обновляет информацию о кошельке и сохраняет изменения в файле.
     *
     * @param updatedWallet Обновленный кошелек.
     * @return true, если обновление успешно выполнено, иначе false.
     */
    public boolean updateWallet(WalletModel updatedWallet) {
        loadWalletsFromFile();
        boolean walletUpdated = false;

        for (int i = 0; i < wallets.size(); i++) {
            WalletModel wallet = wallets.get(i);
            if (wallet.getOwnerId().equals(updatedWallet.getOwnerId())) {
                // Найден кошелек с тем же ID, что и у обновленного кошелька
                // Заменяем текущий кошелек на обновленный
                wallets.set(i, updatedWallet);
                // Сохраняем обновленный список кошельков в файл
                saveWalletsToFile();
                walletUpdated = true;
                break; // Выходим из цикла, так как обновление выполнено
            }
        }

        if (walletUpdated) {
            CustomLogger.logInfo("Wallet updated: " + updatedWallet.getId());
        } else {
            CustomLogger.logWarning("Wallet not found for update: " + updatedWallet.getId());
        }

        return walletUpdated;
    }

    /**
     * Возвращает кошелек по указанному идентификатору владельца (ownerId).
     *
     * @param ownerId Идентификатор владельца кошелька.
     * @return Объект кошелька, соответствующий указанному ownerId, или null, если кошелек не найден.
     * @throws IOException            Если произошла ошибка ввода/вывода при чтении файла.
     * @throws ClassNotFoundException   Если возникла ошибка при десериализации объектов из файла.
     */
    public WalletModel getWalletById(BigInteger ownerId) {
        loadWalletsFromFile(); // Загрузить актуальные кошельки из файла

        for (WalletModel wallet : wallets) {
            if (wallet.getOwnerId().equals(ownerId)) {
                return wallet; // Найден кошелек с указанным ID владельца
            }
        }

        return null; // Кошелек с указанным ID не найден
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
        return BigDecimal.ZERO; // 0 как значение по умолчанию.
    }
}