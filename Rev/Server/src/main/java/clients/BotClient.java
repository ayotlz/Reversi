package clients;

import server.Server;

import java.io.IOException;
import java.net.Socket;

public class BotClient extends AbstractPlayer {
    public BotClient(Server server, Socket socket) throws IOException {
        super(server, socket);
    }

    @Override
    public void run() {

    }
}
