package clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.chip.Color;
import requests.BotRequest;
import server.Server;

import java.io.IOException;
import java.io.StringWriter;
import java.net.Socket;

public class BotClient extends AbstractPlayer {
    public BotClient(Server server, Socket socket) throws IOException {
        super(server, socket);
    }

    @Override
    public final void run() {
        try {
            nickName = in.readLine();
        } catch (IOException e) {
            this.downService();
        }
    }

    public final void sendBotInfo(final String botName, final Color color) throws IOException {
        super.nickName = botName;
        super.color = color;
        final BotRequest botRequest = new BotRequest(botName, color);
        final ObjectMapper mapper = new ObjectMapper();
        final StringWriter writer = new StringWriter();
        mapper.writeValue(writer, botRequest);
        send(writer.toString());
    }
}
