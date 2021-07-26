package io.deeplay.reversi;

import java.io.*;

public class WriteCSV {
    private PrintWriter pw;

    public WriteCSV() {
        String file = "log.csv";
        try {
            pw = new PrintWriter(new FileWriter(file, true), true);
            pw.println();
        } catch (IOException ignored) {
        }
    }

    public void writeStep(final String roomID, final String color, final String coordinates, final String countOfFlip,
                          final String whiteScore, final String blackScore, final String gameStatus) {
        pw.print(roomID);
        pw.print(";");
        pw.print(color);
        pw.print(";");
        pw.print(coordinates);
        pw.print(";");
        pw.print(countOfFlip);
        pw.print(";");
        pw.print(whiteScore);
        pw.print(";");
        pw.print(blackScore);
        pw.print(";");
        pw.println(gameStatus);
    }
}
