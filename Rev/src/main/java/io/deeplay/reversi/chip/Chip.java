package io.deeplay.reversi.chip;

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
        else {
            color = Color.BLACK;
        }
    }
}
