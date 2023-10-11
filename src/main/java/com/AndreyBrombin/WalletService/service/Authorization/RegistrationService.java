package com.AndreyBrombin.WalletService.service.Authorization;

import com.AndreyBrombin.WalletService.repository.RegisterRepository;
import com.AndreyBrombin.WalletService.repository.WalletRepository;

/**
 * Сервис для регистрации новых учетных записей.
 */
public class RegistrationService {
    private RegisterRepository registerRepository;

    /**
     * Конструктор класса RegistrationService.
     *
     * @param registerRepository Репозиторий для регистрации пользователей.
     */
    public RegistrationService(RegisterRepository registerRepository) {
        this.registerRepository = registerRepository;
    }

    /**
     * Регистрирует новую учетную запись.
     *
     * @param name           Имя пользователя.
     * @param surname        Фамилия пользователя.
     * @param login          Логин пользователя.
     * @param password       Пароль пользователя.
     * @param walletRepository Репозиторий для управления кошельками.
     * @return true, если регистрация прошла успешно, иначе false.
     */
    public boolean registerAccount(String name, String surname, String login, String password, WalletRepository walletRepository) {
        if (validateAccountData(name, surname, login, password)) {
            return registerRepository.register(name, surname, login, password, walletRepository);
        }
        return false;
    }

    /**
     * Проверяет, что данные учетной записи не являются null.
     *
     * @param name    Имя пользователя.
     * @param surname Фамилия пользователя.
     * @param login   Логин пользователя.
     * @param password Пароль пользователя.
     * @return true, если данные учетной записи корректны, иначе false.
     */
    private boolean validateAccountData(String name, String surname, String login, String password) {
        return login != null && name != null && surname != null && password != null;
    }

    /**
     * Устанавливает репозиторий для регистрации пользователей.
     * @param registerRepository Репозиторий для регистрации пользователей.
     */
    public void setRegisterRepository(RegisterRepository registerRepository) {
        this.registerRepository = registerRepository;
    }
}