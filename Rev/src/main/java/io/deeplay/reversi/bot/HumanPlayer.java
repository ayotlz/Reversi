package io.deeplay.reversi.bot;

import io.deeplay.reversi.models.board.Board;
import io.deeplay.reversi.models.board.Cell;
import io.deeplay.reversi.models.chip.Color;

import java.io.*;

public class HumanPlayer extends Player {
    public HumanPlayer(Color color) {
        super(color);
    }

    @Override
    public Cell getAnswer(Board board) {
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
