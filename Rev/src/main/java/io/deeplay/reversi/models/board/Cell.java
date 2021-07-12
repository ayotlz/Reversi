package io.deeplay.reversi.models.board;

import java.util.Objects;

/**
 * Класс Cell - класс клетки
 */
public class Cell {
    /**
     * переменная координаты x
     */
    private final int x;
    /**
     * переменная координаты y
     */
    private final int y;

    /**
     * Конструктор - создание клетки по координатам
     *
     * @param x - координата x
     * @param y - координата y
     */
    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Функция получения координаты x
     *
     * @return возвращает координату x
     */
    public int getX() {
        return x;
    }

    /**
     * Функция получения координаты y
     *
     * @return возвращает координату y
     */

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return x == cell.x && y == cell.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
