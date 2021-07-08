package io.deeplay.reversi.bot;

import io.deeplay.reversi.models.board.Cell;

import java.io.*;

public class Bot {
    public Cell getAnswer() throws IOException {
        InputStream inputStream = System.in;
        Reader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        System.out.print("Введите значение по вертикали: ");
        int x = Integer.parseInt(bufferedReader.readLine());
        System.out.print("Введите значение по горизонтали: ");
        int y = Integer.parseInt(bufferedReader.readLine());
        return new Cell(x, y);
    }
}
