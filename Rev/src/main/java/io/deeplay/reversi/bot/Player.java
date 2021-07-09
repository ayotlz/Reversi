package io.deeplay.reversi.bot;

import io.deeplay.reversi.exceptions.ReversiException;
import io.deeplay.reversi.models.board.Board;
import io.deeplay.reversi.models.board.Cell;
import io.deeplay.reversi.models.chip.Color;

public abstract class Player {
    private final Color color;

    public Player(Color color) {
        this.color = color;
    }

    public Color getPlayerColor() {
        return color;
    }

    public abstract Cell getAnswer(Board board) throws ReversiException;
}
