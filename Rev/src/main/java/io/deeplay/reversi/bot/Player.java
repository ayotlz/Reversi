package io.deeplay.reversi.bot;

import io.deeplay.reversi.exceptions.ReversiException;
import io.deeplay.reversi.models.board.Board;
import io.deeplay.reversi.models.board.Cell;
import io.deeplay.reversi.models.chip.Color;

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
    public Player(Color color) {
        this.color = color;
    }

    /**
     * Функция возврата цвета игрока
     *
     * @return возвращается цвет игрока
     */
    public Color getPlayerColor() {
        return color;
    }

    /**
     * Функция, которая запращивает у игрока ответ
     *
     * @param board - доска
     * @return возвращается ответ от игрока
     * @throws ReversiException выбрасывает исключение при нарушении игровой логики
     */
    public abstract Cell getAnswer(Board board) throws ReversiException;
}
