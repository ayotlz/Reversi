package io.deeplay.reversi.player;

import io.deeplay.reversi.models.board.Board;
import io.deeplay.reversi.models.board.Cell;
import io.deeplay.reversi.models.chip.Color;

import java.io.IOException;

/**
 * Класс Player - класс абстрактного игрока
 */
public abstract class Player {

    /**
     * поле цвета, которым ходит игрок
     */
    private final Color color;

    /**
     * Конструктор - создание игрока по цвету
     *
     * @param color - цвет игрока
     */
    public Player(final Color color) {
        this.color = color;
    }

    /**
     * Функция возврата цвета игрока
     *
     * @return возвращается цвет игрока
     */
    public final Color getPlayerColor() {
        return color;
    }

    /**
     * Функция, которая запращивает у игрока ответ
     *
     * @param board - доска
     * @return возвращается ответ от игрока
     */
    public abstract Cell getAnswer(final Board board) throws IOException;
}
