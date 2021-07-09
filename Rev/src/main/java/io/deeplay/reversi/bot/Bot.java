package io.deeplay.reversi.bot;

import io.deeplay.reversi.models.board.Cell;
import io.deeplay.reversi.models.chip.Color;

public abstract class Bot {
    private Color color;

    public abstract Cell getAnswer();
}
