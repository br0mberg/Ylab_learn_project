package com.AndreyBrombin.WalletService.Logger;

import com.AndreyBrombin.WalletService.infrastructure.DependencyContainer;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;

public class CustomLogger {
    private static final Logger logger = getLogger(new DependencyContainer().getConfigService().getProperty("project.name"));

    static {
        try {
            FileHandler fileHandler = new FileHandler(new DependencyContainer().getConfigService().getProperty("path.save.log"));
            fileHandler.setLevel(Level.ALL);
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void logInfo(String message) {
        logMessage(Level.INFO, message);
    }

    public static void logWarning(String message) {
        logMessage(Level.WARNING, message);
    }

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
