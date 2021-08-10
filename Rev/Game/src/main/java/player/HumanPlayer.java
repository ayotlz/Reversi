package player;

import exceptions.ReversiException;
import models.board.Board;
import models.board.Cell;
import models.chip.Color;
import validation.Validator;

import java.io.*;

/**
 * Класс HumanPlayer - класс игрока расширяюшего Player
 */
public class HumanPlayer extends Player {
    public HumanPlayer(final Color color) {
        super(color);
        setName("HumanPlayer");
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

        int x, y;

        while (true) {
            try {
                while (true) {
                    try {
                        System.out.print("Введите значение по вертикали: ");
                        x = Integer.parseInt(bufferedReader.readLine());
                        break;
                    } catch (final NumberFormatException e) {
                        System.out.println("Вы ввели некорректное значение");
                    } catch (final IOException ignore) {
                    }
                }

                while (true) {
                    try {
                        System.out.print("Введите значение по горизонтали: ");
                        y = Integer.parseInt(bufferedReader.readLine());
                        break;
                    } catch (final NumberFormatException e) {
                        System.out.println("Вы ввели некорректное значение");
                    } catch (final IOException ignore) {
                    }
                }
                final Cell cell = new Cell(x, y);
                Validator.isCellCorrect(cell, board.getBoardSize());
                return cell;
            } catch (final ReversiException e) {
                System.out.println("Такой клетки на поле не существует\n");
            }
        }
    }
}
