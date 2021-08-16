package clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.chip.Color;
import server.Server;

import java.io.IOException;
import java.io.StringWriter;
import java.net.Socket;

public class BotClient extends AbstractPlayer {
    public BotClient(Server server, Socket socket) throws IOException {
        super(server, socket);
    }

    @Override
    public void run() {
        try {
            nickName = in.readLine();
        } catch (IOException e) {
            this.downService();
        }
    }

    public void setColor(final Color color) throws IOException {
        super.color = color;
        final ObjectMapper mapper = new ObjectMapper();
        final StringWriter writer = new StringWriter();
        mapper.writeValue(writer, color);
        send(writer.toString());
    }
}
