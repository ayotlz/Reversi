package Ayotlz;

import Ayotlz.algorithms.ExpectiMax;
import Ayotlz.utility.MonteCarlo;
import Ayotlz.utility.SimpleUtilityFunction;
import models.board.Board;
import models.board.Cell;
import models.chip.Color;
import player.Player;

public final class AyotlzExpectiMaxBot extends Player {
    private final int deep = 1;

    public AyotlzExpectiMaxBot(final Color color) {
        super(color);
        setName("AyotlzExpectiMaxBot");
    }

    @Override
    public final Cell getAnswer(final Board board) {
        final ExpectiMax em = new ExpectiMax(new MonteCarlo(), deep);
        return em.getCell(board, getPlayerColor());
    }
}
