package com.AndreyBrombin.WalletService;

import com.AndreyBrombin.WalletService.infrastructure.logger.CustomLogger;
import com.AndreyBrombin.WalletService.controller.MainMenuController;
import com.AndreyBrombin.WalletService.infrastructure.di.DependencyContainer;
import com.AndreyBrombin.WalletService.jdbc.ConnectionManager;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Главный класс приложения Wallet Service.
 * Инициализирует приложение, создавая {@link DependencyContainer},
 * настраивая {@link MainMenuController}, и запуская главное меню.
 */
public class WalletServiceApp {
    public static void main(String[] args) {
        try (Connection connection = ConnectionManager.open()) {
            Database database = DatabaseFactory.getInstance().
                    findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase(
                    "db/changelog/changelog.xml",
                    new ClassLoaderResourceAccessor(), database);
            liquibase.update();
        } catch (DatabaseException e) {
            CustomLogger.logError("Ошибка подключения бд в Liquibase", e);
        } catch (SQLException e)    {
            CustomLogger.logError("Ошибка в создании коннекта к бд", e);
        } catch (LiquibaseException e) {
            CustomLogger.logError("Ошибка миграции бд в Liquibase", e);
        }

        DependencyContainer container = new DependencyContainer();
        MainMenuController mainMenuController = new MainMenuController(container);
        mainMenuController.start();
    }
}