package Ayotlz.getstats;

import Ayotlz.utility.MonteCarlo;
import au.com.bytecode.opencsv.CSVReader;
import models.board.Board;
import models.chip.Chip;
import models.chip.Color;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class AddWinRate {
    private final static String fPathWinRate = "./Ayotlz/src/main/java/Ayotlz/getstats/winrate.csv";
    private final static String sPathWinRate = "resources/winrate.csv";

    private final static String fPathBoards = "./Ayotlz/src/main/java/Ayotlz/getstats/boards.csv";
    private final static String sPathBoards = "resources/boards.csv";

    static Board getBoard(final String[] cellStrings) {
        final Chip[][] chips = new Chip[8][8];
        int i = 0;
        int j = 0;
        for (final String cell : cellStrings) {
            if (j > 7 || i > 7) {
                return new Board(chips, Color.BLACK);
            }
            if (cell.equals("1")) {
                chips[i][j] = new Chip(Color.BLACK);
            } else if (cell.equals("-1")) {
                chips[i][j] = new Chip(Color.WHITE);
            } else {
                chips[i][j] = new Chip(Color.NEUTRAL);
            }
            if (j == 7) {
                i++;
                j = 0;
            } else {
                j++;
            }
        }
        return new Board(chips, Color.BLACK);
    }


    private static void writeHead() {
        PrintWriter pw;
        try {
            pw = new PrintWriter(new FileWriter(sPathWinRate, false), true);
        } catch (IOException e) {
            return;
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                pw.print(Integer.toString(i) + j);
                pw.print(';');
            }
        }
        pw.print("step");
        pw.print(";");
        pw.println("rate");
    }

    private static void writeCSV(String[] board, double score) {
        PrintWriter pw;
        try {
            pw = new PrintWriter(new FileWriter(sPathWinRate, true), true);
        } catch (IOException e) {
            return;
        }

        for (final String s : board) {
            if (!s.equals("")) {
                pw.print(s);
                pw.print(';');
            }
        }
        pw.println(score);
    }

    public static void main(String[] args) throws IOException {
        final CSVReader reader = new CSVReader(new FileReader(sPathBoards), ';', '"', 1);
        System.out.println("Reading csv...");
        final List<String[]> allRows = reader.readAll();
        System.out.println("csv file has bean read...");
        if (allRows.isEmpty()) {
            return;
        }

        int i = 0;
        writeHead();
        for (final String[] row : allRows) {
            if (i % 1000 == 0) {
                System.out.print(i);
                System.out.print(" / ");
                System.out.println(allRows.size());
            }
            final Board board = getBoard(row);
            final MonteCarlo mc = new MonteCarlo();

            final Color playerColor;
            if (row[64].equals("1")) {
                playerColor = Color.BLACK;
            } else {
                playerColor = Color.WHITE;
            }

            final double score = mc.getScore(board, Color.BLACK, playerColor.reverseColor());
            writeCSV(row, score);
            i++;
        }
    }
}
