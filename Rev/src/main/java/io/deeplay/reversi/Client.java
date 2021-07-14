package io.deeplay.reversi;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.deeplay.reversi.bot.HumanPlayer;
import io.deeplay.reversi.bot.Player;
import io.deeplay.reversi.bot.RandomBot;
import io.deeplay.reversi.models.board.Board;
import io.deeplay.reversi.models.board.Cell;
import io.deeplay.reversi.models.chip.Color;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Client {

    private static final String IP = "127.0.0.1";//"localhost";
    private static final int PORT = Server.PORT;

    private final String ip; // ip адрес клиента
    private final int port; // порт соединения

    private Socket socket = null;
    private BufferedReader in = null; // поток чтения из сокета
    private BufferedWriter out = null; // поток записи в сокет
    private Player player;

    /**
     * для создания необходимо принять адрес и номер порта
     *
     * @param ip   ip адрес клиента
     * @param port порт соединения
     */
    private Client(final String ip, final int port) {
        this.ip = ip;
        this.port = port;
    }

    private void startClient() {
        try {
            socket = new Socket(this.ip, this.port);
        } catch (final IOException e) {
            System.err.println("Socket failed");
            return;
        }

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (final IOException e) {
            downService();
            return;
        }

        System.out.printf("Client started, ip: %s, port: %d%n", ip, port);
        player = new HumanPlayer(getColor());
        System.out.println(player.getPlayerColor());
        new ProcessingMessage().start();
    }

    private Color getColor() {
        try {
            String str = in.readLine();
            System.out.println(str);
            final StringReader reader = new StringReader(str);
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(reader, Color.class);
        } catch (IOException e) {
            downService();
        }
        downService();
        return null;
    }

    /**
     * отсылка одного сообщения клиенту
     *
     * @param message сообщение
     */
    private void send(final String message) throws IOException {
        out.write(message + "\n");
        out.flush();
    }

    /**
     * закрытие сокета
     */
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
            String message;
            try {
                while (true) {
                    message = in.readLine(); // ждем сообщения с сервера
                    if (message.equals("Game end")) {
                        break;
                    }

                    final StringReader reader = new StringReader(message);
                    final ObjectMapper mapper = new ObjectMapper();
                    final Board board = mapper.readValue(reader, Board.class);
                    System.out.println(board.toString());
                    final StringWriter writer = new StringWriter();
                    final Cell cell = player.getAnswer(board);
                    mapper.writeValue(writer, cell);
                    send(writer.toString());
                }
            } catch (final IOException ignored) {
//                System.out.println("АШИБКА");
//                downService();
            }
        }
    }

    public static void main(final String[] args) {
        final Client client = new Client(IP, PORT);
        client.startClient();
    }
}
