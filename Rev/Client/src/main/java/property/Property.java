package property;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Property {
    private static final String propertyPath = "./Client/src/main/resources/config.properties";
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

    public static String getIP() {
        try {
            final FileInputStream fis = new FileInputStream(propertyPath);
            final Properties property = new Properties();
            property.load(fis);
            return property.getProperty("IP");
        } catch (final IOException | NumberFormatException e) {
            try {
                final FileInputStream fis = new FileInputStream(propertyPathReserved);
                final Properties property = new Properties();
                property.load(fis);
                return property.getProperty("IP");
            } catch (IOException | NumberFormatException ex) {
                return null;
            }
        }
    }
}
