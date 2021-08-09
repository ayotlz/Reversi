package io.deeplay.reversi;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.deeplay.reversi.exceptions.ReversiErrorCode;
import io.deeplay.reversi.exceptions.ReversiException;
import io.deeplay.reversi.handler.Handler;
import io.deeplay.reversi.models.board.Board;
import io.deeplay.reversi.models.board.Cell;
import io.deeplay.reversi.models.chip.Color;
import io.deeplay.reversi.player.Player;
import io.deeplay.reversi.player.RandomBot;
import io.deeplay.reversi.requests.GameEndRequest;
import io.deeplay.reversi.requests.PlayerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Server {
    static final int PORT = 8082;

    private static final Logger logger = LoggerFactory.getLogger(Handler.class);
    private final ConcurrentLinkedQueue<ServerSomething> serverList = new ConcurrentLinkedQueue<>();
    private final List<Room> roomList = new ArrayList<>();
    private final WriteCSV csvWriter = new WriteCSV();
    private int countOfMadeRoom = 0;

    private class ServerSomething extends Thread {
        private final Server server;
        private final Socket socket;
        private final BufferedReader in;
        private final BufferedWriter out;
        private Color color;

        private ServerSomething(final Server server, final Socket socket) throws IOException {
            this.server = server;
            this.socket = socket;
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        }

        @Override
        public void run() {
            try {
                RoomType roomType = null;
                while (roomType == null) {
                    final String tr = in.readLine();
                    roomType = switch (tr) {
                        case "1" -> RoomType.HumanVsBot;
                        case "2" -> RoomType.HumanVsHuman;
                        case "3" -> RoomType.BotVsBot;
                        default -> null;
                    };
                }
                serverList.add(this);
                color = chooseRoom(roomType).joinRoom(this);
                final StringWriter writer = new StringWriter();
                final ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(writer, color);
                send(writer.toString());
            } catch (final IOException e) {
                this.downService();
            }
        }

        private Room chooseRoom(final RoomType rt) {
            for (final Room room : roomList) {
                if (room.getRoomType() == rt && room.isRoomHasPlace()) {
                    return room;
                }
            }
            Room newRoom = new Room(rt, ++countOfMadeRoom);
            roomList.add(newRoom);
            return newRoom;
        }

        private Color getColor() {
            return color;
        }

        private void send(final String msg) throws IOException {
            out.write(msg + "\n");
            out.flush();
        }

        private void downService() {
            try {
                if (!socket.isClosed()) {
                    socket.close();
                    in.close();
                    out.close();
                    server.serverList.remove(this);
                }
            } catch (final IOException ignored) {
            }
        }
    }

    private enum RoomType {
        HumanVsHuman, HumanVsBot, BotVsBot
    }

    private class Room extends Thread {
        private final int roomID;
        private final List<ServerSomething> players;
        private final List<Player> bots;
        private final Handler handler;
        private final Board board;
        private final RoomType roomType;

        private Room(final RoomType roomType, final int roomID) {
            this.roomID = roomID;
            players = new ArrayList<>();
            handler = new Handler();
            board = new Board();
            this.roomType = roomType;
            bots = new ArrayList<>();
            if (roomType == RoomType.BotVsBot) {
                bots.add(new RandomBot(Color.BLACK));
                bots.add(new RandomBot(Color.WHITE));
            } else if (roomType == RoomType.HumanVsBot) {
                bots.add(new RandomBot(Color.WHITE));
            }
        }

        @Override
        public void run() {
            try {
                handler.initializationBoard(board);
            } catch (final ReversiException ignored) {
            }
            gameProcess();
            gameEnd();
        }

        private void gameProcess() {
            while (!handler.isGameEnd(board)) {
                humanHandler();
                botHandler();
            }
        }

        private void humanHandler() {
            for (final ServerSomething player : players) {
                final Color playerColor = player.getColor();
                if (!handler.haveIStep(board, playerColor) || playerColor == Color.NEUTRAL) {
                    continue;
                }

                while (true) {
                    try {
                        final StringWriter writer = new StringWriter();
                        final ObjectMapper mapper = new ObjectMapper();
                        mapper.writeValue(writer, new PlayerRequest(board, playerColor));
                        sendMessageToAllPlayers(writer.toString());

                        final String answer = player.in.readLine();
                        final StringReader reader = new StringReader(answer);
                        final Cell cell = mapper.readValue(reader, Cell.class);
                        sendMessageToAllPlayers(playerColor + " ставит фишку на клетку: " + cell.toString() + "");

                        formatCsv(playerColor, cell);
                        break;
                    } catch (final ReversiException e) {
                        sendMessageToAllPlayersWithoutException("Некорректный ход\n");
                    } catch (final IOException ignored) {
                    }
                }
            }
        }

        private void botHandler() {
            for (final Player bot : bots) {
                final Color botColor = bot.getPlayerColor();
                if (!handler.haveIStep(board, botColor)) {
                    continue;
                }
                while (true) {
                    try {
                        final StringWriter writer = new StringWriter();
                        final ObjectMapper mapper = new ObjectMapper();
                        mapper.writeValue(writer, new PlayerRequest(board, botColor));
                        sendMessageToAllPlayers(writer.toString());

                        final Cell cell = bot.getAnswer(board);
                        formatCsv(botColor, cell);
                        sendMessageToAllPlayers(botColor + bot.getClass().toString() + " ставит фишку на клетку: " + cell.toString() + "");
                        break;
                    } catch (ReversiException | IOException e) {
                        System.out.println("Бот работает некорректно");
                    }
                }
            }
        }

        private void formatCsv(final Color color, final Cell cell) throws ReversiException {
            final String flipCells;
            try {
                flipCells = String.valueOf(board.getScoreMap(color).get(cell).size());
            } catch (final NullPointerException e) {
                throw new ReversiException(ReversiErrorCode.INCORRECT_PLACE_FOR_CHIP);
            }
            handler.makeStep(board, cell, color);

            final String gameStatus;
            if (handler.isGameEnd(board)) {
                if (handler.getScoreWhite(board) > handler.getScoreBlack(board)) {
                    gameStatus = "Победа белых";
                } else if (handler.getScoreWhite(board) < handler.getScoreBlack(board)) {
                    gameStatus = "Победа чёрных";
                } else {
                    gameStatus = "Ничья";
                }
            } else {
                gameStatus = "Не определён";
            }
            final String room = String.valueOf(roomID);
            final String whiteScore = String.valueOf(handler.getScoreWhite(board));
            final String blackScore = String.valueOf(handler.getScoreBlack(board));
            csvWriter.writeStep(room, color.getString(), cell.toString(),
                    flipCells, whiteScore, blackScore, gameStatus);
        }

        private void gameEnd() {
            try {
                final StringWriter writerPlayerRequest = new StringWriter();
                final ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(writerPlayerRequest, new PlayerRequest(board, Color.NEUTRAL));
                sendMessageToAllPlayers(writerPlayerRequest.toString());

                final StringWriter writerGameEndRequest = new StringWriter();
                final int scoreBlack = handler.getScoreBlack(board);
                final int scoreWhite = handler.getScoreWhite(board);
                mapper.writeValue(writerGameEndRequest, new GameEndRequest(scoreBlack, scoreWhite));
                sendMessageToAllPlayers(writerGameEndRequest.toString());
                sendMessageToAllPlayers("Черные: " + scoreBlack);
                sendMessageToAllPlayers("Белые: " + scoreWhite);

                if (scoreBlack > scoreWhite) {
                    sendMessageToAllPlayers("Чёрные победили");
                } else if (scoreBlack < scoreWhite) {
                    sendMessageToAllPlayers("Белые победили");
                } else {
                    sendMessageToAllPlayers("Ничья");
                }
                sendMessageToAllPlayers("Игра закончилась");

                for (final ServerSomething player : players) {
                    player.downService();
                }
                roomList.remove(this);
            } catch (final IOException ignored) {
            }
        }

        private void sendMessageToAllPlayersWithoutException(final String message) {
            try {
                sendMessageToAllPlayers(message);
            } catch (IOException ignored) {
            }
        }

        private void sendMessageToAllPlayers(final String message) throws IOException {
            for (final ServerSomething ss : players) {
                ss.send(message);
            }
        }

        private RoomType getRoomType() {
            return roomType;
        }

        private boolean isRoomHasPlace() {
            if (roomType == RoomType.HumanVsBot) return players.size() < 1;
            if (roomType == RoomType.HumanVsHuman) return players.size() < 2;
            return false;
        }

        private Color joinRoom(final ServerSomething ss) {
            if (roomType == RoomType.BotVsBot) {
                players.add(ss);
                start();
                return Color.NEUTRAL;
            } else if (roomType == RoomType.HumanVsBot) {
                players.add(ss);
                start();
                return Color.BLACK;
            } else {
                players.add(ss);
                if (players.size() == 1) {
                    return Color.BLACK;
                } else if (players.size() == 2) {
                    start();
                    return Color.WHITE;
                }
                return null;
            }
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private void startServer() throws IOException {
        System.out.println("Сервер запущен");
        logger.debug("Сервер запущен");
        try (final ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                final Socket socket = serverSocket.accept();
                try {
                    new ServerSomething(this, socket).start();
                } catch (final IOException e) {
                    socket.close();
                }
            }
        } catch (final BindException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(final String[] args) throws IOException {
        final Server server = new Server();
        server.startServer();
    }
}
