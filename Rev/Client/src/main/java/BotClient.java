import com.fasterxml.jackson.databind.ObjectMapper;
import models.board.Board;
import models.board.Cell;
import models.chip.Color;
import player.Player;
import player.RandomBot;
import property.Property;
import requests.PlayerRequest;

import java.io.*;
import java.net.Socket;

public class BotClient {
    private static final String IP = Property.getIP();
    private static final int PORT = Property.getPort();
    private final String ip;
    private final int port;

    private Socket socket = null;
    private BufferedReader in = null;
    private BufferedWriter out = null;
    private Player player;

    private BotClient(final String ip, final int port) {
        this.ip = ip;
        this.port = port;
    }

    private void startClient() {
        try {
            socket = new Socket(this.ip, this.port);
        } catch (final IOException e) {
            return;
        }

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (final IOException e) {
            downService();
            return;
        }

        try {
            send("RandomBot");
        } catch (final IOException e) {
            downService();
            return;
        }

        new ProcessingMessage().start();
    }

    private void send(final String message) throws IOException {
        out.write(message + "\n");
        out.flush();
    }

    private void downService() {
        try {
            if (!socket.isClosed()) {
                socket.close();
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            }
        } catch (final IOException ignored) {
        }
    }

    private class ProcessingMessage extends Thread {
        @Override
        public void run() {
            while (true) {
                String message;

                try {
                    message = in.readLine();
                } catch (IOException e) {
                    downService();
                    break;
                }

                if (message == null) {
                    continue;
                }

                try {
                    final Color color = parseColor(message);
                    player = new RandomBot(color);
                    continue;
                } catch (IOException ignored) {
                }

                try {
                    final PlayerRequest request = parsePlayerRequest(message);
                    final Board board = request.getBoard();
                    final Color turnOrder = request.getColor();
                    sendAnswer(board, turnOrder);
                } catch (IOException ignored) {
                }
            }
        }
    }

    private Color parseColor(final String message) throws IOException {
        final StringReader reader = new StringReader(message);
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(reader, Color.class);
    }

    private void sendAnswer(final Board board, final Color turnOrder) throws IOException {
        if (turnOrder == player.getPlayerColor()) {
            final ObjectMapper mapper = new ObjectMapper();
            final StringWriter writer = new StringWriter();
            final Cell cell = player.getAnswer(board);
            mapper.writeValue(writer, cell);
            send(writer.toString());
        }
    }

    private PlayerRequest parsePlayerRequest(final String message) throws IOException {
        final StringReader reader = new StringReader(message);
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(reader, PlayerRequest.class);
    }

    public static void main(final String[] args) {
        final BotClient botClient = new BotClient(IP, PORT);
        botClient.startClient();
    }
}