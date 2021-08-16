package Ayotlz;

import exceptions.ReversiException;
import handler.Handler;
import models.board.Board;
import models.board.Cell;
import models.chip.Color;
import player.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public final class Main {
    public static void main(final String[] args) {
        final int numberOfGames = 200;
        int wins = 0;
        int loss = 0;
        int draws = 0;

        final PrintWriter pw;
        try {
            pw = new PrintWriter(new FileWriter("time.csv", false), true);
        } catch (final IOException e) {
            return;
        }
        pw.print("step;");
        pw.print("time;");
        pw.println("color");

        for (int i = 0; i < numberOfGames; i++) {
            System.out.println("Game №" + (i + 1));
            final int game = game();
            if (game == 1) {
                System.out.println("win");
                wins++;
            } else if (game == -1) {
                System.out.println("loss");
                loss++;
            } else {
                draws++;
            }
        }
        System.out.println("\nПроведено " + numberOfGames + " игр");
        System.out.println("Результаты:");
        System.out.println("Wins: " + (double) wins / (double) numberOfGames * 100 + "%");
        System.out.println("Loss: " + (double) loss / (double) numberOfGames * 100 + "%");
        System.out.println("Draws: " + (double) draws / (double) numberOfGames * 100 + "%");
    }

    public static int game() {
        final Handler handler = new Handler();
        final Board board = new Board();
        final Player[] players = new Player[]{new AyotlzBot(Color.BLACK), new RandomBot(Color.WHITE)};
        int order = 4;

        final PrintWriter pw;
        try {
            pw = new PrintWriter(new FileWriter("time.csv", true), true);
        } catch (final IOException e) {
            return 0;
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
                        final double time_before = System.currentTimeMillis();
                        final Cell cell = player.getAnswer(board);
                        order++;
                        final double time_after = (System.currentTimeMillis() - time_before) / 1000;
                        pw.print(order + ";");
                        pw.print(time_after + ";");
                        pw.println(player.getPlayerColor().getString());
                        handler.makeStep(board, cell, player.getPlayerColor());
                        break;
                    } catch (final ReversiException | IOException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
        return Integer.compare(handler.getScoreBlack(board), handler.getScoreWhite(board));
    }
}
