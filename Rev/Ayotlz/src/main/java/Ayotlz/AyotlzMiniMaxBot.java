package Ayotlz;

import Ayotlz.algorithms.MiniMax;
import Ayotlz.utility.MonteCarlo;
import models.board.Board;
import models.board.Cell;
import models.chip.Color;
import player.Player;

public final class AyotlzMiniMaxBot extends Player {
    private final int deep = 1;

    public AyotlzMiniMaxBot(final Color color) {
        super(color);
        setName("AyotlzMiniMaxBot");
    }

    @Override
    public final Cell getAnswer(final Board board) {
        final MiniMax mm = new MiniMax(new MonteCarlo(), deep);
        return mm.getCell(board, getPlayerColor());
    }
}
