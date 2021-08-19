package Ayotlz;

import Ayotlz.algorithms.MiniMax;
import Ayotlz.utility.MonteCarlo;
import handler.Handler;
import models.board.Board;
import models.board.Cell;
import models.chip.Color;
import player.Player;
import property.Property;

public final class AyotlzBot extends Player {
    private int deep = Property.getDeep();

    public AyotlzBot(final Color color) {
        super(color);
        setName("AyotlzBot");
    }

    @Override
    public final Cell getAnswer(final Board board) {
        final Handler handler = new Handler();
        if (handler.getScoreBlack(board) + handler.getScoreWhite(board) > Property.getStartSecondDeep()) {
            deep = Property.getSecondDeep();
        }

        if (handler.getScoreBlack(board) + handler.getScoreWhite(board) > Property.getStartFullTree()) {
            deep = 64;
        }

        final MiniMax mm = new MiniMax(new MonteCarlo(), deep);
        return mm.getCell(board, getPlayerColor());
    }
}
