package com.javarush.khasanov.config;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import static com.javarush.khasanov.config.Config.CLASSES_ROOT;

public class ApplicationProperties extends Properties {

    public static final String HIBERNATE_CONNECTION_DRIVER_CLASS = "hibernate.connection.driver_class";
    public static final String APPLICATION_PROPERTIES = "/application.properties";

    public ApplicationProperties() {
        try {
            this.load(new FileReader(CLASSES_ROOT + APPLICATION_PROPERTIES));
            String driver = this.getProperty(HIBERNATE_CONNECTION_DRIVER_CLASS);
            Class.forName(driver);
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}