package io.deeplay.reversi.models.chip;

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
