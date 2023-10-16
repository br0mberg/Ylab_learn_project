package com.AndreyBrombin.WalletService.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Юнит-тесты для класса RegisterRepository.
 */
class RegisterRepositoryTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private WalletRepository walletRepository;

    private RegisterRepository registerRepository;

    /**
     * Настройка тестового окружения перед каждым тестом.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        registerRepository = new RegisterRepository(accountRepository, walletRepository);
    }

    /**
     * Тест успешной регистрации пользователя.
     */
    @Test
    void testSuccessfulRegistration() {
        // Arrange
        String name = "John";
        String surname = "Doe";
        String login = "testUser";
        String password = "password";

        when(accountRepository.register(name, surname, login, password, walletRepository)).thenReturn(true);

        // Act
        boolean result = registerRepository.register(name, surname, login, password, walletRepository);

        // Assert
        assertTrue(result);
        verify(accountRepository, times(1)).register(name, surname, login, password, walletRepository);
        verifyNoMoreInteractions(accountRepository);
    }

    /**
     * Тест регистрации с существующим пользователем.
     */
    @Test
    void testRegistrationWithExistingUser() {
        // Arrange
        String name = "John";
        String surname = "Doe";
        String login = "testUser";
        String password = "password";

        when(accountRepository.register(name, surname, login, password, walletRepository)).thenReturn(false);

        // Act
        boolean result = registerRepository.register(name, surname, login, password, walletRepository);

        // Assert
        assertFalse(result);
        verify(accountRepository, times(1)).register(name, surname, login, password, walletRepository);
        verifyNoMoreInteractions(accountRepository);
    }
}
