package com.AndreyBrombin.WalletService.repository;

import com.AndreyBrombin.WalletService.model.AccountModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Юнит-тесты для класса AccountRepository.
 */
class AccountRepositoryTest {
    @Mock
    private WalletRepository walletRepository;

    private AccountRepository accountRepository;

    @TempDir
    Path tempDir; // Создаем временную директорию для тестов

    /**
     * Настройка тестового окружения перед каждым тестом.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        String tempFilePath = tempDir.resolve("accountsTest.dat").toString();
        accountRepository = new AccountRepository(tempFilePath); // Используем временный файл
    }

    /**
     * Тест для регистрации новой учетной записи.
     */
    @Test
    void testRegisterNewAccount() {
        String login = "testUser";
        String password = "password";

        boolean result = accountRepository.register("John", "Doe", login, password, walletRepository);

        assertTrue(result);
        AccountModel account = accountRepository.getAccountByLogin(login);
        assertNotNull(account);
        assertEquals("John", account.getName());
        assertEquals("Doe", account.getSurname());
        assertEquals(password, account.getPassword());
        assertNotNull(account.getWalletId());
    }

    /**
     * Тест для регистрации существующей учетной записи.
     */
    @Test
    void testRegisterExistingAccount() {
        String login = "existingUser";
        String password = "password";

        accountRepository.register("John", "Doe", login, password, walletRepository);
        boolean result = accountRepository.register("Jane", "Smith", login, "newPassword", walletRepository);

        assertFalse(result);
        AccountModel account = accountRepository.getAccountByLogin(login);
        assertNotNull(account);
        assertEquals("password", account.getPassword());
    }

    /**
     * Тест для получения учетной записи по логину.
     */
    @Test
    void testGetAccountByLogin() {
        String login = "testUser";
        String password = "password";
        accountRepository.register("John", "Doe", login, password, walletRepository);

        AccountModel account = accountRepository.getAccountByLogin(login);

        assertNotNull(account);
        assertEquals("John", account.getName());
        assertEquals("Doe", account.getSurname());
        assertEquals(password, account.getPassword());
        assertNotNull(account.getWalletId());
    }
}
