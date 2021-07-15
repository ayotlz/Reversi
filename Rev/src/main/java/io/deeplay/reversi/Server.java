package io.deeplay.reversi;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.deeplay.reversi.bot.Player;
import io.deeplay.reversi.exceptions.ReversiException;
import io.deeplay.reversi.handler.Handler;
import io.deeplay.reversi.models.board.Board;
import io.deeplay.reversi.models.board.Cell;
import io.deeplay.reversi.models.chip.Color;

import java.io.*;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Server {
    static final int PORT = 8082;

    private final ConcurrentLinkedQueue<ServerSomething> serverList = new ConcurrentLinkedQueue<>();
//    private final ConcurrentLinkedQueue<Room> roomList = new ConcurrentLinkedQueue<>();

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
                if (serverList.size() == 1) {
                    color = Color.BLACK;
                    final StringWriter writer = new StringWriter();
                    final ObjectMapper mapper = new ObjectMapper();
                    mapper.writeValue(writer, color);
                    send(writer.toString());
                }
                if (serverList.size() == 2) {
                    color = Color.WHITE;
                    final StringWriter writer = new StringWriter();
                    final ObjectMapper mapper = new ObjectMapper();
                    mapper.writeValue(writer, color);
                    send(writer.toString());

                    ServerSomething[] serverSomethings = new ServerSomething[2];
                    int idx = 0;
                    for (ServerSomething ss : serverList) {
                        serverSomethings[idx++] = ss;
                    }
                    Room game = new Room(serverSomethings);
                    game.run();
                } else if (serverList.size() > 2) {
                    this.downService();
                }
            } catch (final IOException e) {
                this.downService();
            }
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
        private final ServerSomething[] players;
        private final Handler handler;
        private final Board board;

        private Room(ServerSomething[] players) {
            this.players = players;
            handler = new Handler();
            board = new Board();
        }

        @Override
        public void run() {
            System.out.println("All the players have joined");
            try {
                handler.initializationBoard(board);
            } catch (final ReversiException ignored) {
            }

            while (!handler.isGameEnd(board)) {
                for (ServerSomething player : players) {
                    if (!handler.haveIStep(board, player.getColor())) {
                        System.out.println("Ход переходит");
                        continue;
                    }

                    while (true) {
                        System.out.println("Ходят " + player.getColor());

                        try {
                            final StringWriter writer = new StringWriter();
                            final ObjectMapper mapper = new ObjectMapper();
                            mapper.writeValue(writer, board);
                            player.send(writer.toString());

                            String answer = player.in.readLine();
                            final StringReader reader = new StringReader(answer);
                            final Cell cell = mapper.readValue(reader, Cell.class);

                            handler.makeStep(board, cell, player.getColor());
                            System.out.println(board.toString());
                            break;
                        } catch (final ReversiException | IOException e) {
                            System.out.println("Ход не может быть сделан\n");
                        }
                    }
                }
            }
            try {
                for (ServerSomething player : players) {
                    player.send("Игра закончилась");
                    player.send("Черные: " + handler.getScoreBlack(board));
                    player.send("Белые: " + handler.getScoreWhite(board));
                    player.downService();
                }
            } catch (IOException ignored) {
            }
            System.out.println("Игра закончилась");
            System.out.println("Черные: " + handler.getScoreBlack(board));
            System.out.println("Белые: " + handler.getScoreWhite(board));
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private void startServer() throws IOException {
        System.out.printf("Server started, port: %d%n", PORT);
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
