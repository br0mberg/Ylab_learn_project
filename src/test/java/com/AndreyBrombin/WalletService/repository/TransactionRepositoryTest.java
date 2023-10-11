package com.AndreyBrombin.WalletService.repository;

import com.AndreyBrombin.WalletService.model.TransactionModel;
import com.AndreyBrombin.WalletService.model.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.util.List;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TransactionRepositoryTest {
    private TransactionRepository transactionRepository;

    @TempDir
    File tempDir;

    @BeforeEach
    void setUp() {
        // Используем временную директорию, где будет создан временный файл
        File tempFile = new File(tempDir, "test_transactions.dat");
        transactionRepository = new TransactionRepository(tempFile.getAbsolutePath());
    }

    @Test
    void testAddTransaction() {
        // Arrange
        TransactionModel transaction = new TransactionModel(
                BigInteger.valueOf(1),
                BigInteger.valueOf(2),
                BigDecimal.valueOf(100.0),
                new Date(),
                TransactionType.TRANSFER
        );

        // Act
        transactionRepository.addTransaction(transaction);

        // Get all transactions for debugging
        List<TransactionModel> transactions = transactionRepository.getAllTransactions();

        // Debug output
        System.out.println("All Transactions:");
        for (TransactionModel t : transactions) {
            System.out.println(t.toString());
        }

        // Assert
        assertTrue(transactions.contains(transaction));
    }


    @Test
    void testLoadTransactionsFromFile() {
        // Arrange
        TransactionModel transaction1 = new TransactionModel(
                BigInteger.valueOf(1),
                BigInteger.valueOf(2),
                BigDecimal.valueOf(50.0),
                new Date(),
                TransactionType.TRANSFER
        );
        TransactionModel transaction2 = new TransactionModel(
                BigInteger.valueOf(2),
                BigInteger.valueOf(3),
                BigDecimal.valueOf(75.0),
                new Date(),
                TransactionType.TRANSFER
        );
        TransactionModel transaction3 = new TransactionModel(
                BigInteger.valueOf(3),
                BigInteger.valueOf(4),
                BigDecimal.valueOf(30.0),
                new Date(),
                TransactionType.TRANSFER
        );

        // Save transactions to the test file
        transactionRepository.addTransaction(transaction1);
        transactionRepository.addTransaction(transaction2);
        transactionRepository.addTransaction(transaction3);

        // Create a new repository with the same file path
        TransactionRepository newTransactionRepository = new TransactionRepository(transactionRepository.getFilePath());

        // Act
        List<TransactionModel> loadedTransactions = newTransactionRepository.getAllTransactions();

        // Assert
        assertTrue(loadedTransactions.contains(transaction1));
        assertTrue(loadedTransactions.contains(transaction2));
        assertTrue(loadedTransactions.contains(transaction3));
    }

    @Test
    void testSaveTransactionsToFile() {
        // Arrange
        TransactionModel transaction1 = new TransactionModel(
                BigInteger.valueOf(1),
                BigInteger.valueOf(2),
                BigDecimal.valueOf(50.0),
                new Date(),
                TransactionType.TRANSFER
        );
        TransactionModel transaction2 = new TransactionModel(
                BigInteger.valueOf(2),
                BigInteger.valueOf(3),
                BigDecimal.valueOf(75.0),
                new Date(),
                TransactionType.TRANSFER
        );
        TransactionModel transaction3 = new TransactionModel(
                BigInteger.valueOf(3),
                BigInteger.valueOf(4),
                BigDecimal.valueOf(30.0),
                new Date(),
                TransactionType.TRANSFER
        );

        // Act
        transactionRepository.addTransaction(transaction1);
        transactionRepository.addTransaction(transaction2);
        transactionRepository.addTransaction(transaction3);

        // Create a new repository with the same file path
        TransactionRepository newTransactionRepository = new TransactionRepository(transactionRepository.getFilePath());

        // Get the loaded transactions
        List<TransactionModel> loadedTransactions = newTransactionRepository.getAllTransactions();

        // Assert
        assertTrue(loadedTransactions.contains(transaction1));
        assertTrue(loadedTransactions.contains(transaction2));
        assertTrue(loadedTransactions.contains(transaction3));
    }
}
