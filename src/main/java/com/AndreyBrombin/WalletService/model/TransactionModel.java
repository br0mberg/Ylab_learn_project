package com.AndreyBrombin.WalletService.model;

import com.AndreyBrombin.WalletService.repository.TransactionRepository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Objects;

/**
 * Класс, представляющий модель транзакции.
 * Этот класс содержит информацию о переводе средств между аккаунтами, включая идентификатор отправителя,
 * идентификатор получателя, сумму, дату транзакции и тип транзакции (например, депозит или снятие средств).
 */
public class TransactionModel {
    private static BigInteger transactionCounter = BigInteger.ZERO;
    private BigInteger id;

    private BigInteger senderAccountId;
    private BigInteger receiverAccountId;

    private BigDecimal amount;

    private Date transactionDate;
    private TransactionType transactionType;

    /**
     * Конструктор класса TransactionModel.
     *
     * @param id                Идентификатор транзакции.
     * @param senderAccountId   Идентификатор аккаунта отправителя.
     * @param receiverAccountId Идентификатор аккаунта получателя.
     * @param amount           Сумма транзакции.
     * @param transactionDate   Дата транзакции.
     * @param transactionType   Тип транзакции (например, депозит или снятие средств).
     */
    public TransactionModel(BigInteger id, BigInteger senderAccountId, BigInteger receiverAccountId, BigDecimal amount, Date transactionDate, TransactionType transactionType) {
        this.id = id;
        this.senderAccountId = senderAccountId;
        this.receiverAccountId = receiverAccountId;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.transactionType = transactionType;
    }

    /**
     * Генерирует уникальный идентификатор транзакции на основе счетчика транзакций.
     * @return Уникальный идентификатор транзакции.
     */
    public static BigInteger generateTransactionId() {
        synchronized (TransactionRepository.class) {
            transactionCounter = transactionCounter.add(BigInteger.ONE);
            return transactionCounter;
        }
    }

    /**
     * Проверяет, равна ли данная транзакция другой транзакции.
     * @param o Объект для сравнения.
     * @return true, если транзакции равны, иначе false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionModel that = (TransactionModel) o;
        return Objects.equals(senderAccountId, that.senderAccountId) &&
                Objects.equals(receiverAccountId, that.receiverAccountId) &&
                Objects.equals(amount, that.amount) &&
                Objects.equals(transactionDate, that.transactionDate) &&
                transactionType == that.transactionType;
    }

    /**
     * Получает идентификатор (ID) данной транзакции.
     *
     * @return Идентификатор (ID) транзакции.
     */
    public Object getId() {
        return id;
    }

    /**
     * Получает тип данной транзакции в виде строки.
     *
     * @return Тип транзакции в виде строки.
     */
    public String getTransactionType() {
        return transactionType.toString();
    }

    /**
     * Получает сумму транзакции в виде строки.
     *
     * @return Сумма транзакции в виде строки.
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Получает дату транзакции в виде строки.
     *
     * @return Дата транзакции в виде строки.
     */
    public Date getTransactionDate() {
        return transactionDate;
    }

    public BigInteger getReceiverAccountId() {
        return receiverAccountId;
    }

    public BigInteger getSenderAccountId() {
        return senderAccountId;
    }
}