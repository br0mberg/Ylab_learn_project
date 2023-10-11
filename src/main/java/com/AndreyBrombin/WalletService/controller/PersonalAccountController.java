package com.AndreyBrombin.WalletService.controller;

import com.AndreyBrombin.WalletService.controller.in.InputHandler;
import com.AndreyBrombin.WalletService.controller.out.OutputHandler;
import com.AndreyBrombin.WalletService.infrastructure.DependencyContainer;
import com.AndreyBrombin.WalletService.model.AccountModel;
import com.AndreyBrombin.WalletService.model.TransactionModel;
import com.AndreyBrombin.WalletService.service.ConfigService;
import com.AndreyBrombin.WalletService.service.PersonalAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PersonalAccountController {
    private static final Logger logger = LoggerFactory.getLogger(PersonalAccountController.class);
    private AccountModel currentAccount;
    private DependencyContainer dependencyContainer;

    public PersonalAccountController(DependencyContainer dependencyContainer,AccountModel currentAccount) {
        this.currentAccount = currentAccount;
        this.dependencyContainer = dependencyContainer;
    }

    /**
     * Запускает контроллер личного кабинета аккаунта пользователя.
     */
    public void start() {
        OutputHandler outputHandler = dependencyContainer.getOutputHandler();
        ConfigService configService = dependencyContainer.getConfigService();
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
                        handleDeposit(outputHandler, configService, personalAccountService);
                        break;
                    case 2:
                        handleWithdraw(outputHandler, configService, personalAccountService);
                        break;
                    case 3:
                        handleTransfer(outputHandler, configService, personalAccountService);
                        break;
                    case 4:
                        handleBalance(outputHandler, configService, personalAccountService);
                        break;
                    case 5:
                        printAllTransactions(personalAccountService); // Добавляем опцию вывода всех транзакций
                        break;
                    case 6:
                        isRunning = false;
                        break;
                    default:
                        outputHandler.printMessage(configService.getProperty("input.error"));
                }
            } catch (Exception e) {
                logger.error("An error occurred: " + e.getMessage(), e);
            }
        }
    }
    /**
     * Выполнение запроса пользователя - депозит.
     */
    private void handleDeposit(OutputHandler outputHandler, ConfigService configService, PersonalAccountService personalAccountService) {
        boolean depositSuccess = personalAccountService.deposit(currentAccount.getWalletOwnerId());
        if (depositSuccess) {
            outputHandler.printMessage(configService.getProperty("success.deposit.message"));
        } else {
            outputHandler.printMessage(configService.getProperty("error.operation.message"));
        }
    }
    /**
     * Выполнение запроса пользователя - снятие средств.
     */
    private void handleWithdraw(OutputHandler outputHandler, ConfigService configService, PersonalAccountService personalAccountService) {
        boolean withdrawSuccess = personalAccountService.withdraw(currentAccount.getWalletOwnerId());
        if (withdrawSuccess) {
            outputHandler.printMessage(configService.getProperty("success.withdraw.message"));
        } else {
            outputHandler.printMessage(configService.getProperty("error.operation.message"));
        }
    }
    /**
     * Выполнение запроса пользователя - перевод другому пользователю.
     */
    private void handleTransfer(OutputHandler outputHandler, ConfigService configService, PersonalAccountService personalAccountService) {
        boolean transferSuccess = personalAccountService.transfer(currentAccount.getWalletOwnerId());
        if (transferSuccess) {
            outputHandler.printMessage(configService.getProperty("success.transfer.message"));
        } else {
            outputHandler.printMessage(configService.getProperty("error.operation.message"));
        }
    }
    /**
     * Выполнение запроса пользователя - получение баланса.
     */
    private void handleBalance(OutputHandler outputHandler, ConfigService configService, PersonalAccountService personalAccountService) {
        outputHandler.printMessage(configService.getProperty("balance.message") +
                String.valueOf(personalAccountService.getWalletBalance(currentAccount.getWalletOwnerId())));
    }


    /**
     * Выводит меню личного кабинета аккаунта.
     */
    private void printMenu() {
        OutputHandler outputHandler = dependencyContainer.getOutputHandler();
        ConfigService configService = dependencyContainer.getConfigService();
        outputHandler.printMessage(configService.getProperty("personal.menu.message"));
    }

    private void printAllTransactions(PersonalAccountService personalAccountService) {
        List<TransactionModel> userTransactions = personalAccountService.getAllTransactions();
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