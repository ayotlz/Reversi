package CSV;

import au.com.bytecode.opencsv.CSVReader;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public final class ParseCSV {
    private PrintWriter pw;

    public ParseCSV() {
        final String file = "parsedLogs.csv";
        try {
            pw = new PrintWriter(new FileWriter(file, false), true);
        } catch (final IOException ignored) {
        }
    }

    public void writeStep(final String botClass, final String scoreWhiteWins,
                          final String scoreBlackWins, final String wins, final String enemyBotClass) {
        pw.print(botClass);
        pw.print(";");
        pw.print(scoreWhiteWins);
        pw.print(";");
        pw.print(scoreBlackWins);
        pw.print(";");
        pw.print(wins);
        pw.print(";");
        pw.println(enemyBotClass);
    }

    private Map<String, String[]> getParseMap(final List<String[]> allRows) {
        Map<String, String[]> mapCSV = new HashMap<>();
        for (final String[] allRow : allRows) {
            if (!"".equals(allRow[0])) {
                if (mapCSV.containsKey(allRow[0] + allRow[4])) {
                    mapCSV.put(allRow[0] + allRow[4], new String[]{
                            allRow[0],
                            Integer.toString(Integer.parseInt(mapCSV.get(allRow[0] + allRow[4])[1])
                                    + Integer.parseInt(allRow[1])),
                            Integer.toString(Integer.parseInt(mapCSV.get(allRow[0] + allRow[4])[2])
                                    + Integer.parseInt(allRow[2])),
                            Integer.toString(Integer.parseInt(mapCSV.get(allRow[0] + allRow[4])[3])
                                    + Integer.parseInt(allRow[3])),
                            allRow[4]});
                } else {
                    mapCSV.put(allRow[0] + allRow[4], new String[]{allRow[0], allRow[1],
                            allRow[2], allRow[3], allRow[4]});
                }
            }
        }
        return mapCSV;
    }

    public static void main(final String[] args) throws Exception {
        ParseCSV parseCSV = new ParseCSV();
        CSVReader reader = new CSVReader(new FileReader("log.csv"), ';', '"', 1);
        List<String[]> allRows = reader.readAll();
        if (allRows.isEmpty()) {
            return;
        }
        final Map<String, String[]> mapCSV = parseCSV.getParseMap(allRows);

        final Set<String> keySet = mapCSV.keySet();
        ArrayList<String> keyList = new ArrayList<>();
        for (int i = 0; i < keySet.size(); i++) {
            keyList.add(keySet.toArray()[i].toString());
        }

        for (int i = 0; i < mapCSV.size(); i++) {
            parseCSV.writeStep(mapCSV.get(keyList.get(i))[0],
                    mapCSV.get(keyList.get(i))[1], mapCSV.get(keyList.get(i))[2],
                    mapCSV.get(keyList.get(i))[3], mapCSV.get(keyList.get(i))[4]);
        }
    }
}
