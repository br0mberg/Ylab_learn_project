package com.AndreyBrombin.WalletService;

import com.AndreyBrombin.WalletService.Logger.CustomLogger;
import com.AndreyBrombin.WalletService.controller.MainMenuController;
import com.AndreyBrombin.WalletService.infrastructure.DependencyContainer;
import com.AndreyBrombin.WalletService.jdbc.ConnectionManager;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;

/**
 * Главный класс приложения Wallet Service.
 * Инициализирует приложение, создавая {@link DependencyContainer},
 * настраивая {@link MainMenuController}, и запуская главное меню.
 */
public class WalletServiceApp {
    public static void main(String[] args) {
        Connection connection = ConnectionManager.open();
        try {
            Database database = DatabaseFactory.getInstance().
                    findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase(
                    "db/changelog/changelog.xml",
                    new ClassLoaderResourceAccessor(), database);
            liquibase.update();
        } catch(DatabaseException e) {
            CustomLogger.logError("Ошибка подключения бд в Liquibase", e);
        } catch (LiquibaseException e) {
            CustomLogger.logError("Ошибка миграции бд в Liquibase", e);
        }

        DependencyContainer container = new DependencyContainer();
        MainMenuController mainMenuController = new MainMenuController(container);
        mainMenuController.start();
    }
}