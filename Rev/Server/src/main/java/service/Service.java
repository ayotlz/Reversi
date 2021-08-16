package service;

import clients.BotClient;

import java.util.HashMap;
import java.util.Map;

public class Service {
    final Map<BotClient, Integer> bots = new HashMap<>();

    public void addBot(final BotClient botClient) {
        bots.put(botClient, 0);
    }

    public BotClient getBot() {
        while (true) {
            for (Map.Entry<BotClient, Integer> entry : bots.entrySet()) {
                if (entry.getValue() == 0) {
                    bots.put(entry.getKey(), 1);
                    return entry.getKey();
                }
            }
        }
    }
}
