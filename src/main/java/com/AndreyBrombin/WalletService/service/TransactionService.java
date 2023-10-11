package com.AndreyBrombin.WalletService.service;

import com.AndreyBrombin.WalletService.model.TransactionModel;
import com.AndreyBrombin.WalletService.model.TransactionType;
import com.AndreyBrombin.WalletService.model.WalletModel;
import com.AndreyBrombin.WalletService.repository.TransactionRepository;
import com.AndreyBrombin.WalletService.repository.WalletRepository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Сервис для управления транзакциями и операциями над кошельками.
 */
public class TransactionService {
    private TransactionRepository transactionRepository;
    private WalletRepository walletRepository;
    private static final Logger logger = Logger.getLogger(TransactionService.class.getName());

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
            walletRepository.updateWallet(wallet);

            TransactionModel depositTransaction = new TransactionModel(
                    wallet.getId(),
                    null,
                    amount,
                    new Date(),
                    TransactionType.DEPOSIT
            );
            BigInteger depositTransactionId = (BigInteger) depositTransaction.getId();
            if (transactionRepository.doesTransactionExist(depositTransactionId)) {
                logger.log(Level.WARNING, "Transaction with the same ID already exists: " + depositTransactionId);
                return false;
            }
            transactionRepository.addTransaction(depositTransaction);
            return true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error during deposit operation: " + e.getMessage());
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
            walletRepository.updateWallet(wallet);

            TransactionModel withdrawalTransaction = new TransactionModel(
                    wallet.getId(),
                    null,
                    amount,
                    new Date(),
                    TransactionType.WITHDRAW
            );

            BigInteger withdrawalTransactionId = (BigInteger) withdrawalTransaction.getId();
            if (transactionRepository.doesTransactionExist(withdrawalTransactionId)) {
                logger.log(Level.WARNING, "Transaction with the same ID already exists: " + withdrawalTransactionId);
                return false;
            }

            transactionRepository.addTransaction(withdrawalTransaction);
            return true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error during withdrawal operation: " + e.getMessage());
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
            return false; // Один из кошельков не найден, возвращаем false
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
            walletRepository.updateWallet(senderWallet);
            walletRepository.updateWallet(receiverWallet);

            TransactionModel senderTransaction = new TransactionModel(
                    senderWallet.getId(),
                    receiverWallet.getId(),
                    amount,
                    new Date(),
                    TransactionType.TRANSFER
            );

            TransactionModel receiverTransaction = new TransactionModel(
                    receiverWallet.getId(),
                    senderWallet.getId(),
                    amount,
                    new Date(),
                    TransactionType.TRANSFER
            );

            BigInteger senderTransactionId = (BigInteger) senderTransaction.getId();
            if (transactionRepository.doesTransactionExist(senderTransactionId)) {
                logger.log(Level.WARNING, "Transaction with the same ID already exists: " + senderTransactionId);
                return false;
            }

            BigInteger receiverTransactionId = (BigInteger) receiverTransaction.getId();
            if (transactionRepository.doesTransactionExist(receiverTransactionId)) {
                logger.log(Level.WARNING, "Transaction with the same ID already exists: " + receiverTransaction);
                return false;
            }

            transactionRepository.addTransaction(senderTransaction);
            transactionRepository.addTransaction(receiverTransaction);
            return true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error during transfer operation: " + e.getMessage());
            return false;
        }
    }

    /**
     * Получение списка всех транзакций.
     *
     * @return Список всех транзакций.
     */
    public List<TransactionModel> getAllTransactions() {
        return transactionRepository.getAllTransactions();
    }
}