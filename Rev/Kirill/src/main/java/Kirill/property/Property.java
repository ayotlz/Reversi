package Kirill.property;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public final class Property {
    private static final String PROPERTY_PATH = "./Kirill/src/main/resources/config.properties";
    private static final String PROPERTY_PATH_RESERVED = "resources/config.properties";

    public static int getMaxRecLevel() {
        try {
            final FileInputStream fis = new FileInputStream(PROPERTY_PATH);
            final Properties property = new Properties();
            property.load(fis);
            return Integer.parseInt(property.getProperty("maxRecLevel"));
        } catch (final IOException | NumberFormatException e) {
            try {
                final FileInputStream fis = new FileInputStream(PROPERTY_PATH_RESERVED);
                final Properties property = new Properties();
                property.load(fis);
                return Integer.parseInt(property.getProperty("maxRecLevel"));
            } catch (final IOException | NumberFormatException ex) {
                return -1;
            }
        }
    }
}
