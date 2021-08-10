import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Property {
    public static String getIP() {
        try {
            final FileInputStream fis = new FileInputStream("./Client/src/main/resources/config.properties");
            final Properties property = new Properties();
            property.load(fis);
            return property.getProperty("IP");
        } catch (final IOException | NumberFormatException ignored) {
            return null;
        }

    }
}
