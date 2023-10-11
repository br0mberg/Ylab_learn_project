package com.AndreyBrombin.WalletService.controller.out;

import java.io.PrintStream;

/**
 * Класс, предоставляющий метод для вывода сообщений в консоль.
 */
public class OutputHandler {
    private PrintStream consoleOut;

    /**
     * Создает новый экземпляр класса OutputHandler с заданным потоком вывода.
     * @param consoleOut Поток вывода, куда будут выводиться сообщения.
     */
    public OutputHandler(PrintStream consoleOut) {
        this.consoleOut = consoleOut;
    }

    /**
     * Выводит сообщение в консоль с переводом строки.
     * @param message Сообщение для вывода.
     */
    public void printMessage(String message) {
        consoleOut.println(message);
    }
}