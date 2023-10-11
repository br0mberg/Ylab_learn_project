package com.AndreyBrombin.WalletService.controller.in;

import com.AndreyBrombin.WalletService.controller.in.InputHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class InputHandlerTest {
    @Mock
    private Scanner scanner;

    private InputHandler inputHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        inputHandler = new InputHandler(scanner);
    }

    @Test
    void testReadLine() {
        // Подготавливаем мок Scanner для ввода
        when(scanner.nextLine()).thenReturn("Test input");

        String input = inputHandler.readLine();

        assertEquals("Test input", input);
    }

    @Test
    void testReadInt() {
        when(scanner.nextLine()).thenReturn("123");

        int number = inputHandler.readInt();

        assertEquals(123, number);
    }

    @Test
    void testReadBigDecimal() {
        when(scanner.nextLine()).thenReturn("45.67");

        BigDecimal decimal = inputHandler.readBigDecimal();

        assertEquals(new BigDecimal("45.67"), decimal);
    }
}