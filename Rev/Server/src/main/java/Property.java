import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Property {
    public static int getPort() {
        try {
            final FileInputStream fis = new FileInputStream("./Server/src/main/resources/config.properties");
            final Properties property = new Properties();
            property.load(fis);
            return Integer.parseInt(property.getProperty("PORT"));
        } catch (final IOException | NumberFormatException ignored) {
            return -1;
        }
    }

    public static int getCountOfGames() {
        try {
            final FileInputStream fis = new FileInputStream("./Server/src/main/resources/config.properties");
            final Properties property = new Properties();
            property.load(fis);
            return Integer.parseInt(property.getProperty("games"));
        } catch (final IOException | NumberFormatException ignored) {
            return -1;
        }
    }
}
