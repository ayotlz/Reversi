package io.deeplay.reversi.chip;

import java.util.Objects;

public class Chip {
    Color color;

    public Chip(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void changeColor() {
        if (color == Color.BLACK) {
            color = Color.WHITE;
        }
        else if (color == Color.WHITE) {
            color = Color.BLACK;
        }
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
