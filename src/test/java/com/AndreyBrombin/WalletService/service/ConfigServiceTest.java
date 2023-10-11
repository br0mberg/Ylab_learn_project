package com.AndreyBrombin.WalletService.service;

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

class ConfigServiceTest {
    private ConfigService configService;

    @Mock
    private Properties properties;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Инициализация моков
        configService = new ConfigService();
        configService.setProperties(properties);
    }

    @Test
    void testGetProperty() {
        // Arrange
        String key = "testKey";
        String expectedValue = "testValue";

        // Устанавливаем ожидаемое значение свойства
        when(properties.getProperty(key)).thenReturn(expectedValue);

        // Act
        String actualValue = configService.getProperty(key);

        // Assert
        assertEquals(expectedValue, actualValue);

        // Проверим, что метод getProperty был вызван с ожидаемым ключом
        verify(properties).getProperty(key);
    }

    @Test
    void testIOExceptionInConstructor() throws IOException {
        // Arrange
        Properties mockProperties = mock(Properties.class);

        doThrow(new IOException()).when(mockProperties).load(any(InputStream.class));
        when(properties.getProperty(any())).thenReturn("testValue");

        // Act
        ConfigService configServiceWithException = new ConfigService();

        // Assert
        // Убедимся, что свойство properties осталось пустым
        assertEquals(null, configServiceWithException.getProperty("testKey"));
    }
}
