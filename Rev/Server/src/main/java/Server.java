import Kirill.MiniMaxBot;
import Kirill.UtilityFunctions.ExpertTableScoreFunction;
import Kirill.UtilityFunctions.PetrozavodskStateUniversityExpertTableScoreFunction;
import com.fasterxml.jackson.databind.ObjectMapper;
import Ayotlz.AyotlzBot;
import CSV.WriteCSV;
import exceptions.ReversiException;
import handler.Handler;
import models.board.Board;
import models.board.Cell;
import models.chip.Color;
import player.Player;
import player.RandomBot;
import requests.GameEndRequest;
import requests.PlayerRequest;
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
    static final int PORT = Property.getPort();

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
        private String nickName;
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
                nickName = in.readLine();

                RoomType roomType = null;
                while (roomType == null) {
                    final String rt = in.readLine();
                    roomType = switch (rt) {
                        case "1" -> RoomType.HumanVsBot;
                        case "2" -> RoomType.HumanVsHuman;
                        case "3" -> RoomType.BotVsBot;
                        default -> null;
                    };
                }
                Color pColor = null;
                if (roomType == RoomType.BotVsBot) {
                    pColor = Color.NEUTRAL;
                } else {
                    while (pColor == null) {
                        final String message = in.readLine();
                        final StringReader reader = new StringReader(message);
                        final ObjectMapper mapper = new ObjectMapper();
                        pColor = mapper.readValue(reader, Color.class);
                    }
                }
                serverList.add(this);
                color = pColor;
                chooseRoom(roomType, pColor).joinRoom(this);
            } catch (final IOException e) {
                this.downService();
            }
        }

        private Room chooseRoom(final RoomType rt, final Color pColor) {
            for (final Room room : roomList) {
                if (room.getRoomType() == rt && room.isRoomHasPlace(pColor)) {
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
        private final RoomType roomType;

        private final int countOfGames = Property.getCountOfGames();
        private Board board;

        private Room(final RoomType roomType, final int roomID) {
            this.roomID = roomID;
            players = new ArrayList<>();
            handler = new Handler();
            this.roomType = roomType;
            bots = new ArrayList<>();
        }

        @Override
        public void run() {
            for (int i = 0; i < countOfGames; i++) {
                board = new Board();
                try {
                    handler.initializationBoard(board);
                } catch (final ReversiException ignored) {
                }
                gameProcess();
                gameEnd();
            }
            exit();
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
                if (!handler.haveIStep(board, playerColor) || playerColor == Color.NEUTRAL || playerColor != board.getNextPlayer()) {
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
                        sendMessageToAllPlayers("[" + playerColor + "] " + player.nickName + " ставит фишку на клетку: " + cell.toString() + "");

                        handler.makeStep(board, cell, playerColor);
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
                if (!handler.haveIStep(board, botColor) || botColor != board.getNextPlayer()) {
                    continue;
                }
                while (true) {
                    try {
                        final StringWriter writer = new StringWriter();
                        final ObjectMapper mapper = new ObjectMapper();
                        mapper.writeValue(writer, new PlayerRequest(board, botColor));
                        sendMessageToAllPlayers(writer.toString());

                        final Cell cell = bot.getAnswer(board);
                        handler.makeStep(board, cell, botColor);
                        sendMessageToAllPlayers("[" + botColor + "] " + bot.getName() + " ставит фишку на клетку: " + cell.toString() + "");
                        break;
                    } catch (ReversiException | IOException e) {
                        System.out.println("Бот работает некорректно");
                    }
                }
            }
        }


        private void formatCsv(final String name, final Color color, final int scoreBlack, final int scoreWhite) {
            int whiteWins = 0;
            int blackWins = 0;
            if (scoreBlack > scoreWhite) {
                if (color == Color.BLACK) {
                    blackWins = 1;
                }
            } else if (scoreBlack < scoreWhite) {
                if (color == Color.WHITE) {
                    whiteWins = 1;

                }
            }

            csvWriter.writeStep(name, Integer.toString(whiteWins),
                    Integer.toString(blackWins), Integer.toString(whiteWins + blackWins));
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

                for (final ServerSomething ss : serverList) {
                    formatCsv(ss.nickName, ss.getColor(), scoreBlack, scoreWhite);
                }
                for (final Player bot : bots) {
                    formatCsv(bot.getName(), bot.getPlayerColor(), scoreBlack, scoreWhite);
                }
            } catch (final IOException ignored) {
            }
        }

        private void exit() {
            for (final ServerSomething player : players) {
                try {
                    player.send("down service");
                } catch (IOException ignored) {
                }
                player.downService();
            }
            roomList.remove(this);
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

        private boolean isRoomHasPlace(Color pColor) {
            if (roomType == RoomType.HumanVsBot) {
                for (ServerSomething ss : players) {
                    if (ss.getColor() == pColor) {
                        return false;
                    }
                }
                for (Player player : bots) {
                    if (player.getPlayerColor() == pColor) {
                        return false;
                    }
                }
                return players.size() < 1;
            }
            if (roomType == RoomType.HumanVsHuman) {
                for (ServerSomething ss : players) {
                    if (ss.getColor() == pColor) {
                        return false;
                    }
                }
                for (Player player : bots) {
                    if (player.getPlayerColor() == pColor) {
                        return false;
                    }
                }
                return players.size() < 2;
            }
            return false;
        }

        private void joinRoom(final ServerSomething ss) {
            if (roomType == RoomType.BotVsBot) {
                players.add(ss);
                bots.add(new MiniMaxBot(Color.WHITE, new ExpertTableScoreFunction()));
                bots.add(new MiniMaxBot(Color.BLACK, new PetrozavodskStateUniversityExpertTableScoreFunction()));
                start();
            } else if (roomType == RoomType.HumanVsBot) {
                players.add(ss);
                bots.add(new RandomBot(ss.getColor().reverseColor()));
                start();
            } else {
                players.add(ss);
                if (players.size() == 2) {
                    start();
                }
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
