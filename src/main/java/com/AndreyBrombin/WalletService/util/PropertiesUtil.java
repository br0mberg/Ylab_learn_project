package com.AndreyBrombin.WalletService.util;

import com.AndreyBrombin.WalletService.Logger.CustomLogger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Утилита для работы с конфигурационными данными.
 */
public final class PropertiesUtil {
    private static final Properties PROPERTIES = new Properties();
    static {
        loadProperties();
    }

    private static void loadProperties() {
        try(InputStream inputStream = PropertiesUtil.
                class.
                getClassLoader().
                getResourceAsStream("application.properties")) {
            PROPERTIES.load(inputStream);
        } catch(IOException e) {
            CustomLogger.logError("Ошибка загрузки данных из property файла.", e);
        }
    }
    /**
     * Метод для получения свойства по ключу.
     * @return String - значение свойства.
     */
    public static String getProperty(String key) {
        return PROPERTIES.getProperty(key);
    }

    private PropertiesUtil() {}
}
