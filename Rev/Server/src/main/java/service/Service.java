package service;

import clients.BotClient;
import property.Property;
import server.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Service {
    final Map<BotClient, Integer> bots = new HashMap<>();
    final int countOfBots = Property.getCountOfBots();

    public void addBot(final BotClient botClient) {
        bots.put(botClient, 0);
    }

    public void awaitingBots(final Server server, final ServerSocket serverSocket) throws IOException {
        for (int i = 0; i < countOfBots; i++) {
            final Socket socket = serverSocket.accept();
            try {
                final BotClient bc = new BotClient(server, socket);
                addBot(bc);
                bc.start();
            } catch (final IOException e) {
                socket.close();
            }
        }
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
