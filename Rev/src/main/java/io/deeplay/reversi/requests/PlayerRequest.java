package io.deeplay.reversi.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.deeplay.reversi.models.board.Board;
import io.deeplay.reversi.models.chip.Color;

public class PlayerRequest {

    @JsonProperty
    private final Board board;
    @JsonProperty
    private final Color color;

    @JsonCreator
    public PlayerRequest(@JsonProperty("board") final Board board, @JsonProperty("color") final Color color) {
        this.board = board;
        this.color = color;
    }

    public final Board getBoard() {
        return board;
    }

    public final Color getColor() {
        return color;
    }
}
