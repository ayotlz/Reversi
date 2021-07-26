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

    public void writeStep(String roomID, String color, String coordinates, String countOfFlip, String whiteScore, String blackScore) {
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
        pw.println(blackScore);
    }
}
