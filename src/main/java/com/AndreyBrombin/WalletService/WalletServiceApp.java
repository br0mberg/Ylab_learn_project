package com.AndreyBrombin.WalletService;

import com.AndreyBrombin.WalletService.controller.MainMenuController;
import com.AndreyBrombin.WalletService.infrastructure.DependencyContainer;

/**
 * Главный класс приложения Wallet Service.
 * Инициализирует приложение, создавая {@link DependencyContainer},
 * настраивая {@link MainMenuController}, и запуская главное меню.
 */
public class WalletServiceApp {
    public static void main(String[] args) {
        DependencyContainer container = new DependencyContainer();
        MainMenuController mainMenuController = new MainMenuController(container);
        mainMenuController.start();
    }
}