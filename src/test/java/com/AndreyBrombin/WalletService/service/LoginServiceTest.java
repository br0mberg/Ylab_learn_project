package com.AndreyBrombin.WalletService.service;

import com.AndreyBrombin.WalletService.infrastructure.DependencyContainer;
import com.AndreyBrombin.WalletService.model.AccountModel;
import com.AndreyBrombin.WalletService.repository.AccountRepository;
import com.AndreyBrombin.WalletService.repository.LoginRepository;
import com.AndreyBrombin.WalletService.repository.WalletRepository;
import com.AndreyBrombin.WalletService.service.Authorization.LoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginServiceTest {

    private LoginService loginService;
    @Mock
    private LoginRepository loginRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private WalletRepository walletRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        loginService = new LoginService(loginRepository, accountRepository);
    }

    @Test
    void testAuthenticateValidAccount() {
        // Arrange
        when(loginRepository.login("validUser", "validPassword")).thenReturn(true);

        // Act
        boolean result = loginService.authenticateAccount("validUser", "validPassword");

        // Assert
        assertTrue(result);
    }


    @Test
    void testAuthenticateInvalidAccount() {
        // Arrange
        when(loginRepository.login("invalidUser", "invalidPassword")).thenReturn(false);

        // Act
        boolean result = loginService.authenticateAccount("invalidUser", "invalidPassword");

        // Assert
        assertFalse(result);
    }

    @Test
    void testGetAccountByValidLogin() {
        // Arrange
        AccountModel expectedAccount = new AccountModel("name", "surname", "validUser", "validPassword", walletRepository);
        when(accountRepository.getAccountByLogin("validUser")).thenReturn(expectedAccount);

        // Act
        AccountModel result = loginService.getAccountByLogin("validUser");

        // Assert
        assertEquals(expectedAccount, result);
    }


    @Test
    void testGetAccountByInvalidLogin() {
        // Arrange
        when(accountRepository.getAccountByLogin("nonExistentUser")).thenReturn(null);

        // Act
        AccountModel result = loginService.getAccountByLogin("nonExistentUser");

        // Assert
        assertNull(result);
    }
}
