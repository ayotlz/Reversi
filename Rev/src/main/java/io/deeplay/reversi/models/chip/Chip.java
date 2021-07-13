package io.deeplay.reversi.models.chip;

import java.util.Objects;

/**
 * Класс Chip - класс фишки
 */
public class Chip {
    /**
     * поле цвета фишки
     */
    private Color color;

    /**
     * Конструктор - создание фишки по её цвету
     *
     * @param color - цвет
     */
    public Chip(Color color) {
        this.color = color;
    }

    /**
     * Функция получения цвета
     *
     * @return возвращает цвет фишки
     */
    public Color getColor() {
        return color;
    }

    /**
     * Функция установки цвета (разрешены только чёрный и белый)
     *
     * @param color - цвет
     */
    public void setColor(Color color) {
        if (color == Color.BLACK) {
            setBlack();
        } else if (color == Color.WHITE) {
            setWhite();
        }
    }

    /**
     * Функция переворота фишки
     */
    public void reverseChip() {
        color = color.reverseColor();
    }

    /**
     * Функция установки чёрного цвета
     */
    private void setBlack() {
        this.color = Color.BLACK;
    }

    /**
     * Функция установки белого цвета
     */
    private void setWhite() {
        this.color = Color.WHITE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chip chip = (Chip) o;
        return color == chip.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color);
    }
}
