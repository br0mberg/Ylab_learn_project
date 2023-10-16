package com.AndreyBrombin.WalletService.service;

import com.AndreyBrombin.WalletService.controller.in.InputHandler;
import com.AndreyBrombin.WalletService.controller.out.OutputHandler;
import com.AndreyBrombin.WalletService.infrastructure.DependencyContainer;
import com.AndreyBrombin.WalletService.model.AccountModel;
import com.AndreyBrombin.WalletService.repository.AccountRepository;
import com.AndreyBrombin.WalletService.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Юнит-тесты для класса PersonalAccountService.
 */
class PersonalAccountServiceTest {
    @Mock
    private PersonalAccountService personalAccountService;
    @Mock
    private InputHandler inputHandler;
    @Mock
    private OutputHandler outputHandler;
    @Mock
    private TransactionService transactionService;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private WalletRepository walletRepository;

    /**
     * Настройка тестового окружения перед каждым тестом.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        DependencyContainer dependencyContainer = new DependencyContainer();
        dependencyContainer.setInputHandler(inputHandler);
        dependencyContainer.setOutputHandler(outputHandler);
        dependencyContainer.setTransactionService(transactionService);
        dependencyContainer.setAccountRepository(accountRepository);
        dependencyContainer.setWalletRepository(walletRepository);

        personalAccountService = new PersonalAccountService(dependencyContainer);
        inputHandler = dependencyContainer.getInputHandler();
        outputHandler = dependencyContainer.getOutputHandler();
        transactionService = dependencyContainer.getTransactionService();
        accountRepository = dependencyContainer.getAccountRepository();
        walletRepository = dependencyContainer.getWalletRepository();
    }

    /**
     * Тест метода deposit с успешным внесением средств.
     */
    @Test
    void testDeposit() {
        // Arrange
        BigInteger walletId = BigInteger.valueOf(123);
        BigDecimal depositAmount = BigDecimal.valueOf(50.0);

        when(inputHandler.readBigDecimal()).thenReturn(depositAmount);
        when(transactionService.deposit(walletId, depositAmount)).thenReturn(true);

        // Act
        boolean result = personalAccountService.deposit(walletId);

        // Assert
        assertTrue(result);
        verify(outputHandler).printMessage("Enter the amount to deposit:");
        verify(inputHandler).readBigDecimal();
    }

    /**
     * Тест метода withdraw с успешным снятием средств.
     */
    @Test
    void testWithdraw() {
        // Arrange
        BigInteger walletId = BigInteger.valueOf(456);
        BigDecimal withdrawAmount = BigDecimal.valueOf(30.0);

        when(inputHandler.readBigDecimal()).thenReturn(withdrawAmount);
        when(transactionService.withdraw(walletId, withdrawAmount)).thenReturn(true);

        // Act
        boolean result = personalAccountService.withdraw(walletId);

        // Assert
        assertTrue(result);
        verify(outputHandler).printMessage("Enter the amount to withdraw:");
        verify(inputHandler).readBigDecimal();
    }

    /**
     * Тест метода transfer с успешным переводом средств.
     */
    @Test
    void testTransfer_SuccessfulTransfer() {
        // Arrange
        BigInteger senderWalletId = BigInteger.valueOf(123);
        String recipientLogin = "recipient";
        BigDecimal amount = BigDecimal.valueOf(50.0);

        // Создайте мок для recipientAccount
        AccountModel recipientAccount = Mockito.mock(AccountModel.class);

        // Укажите, как должны возвращаться значения из мока recipientAccount
        when(recipientAccount.getWalletId()).thenReturn(BigInteger.valueOf(456)); // Устанавливаем ID кошелька получателя
        // Укажите другие необходимые настройки для recipientAccount, если есть

        // Настройте моки для остальных методов и зависимостей по аналогии

        when(inputHandler.readLine()).thenReturn(recipientLogin);
        when(inputHandler.readBigDecimal()).thenReturn(amount);
        when(accountRepository.getAccountByLogin(recipientLogin)).thenReturn(recipientAccount);
        when(transactionService.transfer(senderWalletId, recipientAccount.getWalletOwnerId(), amount)).thenReturn(true);

        // Act
        boolean result = personalAccountService.transfer(senderWalletId);

        // Assert
        assertTrue(result);
        verify(inputHandler).readBigDecimal();
        verify(transactionService).transfer(senderWalletId, recipientAccount.getWalletOwnerId(), amount);
    }

    /**
     * Тест метода transfer при отсутствии получателя.
     */
    @Test
    void testTransfer_RecipientNotFound() {
        // Arrange
        BigInteger senderWalletId = BigInteger.valueOf(123);
        String recipientLogin = "recipient";
        when(inputHandler.readLine()).thenReturn(recipientLogin);
        when(accountRepository.getAccountByLogin(recipientLogin)).thenReturn(null);

        // Act
        boolean result = personalAccountService.transfer(senderWalletId);

        // Assert
        assertFalse(result);
    }
}
