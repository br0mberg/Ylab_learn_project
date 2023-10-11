package com.AndreyBrombin.WalletService.model;

/**
 * Перечисление, представляющее типы транзакций.
 * Включает три возможных типа транзакций: депозит, перевод и снятие средств.
 */
public enum TransactionType {
    DEPOSIT,   // Тип транзакции - депозит
    TRANSFER,  // Тип транзакции - перевод
    WITHDRAW   // Тип транзакции - снятие средств
}