package io.deeplay.reversi.CSV;

import au.com.bytecode.opencsv.CSVReader;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class ParseCSV {
    private PrintWriter pw;

    public ParseCSV() {
        String file = "parsedLogs.csv";
        try {
            pw = new PrintWriter(new FileWriter(file, false), true);
        } catch (IOException ignored) {
        }
    }

    public void writeStep(final String botClass, final String scoreWhiteWins,
                          final String scoreBlackWins, final String wins) {
        pw.print(botClass);
        pw.print(";");
        pw.print(scoreWhiteWins);
        pw.print(";");
        pw.print(scoreBlackWins);
        pw.print(";");
        pw.println(wins);
    }

    private Map<String, Integer[]> getParseMap(List<String[]> allRows) {
        Map<String, Integer[]> mapCSV = new HashMap<>();
        for (final String[] allRow : allRows) {
            if (!"".equals(allRow[0])) {
                if (mapCSV.containsKey(allRow[0])) {
                    mapCSV.put(allRow[0], new Integer[]{
                            mapCSV.get(allRow[0])[0] + Integer.parseInt(allRow[1]),
                            mapCSV.get(allRow[0])[1] + Integer.parseInt(allRow[2]),
                            mapCSV.get(allRow[0])[2] + Integer.parseInt(allRow[3])});
                } else {
                    mapCSV.put(allRow[0], new Integer[]{Integer.parseInt(allRow[1]),
                            Integer.parseInt(allRow[2]), Integer.parseInt(allRow[3])});
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
        Map<String, Integer[]> mapCSV = parseCSV.getParseMap(allRows);

        Set<String> keySet = mapCSV.keySet();
        ArrayList<String> keyList = new ArrayList<>();
        for (int i = 0; i < keySet.size(); i++) {
            keyList.add(keySet.toArray()[i].toString());
        }

        for (int i = 0; i < mapCSV.size(); i++) {
            parseCSV.writeStep(keyList.get(i), mapCSV.get(keyList.get(i))[0].toString(),
                    mapCSV.get(keyList.get(i))[1].toString(), mapCSV.get(keyList.get(i))[2].toString());
        }
    }
}
