package com.AndreyBrombin.WalletService.infrastructure;

import com.AndreyBrombin.WalletService.controller.in.InputHandler;
import com.AndreyBrombin.WalletService.controller.out.OutputHandler;
import com.AndreyBrombin.WalletService.repository.*;
import com.AndreyBrombin.WalletService.service.Authorization.LoginService;
import com.AndreyBrombin.WalletService.service.Authorization.RegistrationService;
import com.AndreyBrombin.WalletService.service.ConfigService;
import com.AndreyBrombin.WalletService.service.PersonalAccountService;
import com.AndreyBrombin.WalletService.service.TransactionService;

import java.io.PrintStream;
import java.util.Scanner;

/**
 * Класс, представляющий контейнер зависимостей для приложения. Он инициализирует и предоставляет доступ к различным службам и репозиториям,
 * необходимым для функционирования приложения.
 */
public class DependencyContainer {
    private ConfigService configService;
    private TransactionService transactionService;
    private OutputHandler outputHandler;
    private InputHandler inputHandler;
    private AccountRepository accountRepository;
    private WalletRepository walletRepository;
    private RegisterRepository registerRepository;
    private PersonalAccountService personalAccountService;
    private RegistrationService registrationService;
    private LoginRepository loginRepository;
    private TransactionRepository transactionRepository;
    private LoginService loginService;

    /**
     * Конструктор класса DependencyContainer. Инициализирует все зависимости при создании экземпляра.
     */
    public DependencyContainer() {
        this.configService = new ConfigService();

        this.outputHandler = new OutputHandler(new PrintStream(System.out));
        this.inputHandler = new InputHandler(new Scanner(System.in));
        this.accountRepository = new AccountRepository("src/main/resources/accounts.dat");
        this.walletRepository = new WalletRepository("src/main/resources/wallets.dat");
        this.registerRepository = new RegisterRepository(accountRepository, walletRepository);
        this.registrationService = new RegistrationService(registerRepository);
        this.transactionRepository = new TransactionRepository("src/main/resources/transactions.dat");
        this.transactionService = new TransactionService(transactionRepository, walletRepository);
        this.loginRepository = new LoginRepository(this.accountRepository);
        this.loginService = new LoginService(loginRepository, accountRepository);
        this.personalAccountService = new PersonalAccountService(this);
    }

    /**
     * Получить объект службы конфигурации.
     * @return Объект ConfigService.
     */
    public ConfigService getConfigService() {
        return configService;
    }

    /**
     * Получить объект обработчика вывода.
     * @return Объект OutputHandler.
     */
    public OutputHandler getOutputHandler() {
        return outputHandler;
    }

    /**
     * Получить объект обработчика ввода.
     * @return Объект InputHandler.
     */
    public InputHandler getInputHandler() {
        return inputHandler;
    }

    /**
     * Получить объект службы входа в систему.
     * @return Объект LoginService.
     */
    public LoginService getLoginService() {
        return loginService;
    }

    /**
     * Получить объект службы регистрации.
     * @return Объект RegistrationService.
     */
    public RegistrationService getRegistrationService() {
        return registrationService;
    }

    /**
     * Получить объект репозитория аккаунтов.
     * @return Объект AccountRepository.
     */
    public AccountRepository getAccountRepository() {
        return accountRepository;
    }

    /**
     * Получить объект репозитория кошельков.
     * @return Объект WalletRepository.
     */
    public WalletRepository getWalletRepository() {
        return walletRepository;
    }

    /**
     * Получить объект службы управления персональным счетом.
     * @return Объект PersonalAccountService.
     */
    public PersonalAccountService getPersonalAccountService() {
        return personalAccountService;
    }

    /**
     * Получить объект службы транзакций.
     * @return Объект TransactionService.
     */
    public TransactionService getTransactionService() {
        return transactionService;
    }

    /**
     * Установить объект обработчика вывода.
     * @param outputHandler Объект OutputHandler для установки.
     */
    public void setOutputHandler(OutputHandler outputHandler) {
        this.outputHandler = outputHandler;
    }

    /**
     * Установить объект службы конфигурации.
     * @param configService Объект ConfigService для установки.
     */
    public void setConfigService(ConfigService configService) {
        this.configService = configService;
    }

    /**
     * Установить объект обработчика ввода.
     * @param inputHandler Объект InputHandler для установки.
     */
    public void setInputHandler(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
    }

    /**
     * Установить объект службы входа в систему.
     * @param loginService Объект LoginService для установки.
     */
    public void setLoginService(LoginService loginService) {
        this.loginService = loginService;
    }

    /**
     * Установить объект службы регистрации.
     * @param registrationService Объект RegistrationService для установки.
     */
    public void setRegistrationService(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    /**
     * Установить объект репозитория аккаунтов.
     * @param accountRepository Объект AccountRepository для установки.
     */
    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Установить объект репозитория кошельков.
     * @param walletRepository Объект WalletRepository для установки.
     */
    public void setWalletRepository(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    /**
     * Установить объект службы управления персональным счетом.
     * @param personalAccountService Объект PersonalAccountService для установки.
     */
    public void setPersonalAccountService(PersonalAccountService personalAccountService) {
        this.personalAccountService = personalAccountService;
    }

    /**
     * Установить объект службы транзакций.
     * @param transactionService Объект TransactionService для установки.
     */
    public void setTransactionService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Получить объект репозитория транзакций.
     * @return Объект TransactionRepository.
     */
    public TransactionRepository getTransactionRepository() {
        return transactionRepository;
    }

    /**
     * Получить объект репозитория входа в систему.
     * @return Объект LoginRepository.
     */
    public LoginRepository getLoginRepository() {
        return loginRepository;
    }

    /**
     * Получить объект репозитория регистрации.
     * @return Объект RegisterRepository.
     */
    public RegisterRepository getRegisterRepository() {
        return registerRepository;
    }
}