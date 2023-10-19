package com.AndreyBrombin.WalletService.jdbc;

import com.AndreyBrombin.WalletService.Logger.CustomLogger;
import com.AndreyBrombin.WalletService.util.PropertiesUtil;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionManager {
    private static final String URL_KEY="db.url";
    private static final String USERNAME_KEY="db.username";
    private static final String PASSWORD_KEY="db.password";

    public static Connection open() {
        try {
            return DriverManager.getConnection(
                    PropertiesUtil.getProperty(URL_KEY),
                    PropertiesUtil.getProperty(USERNAME_KEY),
                    PropertiesUtil.getProperty(PASSWORD_KEY));
        } catch (SQLException e) {
            CustomLogger.logError("Ошибка при подключении к базе данных.", e);
            return null;
        }
    }
}
