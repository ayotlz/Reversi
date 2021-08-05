package io.deeplay.reversi;

import io.deeplay.reversi.exceptions.ReversiException;
import io.deeplay.reversi.handler.Handler;
import io.deeplay.reversi.models.board.Board;
import io.deeplay.reversi.models.board.Cell;
import io.deeplay.reversi.models.chip.Color;
import io.deeplay.reversi.player.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Main {
    public static void main(String[] args) {
        final int numberOfGames = 100;
        int wins = 0;
        int loss = 0;
        int draws = 0;

        final PrintWriter pw;
        try {
            pw = new PrintWriter(new FileWriter("time.csv", false), true);
        } catch (IOException e) {
            return;
        }

        pw.print("step;");
        pw.print("time;");
        pw.println("color");

        for (int i = 0; i < numberOfGames; i++) {
            System.out.println("Game №" + (i + 1));
            final int game = game();
            if (game == 1) {
                wins++;
            } else if (game == -1) {
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
        int chips = 4;
        final PrintWriter pw;
        try {
            pw = new PrintWriter(new FileWriter("time.csv", true), true);
        } catch (IOException e) {
            return 0;
        }

        try {
            handler.initializationBoard(board);
        } catch (ReversiException e) {
            System.out.println(e.getMessage());
        }

        while (!handler.isGameEnd(board)) {
            for (Player player : players) {
                if (handler.isGameEnd(board)) {
                    break;
                }
                if (!handler.haveIStep(board, player.getPlayerColor())) {
                    continue;
                }
                while (true) {
                    try {
                        double before = System.currentTimeMillis();
                        final Cell cell = player.getAnswer(board);
                        chips++;
                        double after = (System.currentTimeMillis() - before) / 1000;
                        pw.print(chips + ";");
                        pw.print(after + ";");
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
