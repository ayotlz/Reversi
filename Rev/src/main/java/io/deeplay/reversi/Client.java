package io.deeplay.reversi;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.deeplay.reversi.GUI.GUI;
import io.deeplay.reversi.player.HumanPlayer;
import io.deeplay.reversi.player.Player;
import io.deeplay.reversi.player.RandomBot;
import io.deeplay.reversi.models.board.Board;
import io.deeplay.reversi.models.board.Cell;
import io.deeplay.reversi.models.chip.Color;
import io.deeplay.reversi.requests.GameEndRequest;
import io.deeplay.reversi.requests.PlayerRequest;

import java.io.*;
import java.net.Socket;

public class Client {

    private static final String IP = "127.0.0.1";
    private static final int PORT = Server.PORT;

    private final String ip;
    private final int port;

    private Socket socket = null;
    private BufferedReader in = null;
    private BufferedReader inputUser = null;
    private BufferedWriter out = null;
    private Player player;
    private GUI gui;

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
            inputUser = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (final IOException e) {
            downService();
            return;
        }

        System.out.println("Клиент запущен");
        chooseRoom();
        System.out.println(player.getPlayerColor());
        new ProcessingMessage().start();
    }

    private void chooseRoom() {
        System.out.println("1: Human Vs Bot\n2: Human Vs Human\n3: Bot Vs Bot");
        try {
            String choice = "0";
            while (!choice.equals("1") && !choice.equals("2") && !choice.equals("3")) {
                choice = inputUser.readLine();

                if (choice.equals("1") || choice.equals("2")) {
                    send(choice);
                    player = new HumanPlayer(getColor());
                } else if (choice.equals("3")) {
                    send(choice);
                    player = new RandomBot(getColor());
                }
            }
        } catch (IOException e) {
            downService();
        }
    }

    private Color getColor() {
        try {
            String str = in.readLine();
            final StringReader reader = new StringReader(str);
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(reader, Color.class);
        } catch (final IOException e) {
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
            if (player.getClass() != RandomBot.class) {
                gui = new GUI(player.getPlayerColor());
            }

            while (true) {
                try {
                    message = in.readLine();
                } catch (final IOException e) {
                    downService();
                    break;
                }

                try {
                    final StringReader reader = new StringReader(message);
                    final ObjectMapper mapper = new ObjectMapper();
                    final GameEndRequest request = mapper.readValue(reader, GameEndRequest.class);
                    final int scoreBlack = request.getScoreBlack();
                    final int scoreWhite = request.getScoreWhite();
                    if (gui != null) {
                        gui.winLoseWindow(scoreBlack, scoreWhite);
                    }
                    continue;
                } catch (final IOException ignored) {
                } catch (final NullPointerException e) {
                    downService();
                    continue;
                }
                try {
                    final StringReader reader = new StringReader(message);
                    final ObjectMapper mapper = new ObjectMapper();
                    final PlayerRequest request = mapper.readValue(reader, PlayerRequest.class);
                    final Board board = request.getBoard();
                    final Color turnOrder = request.getColor();
                    System.out.println(board.toString());
                    if (gui != null) {
                        gui.drawActiveBoard(board);
                    }
                    if (player.getPlayerColor() == Color.NEUTRAL) {
                        continue;
                    }

                    if (turnOrder == player.getPlayerColor()) {
                        final StringWriter writer = new StringWriter();
                        Cell cell;
                        if (player.getClass() != RandomBot.class && gui != null) {
                            cell = gui.getAnswerCell(board);
                            while (cell == null) {
                                cell = gui.getAnswerCell(board);
                            }
                        } else {
                            cell = player.getAnswer(board);
                        }
                        mapper.writeValue(writer, cell);
                        send(writer.toString());
                    }
                    continue;
                } catch (final IOException ignored) {
                } catch (final NullPointerException e) {
                    downService();
                    continue;
                }
                System.out.println(message);
            }
        }
    }

    public static void main(final String[] args) {
        final Client client = new Client(IP, PORT);
        client.startClient();
    }
}
