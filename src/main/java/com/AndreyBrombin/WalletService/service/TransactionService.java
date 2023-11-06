package com.AndreyBrombin.WalletService.service;

import com.AndreyBrombin.WalletService.infrastructure.logger.CustomLogger;
import com.AndreyBrombin.WalletService.model.TransactionModel;
import com.AndreyBrombin.WalletService.model.TransactionType;
import com.AndreyBrombin.WalletService.model.WalletModel;
import com.AndreyBrombin.WalletService.repository.TransactionRepository;
import com.AndreyBrombin.WalletService.repository.WalletRepository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * Сервис для управления транзакциями и операциями над кошельками.
 */
public class TransactionService {
    private TransactionRepository transactionRepository;
    private WalletRepository walletRepository;

    /**
     * Конструктор класса TransactionService.
     *
     * @param transactionRepository Репозиторий транзакций.
     * @param walletRepository      Репозиторий кошельков.
     */
    public TransactionService(TransactionRepository transactionRepository, WalletRepository walletRepository) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
    }

    /**
     * Выполнение операции пополнения средств на кошелек.
     *
     * @param walletOwnerId Идентификатор владельца кошелька.
     * @param amount       Сумма для пополнения.
     * @return true, если операция пополнения прошла успешно, в противном случае - false.
     */
    public boolean deposit(BigInteger walletOwnerId, BigDecimal amount) {
        WalletModel wallet = walletRepository.getWalletById(walletOwnerId);

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }

        BigDecimal newBalance = wallet.getBalance().add(amount);
        wallet.setBalance(newBalance);

        try {
            // Обновляем информацию о кошельке в репозитории
            walletRepository.updateWalletBalance(wallet);

            TransactionModel depositTransaction = new TransactionModel(
                    transactionRepository.generateTransactionIdFromSequence(),
                    wallet.getId(),
                    wallet.getId(),
                    amount,
                    new Date(),
                    TransactionType.DEPOSIT
            );
            BigInteger depositTransactionId = (BigInteger) depositTransaction.getId();
            if (transactionRepository.doesTransactionExist(depositTransactionId)) {
                CustomLogger.logWarning("Transaction with the same ID already exists: " + depositTransactionId);
                return false;
            }
            transactionRepository.addTransaction(depositTransaction);
            return true;
        } catch (Exception e) {
            CustomLogger.logError("Error during deposit operation: ", e);
            return false;
        }
    }

    /**
     * Выполнение операции снятия средств с кошелька.
     *
     * @param walletId Идентификатор кошелька.
     * @param amount   Сумма для снятия.
     * @return true, если операция снятия средств прошла успешно, в противном случае - false.
     */
    public boolean withdraw(BigInteger walletId, BigDecimal amount) {
        WalletModel wallet = walletRepository.getWalletById(walletId);

        if (amount.compareTo(BigDecimal.ZERO) <= 0 || amount.compareTo(wallet.getBalance()) > 0) {
            return false;
        }

        BigDecimal newBalance = wallet.getBalance().subtract(amount);
        wallet.setBalance(newBalance);

        try {
            // Обновляем информацию о кошельке в репозитории
            walletRepository.updateWalletBalance(wallet);

            TransactionModel withdrawalTransaction = new TransactionModel(
                    transactionRepository.generateTransactionIdFromSequence(),
                    wallet.getId(),
                    wallet.getId(),
                    amount,
                    new Date(),
                    TransactionType.WITHDRAW
            );

            BigInteger withdrawalTransactionId = (BigInteger) withdrawalTransaction.getId();
            if (transactionRepository.doesTransactionExist(withdrawalTransactionId)) {
                CustomLogger.logWarning("Transaction with the same ID already exists: " + withdrawalTransactionId);
                return false;
            }

            transactionRepository.addTransaction(withdrawalTransaction);
            return true;
        } catch (Exception e) {
            CustomLogger.logError("Error during withdrawal operation: ", e);
            return false;
        }
    }

    /**
     * Выполнение операции перевода средств между кошельками.
     *
     * @param senderWalletId   Идентификатор отправителя.
     * @param receiverWalletId Идентификатор получателя.
     * @param amount           Сумма для перевода.
     * @return true, если операция перевода прошла успешно, в противном случае - false.
     */
    public boolean transfer(BigInteger senderWalletId, BigInteger receiverWalletId, BigDecimal amount) {
        WalletModel senderWallet = walletRepository.getWalletById(senderWalletId);
        WalletModel receiverWallet = walletRepository.getWalletById(receiverWalletId);

        if (senderWallet == null || receiverWallet == null) {
            return false;
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0 || amount.compareTo(senderWallet.getBalance()) > 0) {
            return false;
        }

        BigDecimal senderNewBalance = senderWallet.getBalance().subtract(amount);
        BigDecimal receiverNewBalance = receiverWallet.getBalance().add(amount);

        senderWallet.setBalance(senderNewBalance);
        receiverWallet.setBalance(receiverNewBalance);

        try {
            // Обновляем информацию о кошельках в репозитории
            walletRepository.updateWalletBalance(senderWallet);
            walletRepository.updateWalletBalance(receiverWallet);
            TransactionModel senderTransaction = new TransactionModel(
                    transactionRepository.generateTransactionIdFromSequence(),
                    senderWallet.getId(),
                    receiverWallet.getId(),
                    amount,
                    new Date(),
                    TransactionType.TRANSFER
            );


            BigInteger senderTransactionId = (BigInteger) senderTransaction.getId();
            if (transactionRepository.doesTransactionExist(senderTransactionId)) {
                CustomLogger.logWarning("Transaction with the same ID already exists: " + senderTransactionId);
                return false;
            }

            transactionRepository.addTransaction(senderTransaction);
            return true;
        } catch (Exception e) {
            CustomLogger.logError("Error during transfer operation: ", e);
            return false;
        }
    }
}