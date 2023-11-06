package com.AndreyBrombin.WalletService.controller;

import com.AndreyBrombin.WalletService.Logger.CustomLogger;
import com.AndreyBrombin.WalletService.controller.in.InputHandler;
import com.AndreyBrombin.WalletService.controller.out.OutputHandler;
import com.AndreyBrombin.WalletService.infrastructure.DependencyContainer;
import com.AndreyBrombin.WalletService.repository.AccountRepository;
import com.AndreyBrombin.WalletService.service.Authorization.LoginService;
import com.AndreyBrombin.WalletService.service.Authorization.RegistrationService;
import com.AndreyBrombin.WalletService.util.ConfigUtil;

/**
 * Класс, управляющий главным меню приложения.
 */
public class MainMenuController {
    private DependencyContainer dependencyContainer;

    /**
     * Создает новый экземпляр класса MainMenuController.
     * @param dependencyContainer Контейнер зависимостей, предоставляющий необходимые сервисы.
     */
    public MainMenuController(DependencyContainer dependencyContainer) {
        this.dependencyContainer = dependencyContainer;
    }
    /**
     * Запускает главное меню приложения.
     */
    public void start() {
        boolean isRunning = true;
        OutputHandler outputHandler = dependencyContainer.getOutputHandler();
        ConfigUtil configUtil = dependencyContainer.getConfigService();
        InputHandler inputHandler = dependencyContainer.getInputHandler();

        outputHandler.printMessage(configUtil.getProperty("welcome.message"));

        while (isRunning) {
            outputHandler.printMessage(configUtil.getProperty("welcome.menu.message"));
            int choice = inputHandler.readInt();

            switch (choice) {
                case 1:
                    handleLogin();
                    break;
                case 2:
                    handleRegistration();
                    break;
                case 3:
                    isRunning = false;
                    outputHandler.printMessage(configUtil.getProperty("goodbye.message"));
                    break;
                default:
                    outputHandler.printMessage(configUtil.getProperty("input.error"));
            }
        }
    }
    /**
     * Обрабатывает процесс входа в систему.
     */
    public void handleLogin() {
        OutputHandler outputHandler = dependencyContainer.getOutputHandler();
        ConfigUtil configUtil = dependencyContainer.getConfigService();
        InputHandler inputHandler = dependencyContainer.getInputHandler();
        LoginService loginService = dependencyContainer.getLoginService();
        AccountRepository accountRepository = dependencyContainer.getAccountRepository();

        try {
            outputHandler.printMessage(configUtil.getProperty("login.prompt"));
            String login = inputHandler.readLine();
            outputHandler.printMessage(configUtil.getProperty("password.prompt"));
            String password = inputHandler.readLine();

            if (loginService.authenticateAccount(login, password)) {
                outputHandler.printMessage(configUtil.getProperty("valid.credentials"));
                PersonalAccountController personalAccountController = new PersonalAccountController(
                        dependencyContainer,
                        loginService.getAccountByLogin(login));
                dependencyContainer.getAuditService().logLogin(login);
                personalAccountController.start();
            } else {
                outputHandler.printMessage(configUtil.getProperty("invalid.credentials"));
            }
        } catch (Exception e) {
            CustomLogger.logError("An error occurred: " + e.getMessage(), e);
        }
    }

    private void handleRegistration() {
        OutputHandler outputHandler = dependencyContainer.getOutputHandler();
        ConfigUtil configUtil = dependencyContainer.getConfigService();
        InputHandler inputHandler = dependencyContainer.getInputHandler();
        RegistrationService registrationService = dependencyContainer.getRegistrationService();

        try {
            outputHandler.printMessage(configUtil.getProperty("login.prompt"));
            String login = inputHandler.readLine();
            outputHandler.printMessage(configUtil.getProperty("password.prompt"));
            String password = inputHandler.readLine();
            outputHandler.printMessage(configUtil.getProperty("name.prompt"));
            String name = inputHandler.readLine();
            outputHandler.printMessage(configUtil.getProperty("surname.prompt"));
            String surname = inputHandler.readLine();

            if (registrationService.registerAccount(name, surname, login, password, dependencyContainer.getWalletRepository())) {
                outputHandler.printMessage(configUtil.getProperty("registration.success"));
            } else {
                outputHandler.printMessage(configUtil.getProperty("registration.error"));
            }
        } catch (Exception e) {
            CustomLogger.logError("An error occurred: " + e.getMessage(), e);
        }
    }
}
