package com.AndreyBrombin.WalletService.controller.out;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Юнит-тесты для класса OutputHandler.
 */
class OutputHandlerTest {
    private OutputHandler outputHandler;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalSystemOut;

    /**
     * Настройка тестового окружения перед каждым тестом.
     */
    @BeforeEach
    public void setUp() {
        originalSystemOut = System.out; // Сохраняем оригинальный System.out
        outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream); // Перенаправляем System.out на outputStream
        outputHandler = new OutputHandler(printStream);
    }

    /**
     * Тест для проверки вывода сообщения.
     */
    @Test
    public void testPrintMessage() {
        String message = "Test message";
        outputHandler.printMessage(message);
        String output = outputStream.toString().trim(); // Удалите лишние переводы строк
        assertEquals(message, output);
    }

    /**
     * Восстановление оригинального System.out после каждого теста.
     */
    @AfterEach
    public void restoreSystemOut() {
        System.setOut(originalSystemOut); // Восстанавливаем оригинальный System.out
    }
}
