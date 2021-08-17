package Ayotlz.utility;

import handler.Handler;
import models.board.Board;
import models.chip.Color;

public final class SimpleUtilityFunction implements IFunction {
    @Override
    public final double getScore(final Board boar, final Color playerColor, final Color turnOrder) {
        final Handler handler = new Handler();
        if (turnOrder == playerColor) {
            return (double) handler.getScoreWhite(boar) /
                    (double) (handler.getScoreBlack(boar) + handler.getScoreWhite(boar));
        } else {
            return -(double) handler.getScoreBlack(boar) /
                    (double) (handler.getScoreBlack(boar) + handler.getScoreWhite(boar));
        }
    }
}
