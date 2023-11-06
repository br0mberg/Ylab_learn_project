package com.AndreyBrombin.WalletService.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.AndreyBrombin.WalletService.repository.AccountRepository;
import com.AndreyBrombin.WalletService.repository.RegisterRepository;
import com.AndreyBrombin.WalletService.repository.WalletRepository;
import com.AndreyBrombin.WalletService.service.AuthorizationServices.RegistrationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;

/**
 * Юнит-тесты для класса RegistrationService.
 */
class RegistrationServiceTest {
    private RegistrationService registrationService;

    @Mock
    private RegisterRepository registerRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private WalletRepository walletRepository;

    /**
     * Настройка тестового окружения перед каждым тестом.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Инициализация моков
        registrationService = new RegistrationService(registerRepository);
    }

    /**
     * Тест метода registerAccount с успешной регистрацией аккаунта.
     */
    @Test
    void testRegisterValidAccount() throws SQLException {
        // Arrange
        String name = "John";
        String surname = "Doe";
        String login = "johndoe";
        String password = "password";

        when(registerRepository.register(name, surname, login, password, walletRepository)).thenReturn(true);

        // Act
        boolean result = registrationService.registerAccount(name, surname, login, password, walletRepository);

        // Assert
        assertTrue(result);
        // Убедимся, что метод register был вызван с ожидаемыми параметрами
        verify(registerRepository).register(name, surname, login, password, walletRepository);
    }

    /**
     * Тест метода registerAccount при передаче недопустимых значений.
     */
    @Test
    void testRegisterInvalidAccount() throws SQLException {
        // Arrange
        // Передача null значений, что должно вызвать проверку validateAccountData
        when(registerRepository.register(any(), any(), any(), any(), any())).thenReturn(false);

        // Act
        boolean result = registrationService.registerAccount(null, null, null, null, null);

        // Assert
        assertFalse(result);
        // Проверим, что метод register не был вызван
        verifyNoInteractions(registerRepository);
    }

    /**
     * Тест метода registerAccount с успешной регистрацией аккаунта, но недопустимым репозиторием.
     */
    @Test
    void testRegisterValidAccountInvalidRepository() throws SQLException {
        // Arrange
        String name = "John";
        String surname = "Doe";
        String login = "johndoe";
        String password = "password";

        when(registerRepository.register(name, surname, login, password, walletRepository)).thenReturn(false);

        // Act
        boolean result = registrationService.registerAccount(name, surname, login, password, walletRepository);

        // Assert
        assertFalse(result);
        // Убедимся, что метод register был вызван с ожидаемыми параметрами
        verify(registerRepository).register(name, surname, login, password, walletRepository);
    }
}
