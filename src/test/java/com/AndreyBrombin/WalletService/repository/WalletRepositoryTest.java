package com.AndreyBrombin.WalletService.repository;

import com.AndreyBrombin.WalletService.model.WalletModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WalletRepositoryTest {

    private WalletRepository walletRepository;

    @TempDir
    File tempDir;

    @BeforeEach
    void setUp() {
        // Создаем временный файл внутри временной директории
        File tempFile = new File(tempDir, "test_wallets.dat");
        walletRepository = new WalletRepository(tempFile.getAbsolutePath());
    }

    @Test
    void testAddWallet() {
        // Arrange
        WalletModel wallet = new WalletModel(BigInteger.valueOf(1));
        wallet.setBalance(BigDecimal.valueOf(100.0));

        // Act
        walletRepository.addWallet(wallet);

        // Assert
        List<WalletModel> wallets = walletRepository.getAllWallets();
        assertTrue(wallets.contains(wallet));
    }

    @Test
    void testUpdateWallet() {
        // Создаем экземпляр WalletModel
        WalletModel wallet = new WalletModel(BigInteger.valueOf(1));

        // Добавляем кошелек в репозиторий
        walletRepository.addWallet(wallet);

        // Создаем обновленный кошелек с тем же ID
        WalletModel updatedWallet = new WalletModel(wallet.getOwnerId());
        updatedWallet.setId(wallet.getId());

        // Попробуем обновить кошелек
        boolean updateResult = walletRepository.updateWallet(updatedWallet);

        // Выводим информацию о результате
        if (updateResult) {
            System.out.println("Wallet updated successfully: " + updatedWallet.getId());
        } else {
            System.err.println("Wallet update failed: " + updatedWallet.getId());
        }

        // Проверяем, что обновление прошло успешно
        assertTrue(updateResult);
    }

    @Test
    void testGetWalletById() {
        // Создаем экземпляр WalletModel
        WalletModel wallet = new WalletModel(BigInteger.valueOf(1));

        // Добавляем кошелек в репозиторий
        walletRepository.addWallet(wallet);

        // Получаем кошелек по его ownerID
        WalletModel retrievedWallet = walletRepository.getWalletById(wallet.getOwnerId());

        // Проверяем, что retrievedWallet не равен null
        assertNotNull(retrievedWallet);

        // Проверяем, что ID полученного кошелька совпадает с ожидаемым
        assertEquals(wallet.getId(), retrievedWallet.getId());
    }

    @Test
    void testGetWalletBalance() {
        // Arrange
        WalletModel wallet = new WalletModel(BigInteger.valueOf(1));
        wallet.setBalance(BigDecimal.valueOf(100.0));
        walletRepository.addWallet(wallet);

        // Выводим содержимое файла с кошельками
        System.out.println("Wallets in the file:");
        List<WalletModel> walletsInFile = walletRepository.getAllWallets();
        for (WalletModel walletInFile : walletsInFile) {
            System.out.println("Wallet ID: " + walletInFile.getId() + ", Balance: " + walletInFile.getBalance());
        }

        // Act
        BigDecimal balance = walletRepository.getWalletBalance(BigInteger.valueOf(1));

        // Assert
        assertNotNull(balance);
        assertEquals(BigDecimal.valueOf(100.0), balance);
    }

}
