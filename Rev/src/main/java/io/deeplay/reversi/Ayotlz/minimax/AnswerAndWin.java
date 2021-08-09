package io.deeplay.reversi.Ayotlz.minimax;

import io.deeplay.reversi.models.board.Cell;

public final class AnswerAndWin {
    public final Cell answer;
    public double win;

    public AnswerAndWin(final Cell answer, final double win) {
        this.answer = answer;
        this.win = win;
    }
}
