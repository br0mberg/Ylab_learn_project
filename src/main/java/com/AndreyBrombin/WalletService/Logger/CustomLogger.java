package com.AndreyBrombin.WalletService.Logger;

import com.AndreyBrombin.WalletService.infrastructure.DependencyContainer;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;

/**
 * Класс `CustomLogger` предоставляет методы для записи журнала логирования с различными уровнями.
 * Он позволяет записывать сообщения об информации, предупреждения и ошибки в журнале логов.
 */
public class CustomLogger {
    private static final Logger logger = getLogger(new DependencyContainer().getConfigService().getProperty("project.name"));

    static {
        try {
            DependencyContainer container = new DependencyContainer();
            String projectName = container.getConfigService().getProperty("project.name");
            String logPath = container.getConfigService().getProperty("path.save.log");

            if (projectName == null || logPath == null) {
                throw new IOException("Unable to retrieve project name or log file path.");
            }

            FileHandler fileHandler = new FileHandler(logPath);
            fileHandler.setLevel(Level.ALL);
            logger.addHandler(fileHandler);
        } catch (IOException | SecurityException e) {
            e.printStackTrace();
        }
    }

    /**
     * Записывает сообщение в журнал логов с уровнем INFO.
     *
     * @param message Сообщение для записи в журнал логов.
     */
    public static void logInfo(String message) {
        logMessage(Level.INFO, message);
    }
    /**
     * Записывает предупреждение в журнал логов с уровнем WARNING.
     *
     * @param message Предупреждающее сообщение для записи в журнал логов.
     */
    public static void logWarning(String message) {
        logMessage(Level.WARNING, message);
    }

    /**
     * Записывает ошибку в журнал логов с уровнем SEVERE.
     *
     * @param message Пояснение к ошибке для записи в журнал логов.
     * @param e       Исключение, связанное с ошибкой.
     */
    public static void logError(String message, Throwable e) {
        logMessage(Level.SEVERE, message, e);
    }

    private static void logMessage(Level level, String message, Throwable e) {
        StackTraceElement stackTrace = Thread.currentThread().getStackTrace()[2];
        String methodName = stackTrace.getMethodName();
        String className = stackTrace.getClassName();

        String logMessage = String.format("%s.%s: %s", className, methodName, message);
        logger.log(level, logMessage, e);
    }

    private static void logMessage(Level level, String message) {
        StackTraceElement stackTrace = Thread.currentThread().getStackTrace()[2];
        String methodName = stackTrace.getMethodName();
        String className = stackTrace.getClassName();

        String logMessage = String.format("%s.%s: %s", className, methodName, message);
        logger.log(level, logMessage);
    }
}
