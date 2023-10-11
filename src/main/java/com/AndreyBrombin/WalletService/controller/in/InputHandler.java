package com.AndreyBrombin.WalletService.controller.in;

import java.math.BigDecimal;
import java.util.Scanner;

/**
 * Класс, предоставляющий методы для считывания ввода пользователя.
 * Позволяет считывать строки, целые числа и числа с плавающей запятой из входного потока.
 */
public class InputHandler {
    private Scanner scanner;

    /**
     * Создает новый экземпляр класса InputHandler с заданным сканером.
     * @param scanner Объект Scanner для чтения ввода.
     */
    public InputHandler(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Считывает строку из входного потока.
     * @return Строка, считанная из входного потока.
     */
    public String readLine() {
        return scanner.nextLine();
    }

    /**
     * Считывает целое число из входного потока.
     * @return Считанное целое число или 0, если ввод не является числом.
     */
    public int readInt() {
        try {
            String input = scanner.nextLine();
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Считывает число с плавающей запятой из входного потока.
     * @return Считанное число с плавающей запятой или BigDecimal.ZERO, если ввод не является числом.
     */
    public BigDecimal readBigDecimal() {
        try {
            String input = scanner.nextLine();
            return new BigDecimal(input);
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }
}