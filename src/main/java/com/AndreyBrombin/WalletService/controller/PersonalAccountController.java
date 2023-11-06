package com.AndreyBrombin.WalletService.controller;

import com.AndreyBrombin.WalletService.infrastructure.logger.CustomLogger;
import com.AndreyBrombin.WalletService.controller.in.InputHandler;
import com.AndreyBrombin.WalletService.controller.out.OutputHandler;
import com.AndreyBrombin.WalletService.infrastructure.di.DependencyContainer;
import com.AndreyBrombin.WalletService.model.AccountModel;
import com.AndreyBrombin.WalletService.model.TransactionModel;
import com.AndreyBrombin.WalletService.infrastructure.util.ConfigUtil;
import com.AndreyBrombin.WalletService.service.PersonalAccountService;

import java.math.BigInteger;
import java.util.List;
/**
 * Контроллер для управления личным кабинетом аккаунта пользователя.
 */
public class PersonalAccountController {
    private AccountModel currentAccount;
    private DependencyContainer dependencyContainer;

    /**
     * Создает новый экземпляр контроллера личного кабинета аккаунта пользователя.
     *
     * @param dependencyContainer Контейнер зависимостей, предоставляющий доступ к необходимым службам и компонентам.
     * @param currentAccount     Текущий аккаунт пользователя, для которого запускается личный кабинет.
     */
    public PersonalAccountController(DependencyContainer dependencyContainer,AccountModel currentAccount) {
        this.currentAccount = currentAccount;
        this.dependencyContainer = dependencyContainer;
    }

    /**
     * Запускает контроллер личного кабинета аккаунта пользователя.
     */
    public void start() {
        OutputHandler outputHandler = dependencyContainer.getOutputHandler();
        ConfigUtil configUtil = dependencyContainer.getConfigService();
        InputHandler inputHandler = dependencyContainer.getInputHandler();
        PersonalAccountService personalAccountService = dependencyContainer.getPersonalAccountService();

        outputHandler.printMessage("Добро пожаловать, " + currentAccount.getName() + " " + currentAccount.getSurname() + "!");

        boolean isRunning = true;

        while (isRunning) {
            printMenu();
            int choice = inputHandler.readInt();

            try {
                switch (choice) {
                    case 1:
                        handleDeposit(outputHandler, configUtil, personalAccountService);
                        break;
                    case 2:
                        handleWithdraw(outputHandler, configUtil, personalAccountService);
                        break;
                    case 3:
                        handleTransfer(outputHandler, configUtil, personalAccountService);
                        break;
                    case 4:
                        handleBalance(outputHandler, configUtil, personalAccountService);
                        break;
                    case 5:
                        printAllTransactions(personalAccountService, currentAccount.getWalletId());
                        break;
                    case 6:
                        dependencyContainer.getAuditService().logLogout(currentAccount.getLogin());
                        isRunning = false;
                        break;
                    default:
                        outputHandler.printMessage(configUtil.getProperty("input.error"));
                }
            } catch (Exception e) {
                CustomLogger.logError("An error occurred: " + e.getMessage(), e);
            }
        }
    }

    private void handleDeposit(OutputHandler outputHandler, ConfigUtil configUtil, PersonalAccountService personalAccountService) {
        boolean depositSuccess = personalAccountService.deposit(currentAccount.getWalletOwnerId());
        if (depositSuccess) {
            outputHandler.printMessage(configUtil.getProperty("success.deposit.message"));
        } else {
            outputHandler.printMessage(configUtil.getProperty("error.operation.message"));
        }
    }

    private void handleWithdraw(OutputHandler outputHandler, ConfigUtil configUtil, PersonalAccountService personalAccountService) {
        boolean withdrawSuccess = personalAccountService.withdraw(currentAccount.getWalletOwnerId());
        if (withdrawSuccess) {
            outputHandler.printMessage(configUtil.getProperty("success.withdraw.message"));
        } else {
            outputHandler.printMessage(configUtil.getProperty("error.operation.message"));
        }
    }

    private void handleTransfer(OutputHandler outputHandler, ConfigUtil configUtil, PersonalAccountService personalAccountService) {
        boolean transferSuccess = personalAccountService.transfer(currentAccount.getWalletOwnerId());
        if (transferSuccess) {
            outputHandler.printMessage(configUtil.getProperty("success.transfer.message"));
        } else {
            outputHandler.printMessage(configUtil.getProperty("error.operation.message"));
        }
    }

    private void handleBalance(OutputHandler outputHandler, ConfigUtil configUtil, PersonalAccountService personalAccountService) {
        outputHandler.printMessage(configUtil.getProperty("balance.message") +
                String.valueOf(personalAccountService.getWalletBalance(currentAccount.getWalletOwnerId())));
    }

    private void printMenu() {
        OutputHandler outputHandler = dependencyContainer.getOutputHandler();
        ConfigUtil configUtil = dependencyContainer.getConfigService();
        outputHandler.printMessage(configUtil.getProperty("personal.menu.message"));
    }

    private void printAllTransactions(PersonalAccountService personalAccountService, BigInteger accountId) {
        List<TransactionModel> userTransactions = personalAccountService.getAllTransactionsByAccount(accountId);
        OutputHandler outputHandler = dependencyContainer.getOutputHandler();

        if (userTransactions.isEmpty()) {
            outputHandler.printMessage("У вас пока нет ни одной транзакции.");
        } else {
            outputHandler.printMessage("Список ваших транзакций:");
            for (TransactionModel transaction : userTransactions) {
                outputHandler.printMessage("Тип: " + transaction.getTransactionType());
                outputHandler.printMessage("Сумма: " + transaction.getAmount());
                outputHandler.printMessage("Дата: " + transaction.getTransactionDate());
                outputHandler.printMessage("-----");
            }
        }
    }
}