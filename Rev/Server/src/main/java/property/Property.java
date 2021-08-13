package property;

import Ayotlz.AyotlzBot;
import Kirill.MiniMaxBot;
import models.chip.Color;
import player.Player;
import player.RandomBot;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Property {
    private static final String propertyPath = "./Server/src/main/resources/config.properties";
//    private static final String propertyPath = "../src/main/resources/config.properties";

    public static int getPort() {
        try {
            final FileInputStream fis = new FileInputStream(propertyPath);
            final Properties property = new Properties();
            property.load(fis);
            return Integer.parseInt(property.getProperty("PORT"));
        } catch (final IOException | NumberFormatException ignored) {
            return -1;
        }
    }

    public static int getCountOfGames() {
        try {
            final FileInputStream fis = new FileInputStream(propertyPath);
            final Properties property = new Properties();
            property.load(fis);
            return Integer.parseInt(property.getProperty("games"));
        } catch (final IOException | NumberFormatException ignored) {
            return -1;
        }
    }

    public static Player getBot1(Color color) {
        String botName;
        try {
            final FileInputStream fis = new FileInputStream(propertyPath);
            final Properties property = new Properties();
            property.load(fis);
            return getBot(property.getProperty("bot1"), color);
        } catch (final IOException | NumberFormatException ignored) {
            return null;
        }
    }

    public static Player getBot2(Color color) {
        String botName;
        try {
            final FileInputStream fis = new FileInputStream(propertyPath);
            final Properties property = new Properties();
            property.load(fis);
            return getBot(property.getProperty("bot2"), color);
        } catch (final IOException | NumberFormatException ignored) {
            return null;
        }
    }

    private static Player getBot(String botName, Color color) {
        return switch (botName) {
            case "RandomBot" -> new RandomBot(color);
            case "AyotlzBot" -> new AyotlzBot(color);
//            case "" -> new MiniMaxBot(color);
            default -> null;
        };
    }
}
