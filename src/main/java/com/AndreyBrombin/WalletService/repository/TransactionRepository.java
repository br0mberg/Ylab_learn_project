package com.AndreyBrombin.WalletService.repository;

import com.AndreyBrombin.WalletService.model.TransactionModel;

import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Репозиторий, отвечающий за управление транзакциями.
 * Этот репозиторий позволяет добавлять транзакции и получать список всех существующих транзакций.
 */
public class TransactionRepository {
    private List<TransactionModel> transactions;
    private static final Logger logger = Logger.getLogger(TransactionRepository.class.getName());
    private String filePath;

    /**
     * Создает новый экземпляр репозитория транзакций с указанным путем к файлу.
     *
     * @param filePath Путь к файлу, в котором хранятся данные о транзакциях.
     */
    public TransactionRepository(String filePath) {
        this.filePath = filePath;
        transactions = new ArrayList<>();
    }

    /**
     * Добавляет новую транзакцию в репозиторий и сохраняет изменения в файл.
     *
     * @param transaction Новая транзакция для добавления.
     */
    public void addTransaction(TransactionModel transaction) {
        transactions.add(transaction);
        saveTransactionsToFile();
    }

    /**
     * Возвращает список всех существующих транзакций, загружая их из файла при необходимости.
     *
     * @return Список всех транзакций.
     */
    public List<TransactionModel> getAllTransactions() {
        loadTransactionsFromFile();
        return transactions;
    }

    /**
     * Выгружает список всех существующих транзакций из файла.
     */
    private void loadTransactionsFromFile() {
        try {
            File file = new File(filePath);

            if (file.exists() && file.length() > 0) {
                FileInputStream fileInputStream = new FileInputStream(filePath);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                transactions = (List<TransactionModel>) objectInputStream.readObject();
                objectInputStream.close();
                logger.log(Level.INFO, "Transactions loaded from file.");
            } else {
                logger.log(Level.INFO, "Transaction file is empty or does not exist.");
            }
        } catch (IOException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Error loading transactions from file.", e);
        }
    }
    /**
     * Сохраняет все существующие(новые) транзакции в файл.
     */
    private void saveTransactionsToFile() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(transactions);
            objectOutputStream.close();
            logger.log(Level.INFO, "Transactions saved to file.");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error saving transactions to file.", e);
        }
    }

    /**
     * Возвращает путь к файлу, в котором хранятся данные о транзакциях.
     *
     * @return Путь к файлу с данными о транзакциях.
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * Проверяет, существует ли транзакция с указанным идентификатором.
     *
     * @param transactionId Идентификатор транзакции для проверки.
     * @return true, если транзакция с указанным идентификатором существует, в противном случае - false.
     */
    public boolean doesTransactionExist(BigInteger transactionId) {
        loadTransactionsFromFile(); // Убедитесь, что транзакции загружены из файла

        for (TransactionModel transaction : transactions) {
            if (transaction.getId().equals(transactionId)) {
                return true; // Транзакция с указанным идентификатором существует
            }
        }

        return false; // Транзакция с указанным идентификатором не существует
    }
}