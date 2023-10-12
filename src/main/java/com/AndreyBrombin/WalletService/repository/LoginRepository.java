package com.AndreyBrombin.WalletService.repository;

import com.AndreyBrombin.WalletService.Logger.CustomLogger;
import com.AndreyBrombin.WalletService.model.AccountModel;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Репозиторий, отвечающий за аутентификацию пользователей по логину и паролю.
 * Этот репозиторий позволяет проверить, существует ли аккаунт с указанным логином и правильным паролем.
 */
public class LoginRepository {
    private AccountRepository accountRepository;

    /**
     * Создает новый экземпляр репозитория аутентификации на основе репозитория аккаунтов.
     *
     * @param accountRepository Репозиторий аккаунтов, из которого будет осуществляться поиск аккаунта для аутентификации.
     */
    public LoginRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Проверяет, существует ли аккаунт с указанным логином и соответствующим паролем.
     *
     * @param login    Логин, по которому выполняется поиск аккаунта.
     * @param password Пароль, который необходимо проверить.
     * @return true, если аккаунт с указанным логином существует и пароль верен, в противном случае - false.
     */
    public boolean login(String login, String password) {
        AccountModel account = accountRepository.getAccountByLogin(login);

        if (account != null) {
            CustomLogger.logInfo( "Такой логин нашли");
            return account.getPassword().equals(password);
        }
        return false; // Пользователь с таким логином не существует
    }
}