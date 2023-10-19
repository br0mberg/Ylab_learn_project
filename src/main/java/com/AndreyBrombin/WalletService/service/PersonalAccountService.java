package com.AndreyBrombin.WalletService.service;

import com.AndreyBrombin.WalletService.controller.in.InputHandler;
import com.AndreyBrombin.WalletService.controller.out.OutputHandler;
import com.AndreyBrombin.WalletService.infrastructure.DependencyContainer;
import com.AndreyBrombin.WalletService.model.AccountModel;
import com.AndreyBrombin.WalletService.model.TransactionModel;
import com.AndreyBrombin.WalletService.repository.AccountRepository;
import com.AndreyBrombin.WalletService.repository.WalletRepository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * Сервис для управления личным счетом и операциями с кошельком.
 */
public class PersonalAccountService {
    private DependencyContainer dependencyContainer;

    /**
     * Конструктор класса PersonalAccountService.
     *
     * @param dependencyContainer Контейнер зависимостей, предоставляющий доступ к необходимым сервисам.
     */
    public PersonalAccountService(DependencyContainer dependencyContainer) {
        this.dependencyContainer = dependencyContainer;
    }

    /**
     * Пополнение баланса кошелька.
     *
     * @param walletOwnerId Идентификатор владельца кошелька.
     * @return true, если операция пополнения прошла успешно, в противном случае - false.
     */
    public boolean deposit(BigInteger walletOwnerId) {
        InputHandler inputHandler = dependencyContainer.getInputHandler();
        OutputHandler outputHandler = dependencyContainer.getOutputHandler();
        TransactionService transactionService = dependencyContainer.getTransactionService();

        outputHandler.printMessage("Enter the amount to deposit:");
        BigDecimal amount = inputHandler.readBigDecimal();
        boolean result = transactionService.deposit(walletOwnerId, amount);
        if (result) {
            dependencyContainer.getAuditService().logDeposit(walletOwnerId, amount);
        }
        return result;
    }

    /**
     * Вывод средств с кошелька.
     *
     * @param walletId Идентификатор кошелька.
     * @return true, если операция вывода средств прошла успешно, в противном случае - false.
     */
    public boolean withdraw(BigInteger walletId) {
        InputHandler inputHandler = dependencyContainer.getInputHandler();
        OutputHandler outputHandler = dependencyContainer.getOutputHandler();
        TransactionService transactionService = dependencyContainer.getTransactionService();

        outputHandler.printMessage("Enter the amount to withdraw:");
        BigDecimal amount = inputHandler.readBigDecimal();
        boolean result = transactionService.withdraw(walletId, amount);
        if (result) {
            dependencyContainer.getAuditService().logWithdraw(walletId, amount);
        }
        return result;
    }

    /**
     * Перевод средств на другой кошелек.
     *
     * @param senderId Идентификатор отправителя.
     * @return true, если операция перевода прошла успешно, в противном случае - false.
     */
    public boolean transfer(BigInteger senderId) {
        InputHandler inputHandler = dependencyContainer.getInputHandler();
        OutputHandler outputHandler = dependencyContainer.getOutputHandler();
        TransactionService transactionService = dependencyContainer.getTransactionService();
        AccountRepository accountRepository = dependencyContainer.getAccountRepository();

        outputHandler.printMessage("Enter the recipient's login:");
        String recipientLogin = inputHandler.readLine();
        outputHandler.printMessage("Enter the amount to transfer:");
        BigDecimal amount = inputHandler.readBigDecimal();

        AccountModel recipientAccount = accountRepository.getAccountByLogin(recipientLogin);

        if (recipientAccount != null) {
            boolean result = transactionService.transfer(senderId, recipientAccount.getWalletOwnerId(), amount);
            if (result) {
                dependencyContainer.getAuditService().logTransfer(senderId, recipientAccount.getWalletOwnerId(), amount);
            }
            return result;
        } else {
            outputHandler.printMessage("Recipient not found. Please enter a valid login.");
            return false;
        }
    }

    /**
     * Получение текущего баланса кошелька.
     *
     * @param walletId Идентификатор кошелька.
     * @return Текущий баланс кошелька.
     */
    public BigDecimal getWalletBalance(BigInteger walletId) {
        WalletRepository walletRepository = dependencyContainer.getWalletRepository();
        dependencyContainer.getAuditService().logBalanceRequest(walletId);
        return walletRepository.getWalletBalance(walletId);
    }
    /**
     * Получение тсписка всех транзакций хранящихся в "базе".
     */
    public List<TransactionModel> getAllTransactionsByAccount(BigInteger accountId) {
        return dependencyContainer.getTransactionRepository().getTransactionsByAccount(accountId);
    }
}