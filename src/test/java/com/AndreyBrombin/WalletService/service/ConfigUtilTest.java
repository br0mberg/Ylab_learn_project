package com.AndreyBrombin.WalletService.service;

import com.AndreyBrombin.WalletService.util.ConfigUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Юнит-тесты для класса ConfigService.
 */
class ConfigUtilTest {
    private ConfigUtil configUtil;

    @Mock
    private Properties properties;

    /**
     * Настройка тестового окружения перед каждым тестом.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Инициализация моков
        configUtil = new ConfigUtil();
        configUtil.setProperties(properties);
    }

    /**
     * Тест получения значения свойства по ключу.
     */
    @Test
    void testGetProperty() {
        // Arrange
        String key = "testKey";
        String expectedValue = "testValue";

        // Устанавливаем ожидаемое значение свойства
        when(properties.getProperty(key)).thenReturn(expectedValue);

        // Act
        String actualValue = configUtil.getProperty(key);

        // Assert
        assertEquals(expectedValue, actualValue);

        // Проверим, что метод getProperty был вызван с ожидаемым ключом
        verify(properties).getProperty(key);
    }

    /**
     * Тест обработки исключения при создании ConfigService.
     */
    @Test
    void testIOExceptionInConstructor() throws IOException {
        // Arrange
        Properties mockProperties = mock(Properties.class);

        doThrow(new IOException()).when(mockProperties).load(any(InputStream.class));
        when(properties.getProperty(any())).thenReturn("testValue");

        // Act
        ConfigUtil configUtilWithException = new ConfigUtil();

        // Assert
        // Убедимся, что свойство properties осталось пустым
        assertEquals(null, configUtilWithException.getProperty("testKey"));
    }
}
