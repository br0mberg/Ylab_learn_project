package com.AndreyBrombin.WalletService.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Класс, представляющий модель кошелька в системе.
 * Каждый кошелек имеет уникальный идентификатор (ID), владельца, имя кошелька, баланс и список транзакций.
 */
public class WalletModel implements Serializable {
    private BigInteger id;           // Уникальный идентификатор кошелька
    private BigInteger ownerId;       // ID владельца кошелька
    private String walletName;        // Имя кошелька
    private BigDecimal balance;       // Баланс кошелька
    private List<TransactionModel> transactions;  // Список транзакций, связанных с кошельком

    /**
     * Создает новый экземпляр кошелька для указанного владельца.
     * @param ownerId ID владельца кошелька.
     */
    public WalletModel(BigInteger ownerId) {
        UUID uuid = UUID.randomUUID();

        // Преобразование UUID в 16-ричную строку без дефисов
        String uuidString = uuid.toString().replace("-", "");

        // Создание объекта BigInteger из 16-ричной строки
        this.id = new BigInteger(uuidString, 16);

        this.ownerId = ownerId;
        this.walletName = ownerId + "Wallet's";
        this.balance = BigDecimal.valueOf(0);
        this.transactions = new ArrayList<>();
    }

    /**
     * Получить текущий баланс кошелька.
     * @return Текущий баланс кошелька.
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * Установить новый баланс кошелька.
     * @param newBalance Новый баланс для кошелька.
     */
    public void setBalance(BigDecimal newBalance) {
        this.balance = newBalance;
    }

    /**
     * Получить уникальный идентификатор кошелька.
     * @return Уникальный идентификатор кошелька.
     */
    public BigInteger getId() {
        return id;
    }

    /**
     * Получить ID владельца кошелька.
     * @return ID владельца кошелька.
     */
    public BigInteger getOwnerId() {
        return ownerId;
    }

    /**
     * Установить новый идентификатор кошелька.
     * @param id Новый уникальный идентификатор для кошелька.
     */
    public void setId(BigInteger id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WalletModel that = (WalletModel) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(ownerId, that.ownerId) &&
                Objects.equals(walletName, that.walletName) &&
                Objects.equals(balance, that.balance);
    }
}