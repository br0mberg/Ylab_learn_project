package com.AndreyBrombin.WalletService.model;

import com.AndreyBrombin.WalletService.repository.TransactionRepository;
import com.AndreyBrombin.WalletService.repository.WalletRepository;

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
    private static BigInteger walletCounter = BigInteger.ONE;
    private BigInteger id;
    private BigInteger ownerId;
    private String walletName;
    private BigDecimal balance;

    /**
     * Создает новый экземпляр кошелька для указанного владельца.
     * @param ownerId ID владельца кошелька.
     */
    public WalletModel(BigInteger id, BigInteger ownerId) {
        this.id = id;
        this.ownerId = ownerId;
        this.walletName = ownerId + "Wallet's";
        this.balance = BigDecimal.valueOf(0);
    }
    /**
     * Создает новый экземпляр кошелька для указанного владельца.
     * @param ownerId ID владельца кошелька.
     */
    public WalletModel(BigInteger id, BigInteger ownerId, String walletName, BigDecimal balance) {
        this.id = id;
        this.ownerId = ownerId;
        this.walletName = walletName;
        this.balance = balance;
    }

    /**
     * Генерирует уникальный идентификатор транзакции на основе счетчика транзакций.
     * @return Уникальный идентификатор транзакции.
     */
    public static BigInteger generateWalletId() {
        synchronized (WalletRepository.class) {
            return walletCounter.add(BigInteger.ONE);
        }
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

    public String getName() {
        return walletName;
    }
}