package Ayotlz;

import Ayotlz.algorithms.MiniMax;
import Ayotlz.utility.MonteCarlo;
import handler.Handler;
import models.board.Board;
import models.board.Cell;
import models.chip.Color;
import player.Player;

public final class AyotlzBot extends Player {
    private int deep = 1;

    public AyotlzBot(final Color color) {
        super(color);
        setName("AyotlzBot");
    }

    @Override
    public final Cell getAnswer(final Board board) {
        final Handler handler = new Handler();
        if (handler.getScoreBlack(board) + handler.getScoreWhite(board) > 45) {
            deep = 2;
        }

        if (handler.getScoreBlack(board) + handler.getScoreWhite(board) > 55) {
            deep = 10;
        }

        final MiniMax mm = new MiniMax(new MonteCarlo(), deep);
        return mm.getCell(board, getPlayerColor());
    }
}
