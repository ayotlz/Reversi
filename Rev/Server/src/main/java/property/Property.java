package property;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Property {
    private static final String propertyPath = "./Server/src/main/resources/config.properties";
    private static final String propertyPathReserved = "resources/config.properties";

    public static int getPort() {
        try {
            final FileInputStream fis = new FileInputStream(propertyPath);
            final Properties property = new Properties();
            property.load(fis);
            return Integer.parseInt(property.getProperty("PORT"));
        } catch (final IOException | NumberFormatException e) {
            try {
                final FileInputStream fis = new FileInputStream(propertyPathReserved);
                final Properties property = new Properties();
                property.load(fis);
                return Integer.parseInt(property.getProperty("PORT"));
            } catch (IOException | NumberFormatException ex) {
                return -1;
            }
        }
    }

    public static int getCountOfBots() {
        try {
            final FileInputStream fis = new FileInputStream(propertyPath);
            final Properties property = new Properties();
            property.load(fis);
            return Integer.parseInt(property.getProperty("bots"));
        } catch (final IOException | NumberFormatException e) {
            try {
                final FileInputStream fis = new FileInputStream(propertyPathReserved);
                final Properties property = new Properties();
                property.load(fis);
                return Integer.parseInt(property.getProperty("bots"));
            } catch (IOException | NumberFormatException ex) {
                return -1;
            }
        }
    }

    public static int getCountOfGames() {
        try {
            final FileInputStream fis = new FileInputStream(propertyPath);
            final Properties property = new Properties();
            property.load(fis);
            return Integer.parseInt(property.getProperty("games"));
        } catch (final IOException | NumberFormatException e) {
            try {
                final FileInputStream fis = new FileInputStream(propertyPathReserved);
                final Properties property = new Properties();
                property.load(fis);
                return Integer.parseInt(property.getProperty("games"));
            } catch (IOException | NumberFormatException ex) {
                return -1;
            }
        }
    }

    public static String getBot1() {
        try {
            final FileInputStream fis = new FileInputStream(propertyPath);
            final Properties property = new Properties();
            property.load(fis);
            return property.getProperty("bot1");
        } catch (final IOException | NumberFormatException e) {
            try {
                final FileInputStream fis = new FileInputStream(propertyPathReserved);
                final Properties property = new Properties();
                property.load(fis);
                return property.getProperty("bot1");
            } catch (IOException | NumberFormatException ex) {
                return null;
            }
        }
    }

    public static String getBot2() {
        try {
            final FileInputStream fis = new FileInputStream(propertyPath);
            final Properties property = new Properties();
            property.load(fis);
            return property.getProperty("bot2");
        } catch (final IOException | NumberFormatException e) {
            try {
                final FileInputStream fis = new FileInputStream(propertyPathReserved);
                final Properties property = new Properties();
                property.load(fis);
                return property.getProperty("bot2");
            } catch (IOException | NumberFormatException ex) {
                return null;
            }
        }
    }
}
