package io.deeplay.reversi.models.board;

public enum BoardColor {
    RESET("\u001B[0m"),
    RED("\u001B[31m"),
    YELLOW("\u001B[33m"),
    PURPLE("\u001B[35m"),
    CYAN("\u001B[36m");

    private final String color;

    BoardColor(final String color) {
        this.color = color;
    }

    public final String getColor() {
        return color;
    }
}
