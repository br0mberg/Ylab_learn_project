package com.AndreyBrombin.WalletService.repository;

/**
 * Репозиторий, отвечающий за регистрацию новых пользователей.
 * Этот репозиторий позволяет создавать новые аккаунты и связанные с ними кошельки.
 */
public class RegisterRepository {
    private AccountRepository accountRepository;
    private WalletRepository walletRepository;

    /**
     * Создает новый экземпляр репозитория регистрации на основе репозиториев аккаунтов и кошельков.
     *
     * @param accountRepository Репозиторий аккаунтов, в котором будут создаваться новые аккаунты.
     * @param walletRepository  Репозиторий кошельков, в котором будут создаваться новые кошельки для аккаунтов.
     */
    public RegisterRepository(AccountRepository accountRepository, WalletRepository walletRepository) {
        this.accountRepository = accountRepository;
        this.walletRepository = walletRepository;
    }

    /**
     * Регистрирует нового пользователя с указанными данными.
     *
     * @param name             Имя пользователя.
     * @param surname          Фамилия пользователя.
     * @param login            Логин пользователя.
     * @param password         Пароль пользователя.
     * @param walletRepository
     * @return true, если регистрация успешно завершена, false, если пользователь с таким логином уже существует.
     */
    public boolean register(String name, String surname, String login, String password, WalletRepository walletRepository) {
        if (accountRepository.register(name, surname, login, password, this.walletRepository)) {
            System.out.println("Регистрация успешно завершена.");
            return true;
        }
        System.out.println("Пользователь с таким логином уже существует.");
        return false;
    }
}