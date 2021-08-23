package property;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public final class Property {
    private static final String PROPERTY_PATH = "./Client/src/main/resources/config.properties";
    private static final String PROPERTY_PATH_RESERVED = "resources/config.properties";

    public static int getPort() {
        try {
            final FileInputStream fis = new FileInputStream(PROPERTY_PATH);
            final Properties property = new Properties();
            property.load(fis);
            return Integer.parseInt(property.getProperty("PORT"));
        } catch (final IOException | NumberFormatException e) {
            try {
                final FileInputStream fis = new FileInputStream(PROPERTY_PATH_RESERVED);
                final Properties property = new Properties();
                property.load(fis);
                return Integer.parseInt(property.getProperty("PORT"));
            } catch (final IOException | NumberFormatException ex) {
                return -1;
            }
        }
    }

    public static String getIP() {
        try {
            final FileInputStream fis = new FileInputStream(PROPERTY_PATH);
            final Properties property = new Properties();
            property.load(fis);
            return property.getProperty("IP");
        } catch (final IOException | NumberFormatException e) {
            try {
                final FileInputStream fis = new FileInputStream(PROPERTY_PATH_RESERVED);
                final Properties property = new Properties();
                property.load(fis);
                return property.getProperty("IP");
            } catch (final IOException | NumberFormatException ex) {
                return null;
            }
        }
    }

    public static String getNick() {
        try {
            final FileInputStream fis = new FileInputStream(PROPERTY_PATH);
            final Properties property = new Properties();
            property.load(fis);
            return property.getProperty("NICK");
        } catch (final IOException | NumberFormatException e) {
            try {
                final FileInputStream fis = new FileInputStream(PROPERTY_PATH_RESERVED);
                final Properties property = new Properties();
                property.load(fis);
                return property.getProperty("NICK");
            } catch (final IOException | NumberFormatException ex) {
                return null;
            }
        }
    }

    public static String getTypeOfRoom() {
        try {
            final FileInputStream fis = new FileInputStream(PROPERTY_PATH);
            final Properties property = new Properties();
            property.load(fis);
            return property.getProperty("typeOfRoom");
        } catch (final IOException | NumberFormatException e) {
            try {
                final FileInputStream fis = new FileInputStream(PROPERTY_PATH_RESERVED);
                final Properties property = new Properties();
                property.load(fis);
                return property.getProperty("typeOfRoom");
            } catch (final IOException | NumberFormatException ex) {
                return null;
            }
        }
    }
}
