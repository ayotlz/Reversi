package io.deeplay.reversi;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.deeplay.reversi.bot.RandomBot;
import io.deeplay.reversi.exceptions.ReversiException;
import io.deeplay.reversi.handler.Handler;
import io.deeplay.reversi.models.board.Board;
import io.deeplay.reversi.models.board.Cell;
import io.deeplay.reversi.models.chip.Color;
import io.deeplay.reversi.requests.GameEndRequest;
import io.deeplay.reversi.requests.PlayerRequest;

import java.io.*;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Server {
    static final int PORT = 8082;

    private final ConcurrentLinkedQueue<ServerSomething> serverList = new ConcurrentLinkedQueue<>();
    private final List<Room> roomList = new ArrayList<>();

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
                    String tr = in.readLine();
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

        private Room chooseRoom(RoomType rt) {
            for (Room room : roomList) {
                if (room.getRoomType() == rt && room.isRoomHasPlace()) {
                    return room;
                }
            }
            Room newRoom = new Room(rt);
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
        private final List<ServerSomething> players;
        private final List<RandomBot> randomBots;
        private final Handler handler;
        private final Board board;
        private final RoomType roomType;

        private Room(RoomType roomType) {
            players = new ArrayList<>();
            handler = new Handler();
            board = new Board();
            this.roomType = roomType;
            randomBots = new ArrayList<>();
            if (roomType == RoomType.BotVsBot) {
                randomBots.add(new RandomBot(Color.BLACK));
                randomBots.add(new RandomBot(Color.WHITE));
            } else if (roomType == RoomType.HumanVsBot) {
                randomBots.add(new RandomBot(Color.WHITE));
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
                for (ServerSomething player : players) {
                    if (player.getColor() == Color.NEUTRAL) {
                        try {
                            final StringWriter writer = new StringWriter();
                            final ObjectMapper mapper = new ObjectMapper();
                            mapper.writeValue(writer, new PlayerRequest(board, player.getColor()));
                            player.send(writer.toString());
                        } catch (IOException ignored) {
                        }
                        continue;
                    }
                    if (!handler.haveIStep(board, player.getColor())) {
                        continue;
                    }

                    while (true) {
                        try {
                            final StringWriter writer = new StringWriter();
                            final ObjectMapper mapper = new ObjectMapper();
                            mapper.writeValue(writer, new PlayerRequest(board, player.getColor()));
                            sendMessageToAllPlayers(writer.toString());

                            final String answer = player.in.readLine();
                            final StringReader reader = new StringReader(answer);
                            final Cell cell = mapper.readValue(reader, Cell.class);
                            sendMessageToAllPlayers(player.getColor() + " поставил фишку на клетку: " + cell.toString() + "\n");

                            handler.makeStep(board, cell, player.getColor());
                            break;
                        } catch (final ReversiException | IOException ignored) {
                        }
                    }
                }
                for (RandomBot rb : randomBots) {
                    if (!handler.haveIStep(board, rb.getPlayerColor())) {
                        continue;
                    }
                    while (true) {
                        try {
                            final Cell cell = rb.getAnswer(board);
                            handler.makeStep(board, cell, rb.getPlayerColor());
                            break;
                        } catch (ReversiException | IOException e) {
                            System.out.println("Бот работае некорректно");
                        }
                    }
                }
            }
        }

        private void gameEnd() {
            try {
                final StringWriter writerPlayerRequest = new StringWriter();
                final ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(writerPlayerRequest, new PlayerRequest(board, Color.NEUTRAL));
                sendMessageToAllPlayers(writerPlayerRequest.toString());
                sendMessageToAllPlayers("Игра закончилась");

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

                for (ServerSomething player : players) {
                    player.downService();
                }
                roomList.remove(this);
            } catch (final IOException ignored) {
            }
        }

        private void sendMessageToAllPlayers(final String message) throws IOException {
            for (ServerSomething ss : players) {
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
