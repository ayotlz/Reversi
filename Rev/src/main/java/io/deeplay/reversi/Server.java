package io.deeplay.reversi;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.deeplay.reversi.exceptions.ReversiException;
import io.deeplay.reversi.handler.Handler;
import io.deeplay.reversi.models.board.Board;
import io.deeplay.reversi.models.board.Cell;
import io.deeplay.reversi.models.chip.Color;
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
                serverList.add(this);
                color = chooseRoom().joinRoom(this);
                final StringWriter writer = new StringWriter();
                final ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(writer, color);
                send(writer.toString());
            } catch (final IOException e) {
                this.downService();
            }
        }

        private Room chooseRoom() {
            for (Room room : roomList) {
                if (room.isRoomHasPlace()) {
                    return room;
                }
            }
            Room newRoom = new Room();
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

    private class Room extends Thread {
        private final List<ServerSomething> players;
        private final Handler handler;
        private final Board board;

        private Room() {
            players = new ArrayList<>();
            handler = new Handler();
            board = new Board();
        }

        @Override
        public void run() {
            try {
                handler.initializationBoard(board);
            } catch (final ReversiException ignored) {
            }

            while (!handler.isGameEnd(board)) {
                for (ServerSomething player : players) {
                    if (!handler.haveIStep(board, player.getColor())) {
                        continue;
                    }

                    while (true) {
                        try {
                            final StringWriter writer = new StringWriter();
                            final ObjectMapper mapper = new ObjectMapper();
                            mapper.writeValue(writer, new PlayerRequest(board, player.getColor()));
                            for (ServerSomething ss : players) {
                                ss.send(writer.toString());
                            }

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
            }
            try {
                for (ServerSomething player : players) {
                    final StringWriter writer = new StringWriter();
                    final ObjectMapper mapper = new ObjectMapper();
                    mapper.writeValue(writer, new PlayerRequest(board, Color.NEUTRAL));
                    player.send(writer.toString());
                    player.send("Игра закончилась");
                    final int scoreBlack = handler.getScoreBlack(board);
                    final int scoreWhite = handler.getScoreWhite(board);
                    player.send("Черные: " + scoreBlack);
                    player.send("Белые: " + scoreWhite);
                    if (scoreBlack > scoreWhite) {
                        player.send("Чёрные победили");
                    } else if (scoreBlack < scoreWhite) {
                        player.send("Белые победили");
                    } else {
                        player.send("Ничья");
                    }
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

        private boolean isRoomHasPlace() {
            return players.size() < 2;
        }

        private Color joinRoom(final ServerSomething ss) {
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
            e.printStackTrace();
        }
    }

    public static void main(final String[] args) throws IOException {
        final Server server = new Server();
        server.startServer();
    }
}
