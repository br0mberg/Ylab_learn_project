package com.AndreyBrombin.WalletService.repository;

import com.AndreyBrombin.WalletService.model.AccountModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginRepositoryTest {
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private WalletRepository walletRepository;

    private LoginRepository loginRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        loginRepository = new LoginRepository(accountRepository);
    }

    @Test
    void testLoginWithExistingUserAndCorrectPassword() {
        String login = "testUser";
        String password = "password";

        // Создайте объект AccountModel с тестовыми данными
        AccountModel testAccount = new AccountModel("John", "Doe", login, password, walletRepository);

        // Укажите, что должно возвращаться, когда вызывается метод getAccountByLogin
        when(accountRepository.getAccountByLogin(any())).thenReturn(testAccount);

        boolean result = loginRepository.login(login, password);

        assertTrue(result);
    }



    @Test
    void testLoginWithExistingUserAndIncorrectPassword() {
        String login = "testUser";
        String correctPassword = "correctPassword";

        // Создайте объект AccountModel с неправильным паролем
        AccountModel accountModel = new AccountModel("John", "Doe", login, "incorrectPassword", walletRepository);

        when(accountRepository.getAccountByLogin(login)).thenReturn(accountModel);

        boolean result = loginRepository.login(login, correctPassword);

        assertFalse(result);
    }

    @Test
    void testLoginWithNonExistingUser() {
        String login = "nonExistingUser";
        when(accountRepository.getAccountByLogin(login)).thenReturn(null);

        boolean result = loginRepository.login(login, "password");

        assertFalse(result);
    }
}
