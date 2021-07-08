package io.deeplay.reversi;

import java.io.*;

public class Bot {
    public Cell getAnswer() throws IOException {
        InputStream inputStream = System.in;
        Reader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        int x = Integer.parseInt(bufferedReader.readLine());
        System.out.println("ok");
        int y = Integer.parseInt(bufferedReader.readLine());
        System.out.println("ok");
        return new Cell(x, y);
    }
}
