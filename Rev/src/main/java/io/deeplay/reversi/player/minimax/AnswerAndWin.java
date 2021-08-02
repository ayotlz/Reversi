package io.deeplay.reversi.player.minimax;

import io.deeplay.reversi.models.board.Cell;

public final class AnswerAndWin {
    public final Cell answer;
    public final double win;

    public AnswerAndWin(final Cell answer, final double win) {
        this.answer = answer;
        this.win = win;
    }
}
