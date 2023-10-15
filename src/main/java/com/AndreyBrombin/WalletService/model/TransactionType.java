package com.AndreyBrombin.WalletService.model;

/**
 * Перечисление, представляющее типы транзакций.
 * Включает три возможных типа транзакций: депозит, перевод и снятие средств.
 */
public enum TransactionType {
    DEPOSIT,
    TRANSFER,
    WITHDRAW;
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