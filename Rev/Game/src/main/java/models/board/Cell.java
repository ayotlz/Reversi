package models.board;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * Класс Cell - класс клетки
 */
public final class Cell {

    /**
     * переменная координаты x
     */
    @JsonProperty
    private final int x;

    /**
     * переменная координаты y
     */
    @JsonProperty
    private final int y;

    /**
     * Конструктор - создание клетки по координатам
     *
     * @param x - координата x
     * @param y - координата y
     */
    @JsonCreator
    public Cell(@JsonProperty("x") final int x, @JsonProperty("y") final int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Конструктор копирования клетки
     */
    public Cell(final Cell cell) {
        this.x = cell.getX();
        this.y = cell.getY();
    }

    /**
     * Функция получения координаты x
     *
     * @return возвращает координату x
     */
    public final int getX() {
        return x;
    }

    /**
     * Функция получения координаты y
     *
     * @return возвращает координату y
     */

    public final int getY() {
        return y;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Cell cell = (Cell) o;
        return x == cell.x && y == cell.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "{" + x + ", " + y + '}';
    }
}
