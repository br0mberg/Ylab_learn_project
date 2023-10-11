package com.AndreyBrombin.WalletService.service.Authorization;

import com.AndreyBrombin.WalletService.model.AccountModel;
import com.AndreyBrombin.WalletService.repository.AccountRepository;
import com.AndreyBrombin.WalletService.repository.LoginRepository;

/**
 * Сервис аутентификации пользователей и управления аккаунтами.
 */
public class LoginService {
    private LoginRepository loginRepository;
    private AccountRepository accountRepository;

    /**
     * Создает новый экземпляр сервиса аутентификации.
     *
     * @param loginRepository    Репозиторий для выполнения входа в систему.
     * @param accountRepository  Репозиторий аккаунтов для доступа к данным аккаунтов.
     */
    public LoginService(LoginRepository loginRepository, AccountRepository accountRepository) {
        this.loginRepository = loginRepository;
        this.accountRepository = accountRepository;
    }

    /**
     * Проверяет аутентификацию аккаунта с заданным логином и паролем.
     *
     * @param login    Логин аккаунта.
     * @param password Пароль аккаунта.
     * @return true, если аккаунт аутентифицирован успешно, в противном случае false.
     */
    public boolean authenticateAccount(String login, String password) {
        return loginRepository.login(login, password);
    }

    /**
     * Возвращает аккаунт с заданным логином.
     *
     * @param login Логин аккаунта.
     * @return Объект аккаунта, соответствующий заданному логину, или null, если аккаунт не найден.
     */
    public AccountModel getAccountByLogin(String login) {
        if (login == null || login.length() == 0) {
            return null;
        }
        return accountRepository.getAccountByLogin(login);
    }

    /**
     * Устанавливает репозиторий для выполнения входа в систему.
     *
     * @param loginRepository Репозиторий для выполнения входа в систему.
     */
    public void setLoginRepository(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    /**
     * Устанавливает репозиторий для выполнения входа в систему.
     *
     * @param accountRepository Репозиторий для работы с аккаунтами.
     */
    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
}
