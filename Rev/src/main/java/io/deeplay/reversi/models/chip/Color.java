package io.deeplay.reversi.models.chip;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum Color {
    @JsonProperty
    BLACK("Black"),
    @JsonProperty
    WHITE("White"),
    @JsonProperty
    NEUTRAL("Neutral");

    @JsonProperty
    private final String colorString;

    @JsonCreator
    Color(@JsonProperty("colorString") String colorString) {
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