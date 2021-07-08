package io.deeplay.reversi.models.chip;

public enum Color {
    BLACK, WHITE, NEUTRAL;

    public Color reverseColor(){
        if(this == BLACK) return WHITE;
        if(this == WHITE) return BLACK;
        return NEUTRAL;
    }
}
