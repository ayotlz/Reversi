package io.deeplay.reversi.bot;

import io.deeplay.reversi.models.board.Cell;

import java.io.*;

public class HumanBot extends Bot {
    public Cell getAnswer() {
        InputStream inputStream = System.in;
        Reader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        Integer x = null, y = null;

        while (x == null) {
            try {
                System.out.print("Введите значение по вертикали: ");
                x = Integer.parseInt(bufferedReader.readLine());
            } catch (IOException e) {
                System.out.println("Вы ввели некорректное значение");
            }
        }

        while (y == null) {
            try {
                System.out.print("Введите значение по горизонтали: ");
                y = Integer.parseInt(bufferedReader.readLine());
            } catch (IOException e) {
                System.out.println("Вы ввели некорректное значение");
            }
        }

        return new Cell(x, y);
    }
}
