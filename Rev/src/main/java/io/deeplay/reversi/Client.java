package io.deeplay.reversi;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.deeplay.reversi.GUI.GUI;
import io.deeplay.reversi.handler.Handler;
import io.deeplay.reversi.player.HumanPlayer;
import io.deeplay.reversi.player.Player;
import io.deeplay.reversi.player.RandomBot;
import io.deeplay.reversi.models.board.Board;
import io.deeplay.reversi.models.board.Cell;
import io.deeplay.reversi.models.chip.Color;
import io.deeplay.reversi.requests.GameEndRequest;
import io.deeplay.reversi.requests.PlayerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

public class Client {

    private static final Logger logger = LoggerFactory.getLogger(Handler.class);
//    private static final String IP = "192.168.7.79";
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
            logger.error("Socket failed");
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

        logger.debug("Клиент запущен");
        System.out.println("Клиент запущен");
        enterNickName();
        chooseRoom();
        new ProcessingMessage().start();
    }

    private void enterNickName() {
        System.out.println("Введите ваше имя");
        try {
            final String nick = inputUser.readLine();
            send(nick);
        } catch (final IOException e) {
            downService();
        }
    }

    private void chooseRoom() {
        System.out.println("1: Human Vs Bot\n2: Human Vs Human\n3: Bot Vs Bot");
        try {
            String choice = "0";
            while (!choice.equals("1") && !choice.equals("2") && !choice.equals("3")) {
                choice = inputUser.readLine();

                if (choice.equals("1") || choice.equals("2")) {
                    send(choice);
                    player = new HumanPlayer(chooseColor());
                    logger.debug("Клиент запущен в комнату");
                } else if (choice.equals("3")) {
                    send(choice);
                    player = new RandomBot(Color.NEUTRAL);
                }
            }
        } catch (IOException e) {
            downService();
        }
    }

    private Color chooseColor() {
        System.out.println("1: Black\n2: White");
        while (true) {
            try {
                String choice = inputUser.readLine();

                if (choice.equals("1")) {
                    send(choice);
                    return Color.BLACK;
                }
                if (choice.equals("2")) {
                    send(choice);
                    return Color.WHITE;
                }
            } catch (IOException e) {
                downService();
            }
        }
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
            while (true) {
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
                        parseGameEnd(message);
                        break;
                    } catch (final IOException ignored) {
                    } catch (final NullPointerException e) {
                        downService();
                        continue;
                    }
                    try {
                        final PlayerRequest request = parsePlayerRequest(message);
                        final Board board = request.getBoard();
                        final Color turnOrder = request.getColor();
                        System.out.println(board.toString());
                        if (gui != null) {
                            gui.drawActiveBoard(board);
                        }
                        if (player.getPlayerColor() == Color.NEUTRAL) {
                            continue;
                        }

                        sendAnswer(board, turnOrder);
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

        private void parseGameEnd(final String message) throws IOException {
            final StringReader reader = new StringReader(message);
            final ObjectMapper mapper = new ObjectMapper();
            final GameEndRequest request = mapper.readValue(reader, GameEndRequest.class);
            final int scoreBlack = request.getScoreBlack();
            final int scoreWhite = request.getScoreWhite();
            if (gui != null) {
                gui.winLoseWindow(scoreBlack, scoreWhite);
            }
        }

        private PlayerRequest parsePlayerRequest(final String message) throws IOException {
            final StringReader reader = new StringReader(message);
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(reader, PlayerRequest.class);
        }

        private void sendAnswer(Board board, Color turnOrder) throws IOException {
            if (turnOrder == player.getPlayerColor()) {
                final ObjectMapper mapper = new ObjectMapper();
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
        }
    }

    public static void main(final String[] args) {
        final Client client = new Client(IP, PORT);
        client.startClient();
    }
}
