package io.deeplay.reversi.models.chip;

public enum Color {
    BLACK("Black"), WHITE("White"), NEUTRAL("Neutral");

    private final String colorString;

    Color(String colorString) {
        this.colorString = colorString;
    }

    public Color reverseColor(){
        if(this == BLACK) return WHITE;
        if(this == WHITE) return BLACK;
        return NEUTRAL;
    }

    public String getString() {
        return colorString;
    }
}
