package io.deeplay.reversi.models.chip;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Objects;

/**
 * Класс Chip - класс фишки
 */
@JsonPropertyOrder
public class Chip {

    /**
     * поле цвета фишки
     */
    @JsonProperty
    private final Color color;

    /**
     * Конструктор - создание фишки по её цвету
     *
     * @param color - цвет
     */
    @JsonCreator
    public Chip(@JsonProperty("color") Color color) {
        this.color = color;
    }

    /**
     * Функция получения цвета
     *
     * @return возвращает цвет фишки
     */
    public final Color getColor() {
        return color;
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
