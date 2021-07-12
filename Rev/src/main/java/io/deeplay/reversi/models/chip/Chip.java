package io.deeplay.reversi.models.chip;

import java.util.Objects;

public class Chip {
    private Color color;

    public Chip(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        if (color == Color.BLACK) {
            setBlack();
        } else if (color == Color.WHITE) {
            setWhite();
        }
    }

    public void reverseChip() {
        color = color.reverseColor();
    }

    private void setBlack() {
        this.color = Color.BLACK;
    }

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
