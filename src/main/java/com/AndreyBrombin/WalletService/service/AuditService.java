package com.AndreyBrombin.WalletService.service;


import com.AndreyBrombin.WalletService.infrastructure.logger.CustomLogger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * Класс для аудита действий игрока, таких как авторизация, завершение работы, пополнение счета и снятие средств.
 */
public class AuditService {
    private final String auditLogFilePath;

    /**
     * Создает новый экземпляр AuditService.
     *
     * @param auditLogFilePath Путь к файлу для журнала аудита.
     */
    public AuditService(String auditLogFilePath) {
        this.auditLogFilePath = auditLogFilePath;
    }

    /**
     * Записывает событие авторизации в журнал аудита.
     *
     * @param login Login пользователя, авторизовавшегося.
     */
    public void logLogin(String login) {
        String logMessage = "Авторизация: Пользователь '" + login + "' вошел в систему. Время: " + new Date();
        writeLog(logMessage);
    }

    /**
     * Записывает событие завершения работы в журнал аудита.
     *
     * @param login Login пользователя, завершившего сеанс.
     */
    public void logLogout(String login) {
        String logMessage = "Завершение работы: Пользователь '" + login + "' вышел из системы. Время: " + new Date();
        writeLog(logMessage);
    }

    /**
     * Записывает событие запроса баланса кошелька в журнал аудита.
     *
     * @param walletId Идентификатор кошелька, баланс которого был запрошен.
     */
    public void logBalanceRequest(BigInteger walletId) {
        String logMessage = "Запрос баланса: Запрошен баланс для кошелька '" + walletId.toString() + "'. Время: " + new Date();
        writeLog(logMessage);
    }

    /**
     * Записывает событие пополнения счета в журнал аудита.
     *
     * @param walletId Id кошелька пользователя, пополнившего счет.
     * @param amount   Сумма пополнения.
     */
    public void logDeposit(BigInteger walletId, BigDecimal amount) {
        String logMessage = "Пополнение счета: Кошелёк '" + walletId.toString()
                + "' пополнили на " + amount + " рублей. Время: " + new Date();
        writeLog(logMessage);
    }

    /**
     * Записывает событие снятия средств со счета в журнал аудита.
     *
     * @param walletId Id кошелька пользователя, снявшего средства.
     * @param amount   Сумма снятия.
     */
    public void logWithdraw(BigInteger walletId, BigDecimal amount) {
        String logMessage = "Снятие средств: С кошелька '" + walletId.toString() + "' было снято " + amount + " рублей. Время: " + new Date();
        writeLog(logMessage);
    }

    /**
     * Регистрирует действие перевода средств с одного кошелька на другой в журнале аудита.
     *
     * @param senderId         Идентификатор кошелька отправителя.
     * @param walletReceiverId Идентификатор кошелька получателя.
     * @param amount           Сумма перевода в рублях.
     */
    public void logTransfer(BigInteger senderId, BigInteger walletReceiverId, BigDecimal amount) {
        String logMessage = "Перевод с кошелька: '" + senderId.toString() + "' на кошелёк: '"
                + walletReceiverId + "' на сумму: " + amount + "рублей. Время: " + new Date();
        writeLog(logMessage);
    }

    /**
     * Записывает событие другого действия в журнал аудита.
     *
     * @param login пользователя, снявшего средства., выполнившего действие.
     * @param action   Описание выполняемого действия.
     */
    public void logCustomAction(String login, String action) {
        String logMessage = "Действие: Пользователь '" + login + "' выполнил действие: " + action + ". Время: " + new Date();
        writeLog(logMessage);
    }

    /**
     * Записывает сообщение в журнал аудита.
     *
     * @param logMessage Сообщение для записи в журнал аудита.
     */
    private void writeLog(String logMessage) {
        try (FileWriter fileWriter = new FileWriter(auditLogFilePath, true);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.println(logMessage);
        } catch (IOException e) {
            CustomLogger.logError("Error write to audit file", e);
        }
    }
}



