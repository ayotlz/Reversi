package io.deeplay.reversi.bot;

import io.deeplay.reversi.models.board.Board;
import io.deeplay.reversi.models.board.Cell;
import io.deeplay.reversi.models.chip.Color;

import java.io.*;

/**
 * Класс HumanPlayer - класс игрока расширяюшего Player
 */
public class HumanPlayer extends Player {
    public HumanPlayer(final Color color) {
        super(color);
    }

    /**
     * Функция, которая запращивает у игрока ответ
     *
     * @param board - доска
     * @return возвращается ответ от игрока
     */
    @Override
    public final Cell getAnswer(final Board board) {
        final InputStream inputStream = System.in;
        final Reader inputStreamReader = new InputStreamReader(inputStream);
        final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        Integer x = null, y = null;

        while (x == null) {
            try {
//                тесты
                System.out.print("Введите значение по вертикали: ");
                x = Integer.parseInt(bufferedReader.readLine());
            } catch (final NumberFormatException e) {
                System.out.println("Вы ввели некорректное значение");
            } catch (final IOException ignore) {
                System.out.println(ignore.getMessage());
            }
        }

        while (y == null) {
            try {
                System.out.print("Введите значение по горизонтали: ");
                y = Integer.parseInt(bufferedReader.readLine());
            } catch (final NumberFormatException e) {
                System.out.println("Вы ввели некорректное значение");
            } catch (final IOException ignore) {
                System.out.println(ignore.getMessage());
            }
        }
        return new Cell(x, y);
    }
}
