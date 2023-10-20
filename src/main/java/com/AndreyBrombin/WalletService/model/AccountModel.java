package com.AndreyBrombin.WalletService.model;

import com.AndreyBrombin.WalletService.repository.WalletRepository;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Класс, представляющий модель аккаунта в системе.
 * Этот класс содержит информацию о пользователе, такую как имя, фамилия, логин и пароль.
 * Он также связан с кошельком пользователя, который используется для хранения средств.
 */
public class AccountModel{
    private static BigInteger accountCounter = BigInteger.ZERO;
    private BigInteger id;
    private String name;
    private String surname;
    private String login;
    private String password;
    private BigInteger walletId;

    /**
     * Конструктор класса AccountModel.
     *
     * @param name            Имя пользователя.
     * @param surname         Фамилия пользователя.
     * @param login           Логин пользователя.
     * @param password        Пароль пользователя.
     * @param walletId        Id кошелька пользователя.
     */
    public AccountModel(BigInteger id, String name, String surname, String login, String password, BigInteger walletId) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.password = password;
        this.walletId = walletId;
    }

    /**
     * Получить уникальный идентификатор аккаунта.
     *
     * @return Уникальный идентификатор аккаунта.
     */
    public BigInteger getId() {
        return id;
    }

    /**
     * Получить пароль пользователя.
     *
     * @return Пароль пользователя.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Получить имя пользователя.
     *
     * @return Имя пользователя.
     */
    public String getName() {
        return name;
    }

    /**
     * Получить фамилию пользователя.
     *
     * @return Фамилия пользователя.
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Получить логин пользователя.
     *
     * @return Логин пользователя.
     */
    public String getLogin() {
        return login;
    }

    /**
     * Получить уникальный идентификатор кошелька пользователя.
     *
     * @return Уникальный идентификатор кошелька пользователя.
     */
    public BigInteger getWalletId() {
        return walletId;
    }

    /**
     * Создает кошелек для аккаунта и сохраняет его в репозитории кошельков.
     *
     * @param walletRepository Репозиторий кошельков, в котором создается кошелек.
     * @return Уникальный идентификатор созданного кошелька.
     */
    public BigInteger createWallet(WalletRepository walletRepository) {
        WalletModel wallet = new WalletModel(walletRepository.generateTransactionIdFromSequence(), this.id);
        walletRepository.addWallet(wallet);
        return wallet.getId();
    }

    /**
     * Проверяет, равен ли данный аккаунт другому объекту.
     *
     * @param o Объект для сравнения.
     * @return true, если аккаунты равны, иначе false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccountModel that = (AccountModel) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (surname != null ? !surname.equals(that.surname) : that.surname != null) return false;
        if (login != null ? !login.equals(that.login) : that.login != null) return false;
        return password != null ? password.equals(that.password) : that.password == null;
    }
    /**
    * Получить идентификатор кошелька.
    *
    * @return Id кошелька.
     */
    public BigInteger getWalletOwnerId() {
        return id;
    }
    /**
     * Получить уникальный идентификатор аккаунта, который используется для операций с кошельком.
     *
     * @return Id сгенерированный для аккаунта.
     */
    public static synchronized BigInteger generateUniqueId() {
        accountCounter = accountCounter.add(BigInteger.ONE);
        return accountCounter;
    }
}
