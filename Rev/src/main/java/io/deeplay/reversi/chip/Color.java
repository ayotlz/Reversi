package io.deeplay.reversi.chip;

public enum Color {
    BLACK, WHITE, NEUTRAL;

    public Color reverseColor(){
        if(this == BLACK) return WHITE;
        if(this == WHITE) return BLACK;
//        throw new IllegalAccessException();
        return NEUTRAL;
    }
}
