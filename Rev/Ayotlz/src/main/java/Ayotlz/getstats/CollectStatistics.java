package Ayotlz.getstats;

import exceptions.ReversiException;
import handler.Handler;
import models.board.Board;
import models.board.Cell;
import models.chip.Color;
import player.Player;
import player.RandomBot;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CollectStatistics {
    public static void main(String[] args) {
        final int numberOfGames = 100000;
//        final int numberOfGames = 3000;

        final PrintWriter pw;
        try {
            pw = new PrintWriter(new FileWriter("./Ayotlz/src/main/java/Ayotlz/getstats/statistics.csv", false), true);
        } catch (final IOException e) {
            return;
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                pw.print(Integer.toString(i) + j);
                pw.print(';');
            }
        }
        pw.println("step");


        for (int i = 0; i < numberOfGames; i++) {
            if (i % 1000 == 0) {
                System.out.println(i);
            }
            game();
        }
    }

    private static void game() {
        final Handler handler = new Handler();
        final Board board = new Board();
        final Player[] players = new Player[]{new RandomBot(Color.BLACK), new RandomBot(Color.WHITE)};

        final PrintWriter pw;
        try {
            pw = new PrintWriter(new FileWriter("./Ayotlz/src/main/java/Ayotlz/getstats/statistics.csv", true), true);
        } catch (final IOException e) {
            return;
        }

        try {
            handler.initializationBoard(board);
        } catch (final ReversiException e) {
            System.out.println(e.getMessage());
        }

        while (!handler.isGameEnd(board)) {
            for (final Player player : players) {
                if (handler.isGameEnd(board)) {
                    break;
                }
                if (!handler.haveIStep(board, player.getPlayerColor())) {
                    continue;
                }
                while (true) {
                    try {
                        final Cell cell = player.getAnswer(board);
                        handler.makeStep(board, cell, player.getPlayerColor());
                        pw.println(getPrintWriteString(board, player.getPlayerColor()));
                        break;
                    } catch (final ReversiException | IOException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
    }

    private static String getPrintWriteString(final Board board, final Color turnOrder) {
        final StringBuilder str = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board.getChipColor(i, j) == Color.BLACK) {
                    str.append(1);
                } else if (board.getChipColor(i, j) == Color.WHITE) {
                    str.append(-1);
                } else {
                    str.append(0);
                }
                str.append(';');
            }
        }
        if (turnOrder == Color.BLACK) {
            str.append(1);
        } else {
            str.append(-1);
        }
        return str.toString();
    }
}
