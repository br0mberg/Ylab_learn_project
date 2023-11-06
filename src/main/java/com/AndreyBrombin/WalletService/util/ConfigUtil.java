package com.AndreyBrombin.WalletService.util;

import com.AndreyBrombin.WalletService.Logger.CustomLogger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Сервис для работы с конфигурационными данными.
 */
public class ConfigUtil {
    private static final String CONFIG_FILE = "src/main/resources/config.properties";
    private Properties properties;

    /**
     * Конструктор класса ConfigService. Загружает конфигурационные данные из файла.
     */
    public ConfigUtil() {
        properties = new Properties();
        try {
            FileInputStream fileInputStream = new FileInputStream(CONFIG_FILE);
            properties.load(fileInputStream);
            fileInputStream.close();
        } catch (IOException e) {
            CustomLogger.logError("Error get data from config file", e);
        }
    }

    /**
     * Получает значение настройки по ключу.
     *
     * @param key Ключ настройки.
     * @return Значение настройки или null, если настройка не найдена.
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Устанавливает новый набор настроек.
     *
     * @param properties Новый набор настроек в формате Properties.
     */
    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}