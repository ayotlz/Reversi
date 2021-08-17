package CSV;

import java.io.*;

public class WriteCSV {
    private PrintWriter pw;

    public WriteCSV() {
        String file = "log.csv";
        try {
            pw = new PrintWriter(new FileWriter(file, true), true);
            pw.println();
        } catch (final IOException ignored) {
        }
    }

    public void writeStep(final String name, final String scoreWhiteWins,
                          final String scoreBlackWins, final String wins, final String enemyName) {
        pw.print(name);
        pw.print(";");
        pw.print(scoreWhiteWins);
        pw.print(";");
        pw.print(scoreBlackWins);
        pw.print(";");
        pw.print(wins);
        pw.print(";");
        pw.println(enemyName);
    }
}
