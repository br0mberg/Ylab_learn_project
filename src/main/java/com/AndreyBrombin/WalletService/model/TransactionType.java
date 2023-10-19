package com.AndreyBrombin.WalletService.model;

import com.AndreyBrombin.WalletService.Logger.CustomLogger;

/**
 * Перечисление, представляющее типы транзакций.
 * Включает три возможных типа транзакций: депозит, перевод и снятие средств.
 */
public enum TransactionType {
    DEPOSIT,
    TRANSFER,
    INCOMING_TRANSFER,
    OUTGOING_TRANSFER,
    WITHDRAW,

    UNNAMED;

    /**
     * Переопределённый метод для трансформации типа транзакции в строку.
     */
    @Override
    public String toString() {
        switch (this) {
            case DEPOSIT:
                return "Депозит";
            case TRANSFER:
                return "Перевод";
            case INCOMING_TRANSFER:
                return "Входящий перевод";
            case OUTGOING_TRANSFER:
                return "Исходящий перевод";
            case WITHDRAW:
                return "Снятие средств";
            default:
                return "Неизвестный тип";
        }
    }
    /**
     * Метод для трансформации строки в тип транзакции.
     */
    public static TransactionType fromString(String type) {
        switch (type) {
            case "Депозит":
                return DEPOSIT;
            case "Перевод":
                return TRANSFER;
            case "Входящий перевод":
                return INCOMING_TRANSFER;
            case "Исходящий перевод":
                return OUTGOING_TRANSFER;
            case "Снятие средств":
                return WITHDRAW;
            default:
                CustomLogger.logError("Неизвестный тип транзакции: ", new IllegalArgumentException(type));
                return UNNAMED;
        }
    }
}