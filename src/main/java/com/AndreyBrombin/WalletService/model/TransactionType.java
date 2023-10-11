package com.AndreyBrombin.WalletService.model;

/**
 * Перечисление, представляющее типы транзакций.
 * Включает три возможных типа транзакций: депозит, перевод и снятие средств.
 */
public enum TransactionType {
    DEPOSIT,   // Тип транзакции - депозит
    TRANSFER,  // Тип транзакции - перевод
    WITHDRAW;   // Тип транзакции - снятие средств
    @Override
    public String toString() {
        switch (this) {
            case DEPOSIT:
                return "Депозит";
            case TRANSFER:
                return "Перевод";
            case WITHDRAW:
                return "Снятие средств";
            default:
                return "Неизвестный тип";
        }
    }
}