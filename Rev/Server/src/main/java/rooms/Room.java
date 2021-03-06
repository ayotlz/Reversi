package rooms;

import clients.AbstractPlayer;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.ReversiException;
import handler.Handler;
import models.board.Board;
import models.board.Cell;
import models.chip.Color;
import property.Property;
import requests.GameEndRequest;
import requests.PlayerRequest;
import CSV.WriteCSV;
import server.Server;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public final class Room extends Thread {
    private final Server server;
    private final RoomType roomType;
    private final int roomID;
    private final int countOfGames = Property.getCountOfGames();
    private final WriteCSV csvWriter = new WriteCSV();
    private final List<AbstractPlayer> players = new ArrayList<>();
    private final List<AbstractPlayer> observers = new ArrayList<>();
    private final Handler handler = new Handler();
    private Board board;

    public Room(final Server server, final RoomType roomType, final int roomID) {
        this.server = server;
        this.roomType = roomType;
        this.roomID = roomID;
    }

    @Override
    public final void run() {
        System.out.printf("В комнате %d начались игры%n", roomID);
        for (int i = 0; i < countOfGames; i++) {
            board = new Board();
            try {
                handler.initializationBoard(board);
            } catch (final ReversiException ignored) {
            }
            gameProcess();
            gameEnd();
        }
        server.closeRoom(this);
    }

    public final List<AbstractPlayer> getPlayers() {
        return players;
    }

    public final List<AbstractPlayer> getObservers() {
        return observers;
    }

    public final int getID() {
        return roomID;
    }

    private void gameProcess() {
        while (!handler.isGameEnd(board)) {
            for (final AbstractPlayer player : players) {
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

                        final String answer = player.readMessage();
                        final StringReader reader = new StringReader(answer);
                        final Cell cell = mapper.readValue(reader, Cell.class);
                        sendMessageToAllPlayers("[" + playerColor + "] " + player.getNickName() + " ставит фишку на клетку: " + cell.toString() + "");

                        handler.makeStep(board, cell, playerColor);
                        Thread.sleep((long)Property.getTime() * 1000);
                        break;
                    } catch (final ReversiException e) {
                        try {
                            sendMessageToAllPlayers("Некорректный ход\n");
                        } catch (final IOException ignored) {
                        }
                    } catch (final IOException ignored) {
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void formatCsv(final String name, final Color color, final int scoreBlack,
                           final int scoreWhite, final String enemyName) {
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
                Integer.toString(blackWins), Integer.toString(whiteWins + blackWins), enemyName);
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

            final String name = players.get(0).getNickName();
            final String enemyName = players.get(1).getNickName();

            if (players.get(0).getColor() != Color.NEUTRAL) {
                formatCsv(name, players.get(0).getColor(), scoreBlack, scoreWhite, enemyName);
            }

            if (players.get(1).getColor() != Color.NEUTRAL) {
                formatCsv(enemyName, players.get(1).getColor(), scoreBlack, scoreWhite, name);
            }

        } catch (final IOException ignored) {
        }
    }

    private void sendMessageToAllPlayers(final String message) throws IOException {
        for (final AbstractPlayer ap : players) {
            ap.send(message);
        }
        for (final AbstractPlayer player : observers) {
            player.send(message);
        }
    }

    public final RoomType getRoomType() {
        return roomType;
    }

    public final boolean isRoomHasPlace(final Color pColor) {
        if (roomType == RoomType.HumanVsBot) {
            for (final AbstractPlayer ap : players) {
                if (ap.getColor() == pColor) {
                    return false;
                }
            }
            return players.size() < 1;
        }
        if (roomType == RoomType.HumanVsHuman) {
            for (final AbstractPlayer ap : players) {
                if (ap.getColor() == pColor) {
                    return false;
                }
            }
            return players.size() < 2;
        }
        return false;
    }

    public final void joinRoom(final AbstractPlayer ap) {
        if (roomType == RoomType.BotVsBot && ap.getColor() == Color.NEUTRAL) {
            observers.add(ap);
            players.add(server.getBot(Property.getBot1(), Color.BLACK));
            players.add(server.getBot(Property.getBot2(), Color.WHITE));
            start();
        } else if (roomType == RoomType.HumanVsBot) {
            players.add(ap);
            players.add(server.getBot("RandomBot", ap.getColor().reverseColor()));
            start();
        } else if (roomType == RoomType.HumanVsHuman) {
            players.add(ap);
            if (players.size() >= 2) {
                start();
            }
        }
    }
}
