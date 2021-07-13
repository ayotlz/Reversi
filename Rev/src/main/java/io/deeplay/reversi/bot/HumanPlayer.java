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
        final InputStream inputStream = System.in;
        final Reader inputStreamReader = new InputStreamReader(inputStream);
        final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        Integer x = null, y = null;

        while (x == null) {
            try {
                System.out.print("Введите значение по вертикали: ");
                x = Integer.parseInt(bufferedReader.readLine());
            } catch (NumberFormatException e) {
                System.out.println("Вы ввели некорректное значение");
            } catch (IOException ignore) {
            }
        }

        while (y == null) {
            try {
                System.out.print("Введите значение по горизонтали: ");
                y = Integer.parseInt(bufferedReader.readLine());
            } catch (NumberFormatException e) {
                System.out.println("Вы ввели некорректное значение");
            } catch (IOException ignore) {
            }
        }

        return new Cell(x, y);
    }
}
