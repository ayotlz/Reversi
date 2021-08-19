package property;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Property {
    private static final String propertyPath = "./Ayotlz/src/main/resources/config.properties";
    private static final String propertyPathReserved = "resources/config.properties";

    public static int getDeep() {
        try {
            final FileInputStream fis = new FileInputStream(propertyPath);
            final Properties property = new Properties();
            property.load(fis);
            return Integer.parseInt(property.getProperty("deep"));
        } catch (final IOException | NumberFormatException e) {
            try {
                final FileInputStream fis = new FileInputStream(propertyPathReserved);
                final Properties property = new Properties();
                property.load(fis);
                return Integer.parseInt(property.getProperty("deep"));
            } catch (IOException | NumberFormatException ex) {
                return -1;
            }
        }
    }

    public static int getMCGames() {
        try {
            final FileInputStream fis = new FileInputStream(propertyPath);
            final Properties property = new Properties();
            property.load(fis);
            return Integer.parseInt(property.getProperty("montecarlo"));
        } catch (final IOException | NumberFormatException e) {
            try {
                final FileInputStream fis = new FileInputStream(propertyPathReserved);
                final Properties property = new Properties();
                property.load(fis);
                return Integer.parseInt(property.getProperty("montecarlo"));
            } catch (IOException | NumberFormatException ex) {
                return -1;
            }
        }
    }

    public static int getSecondDeep() {
        try {
            final FileInputStream fis = new FileInputStream(propertyPath);
            final Properties property = new Properties();
            property.load(fis);
            return Integer.parseInt(property.getProperty("secondDeep"));
        } catch (final IOException | NumberFormatException e) {
            try {
                final FileInputStream fis = new FileInputStream(propertyPathReserved);
                final Properties property = new Properties();
                property.load(fis);
                return Integer.parseInt(property.getProperty("secondDeep"));
            } catch (IOException | NumberFormatException ex) {
                return -1;
            }
        }
    }

    public static int getStartSecondDeep() {
        try {
            final FileInputStream fis = new FileInputStream(propertyPath);
            final Properties property = new Properties();
            property.load(fis);
            return Integer.parseInt(property.getProperty("startSecondDeep"));
        } catch (final IOException | NumberFormatException e) {
            try {
                final FileInputStream fis = new FileInputStream(propertyPathReserved);
                final Properties property = new Properties();
                property.load(fis);
                return Integer.parseInt(property.getProperty("startSecondDeep"));
            } catch (IOException | NumberFormatException ex) {
                return -1;
            }
        }
    }

    public static int getStartFullTree() {
        try {
            final FileInputStream fis = new FileInputStream(propertyPath);
            final Properties property = new Properties();
            property.load(fis);
            return Integer.parseInt(property.getProperty("startFullTree"));
        } catch (final IOException | NumberFormatException e) {
            try {
                final FileInputStream fis = new FileInputStream(propertyPathReserved);
                final Properties property = new Properties();
                property.load(fis);
                return Integer.parseInt(property.getProperty("startFullTree"));
            } catch (IOException | NumberFormatException ex) {
                return -1;
            }
        }
    }


}
