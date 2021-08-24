package Ayotlz.algorithms;

import Ayotlz.utility.IFunction;
import exceptions.ReversiException;
import handler.Handler;
import models.board.Board;
import models.board.Cell;
import models.chip.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.ToDoubleFunction;

public final class MiniMax implements IAlgorithms {
    private final int deep;
    private final Handler handler = new Handler();
    private final IFunction utilityFunction;

    public MiniMax(final IFunction utilityFunction, final int deep) {
        this.utilityFunction = utilityFunction;
        this.deep = deep;
    }

    @Override
    public final Cell getCell(final Board board, final Color playerColor) {
        final Map<Cell, List<Cell>> scoreMap = board.getScoreMap(playerColor);
        final List<AnswerAndWin> awList = new ArrayList<>();
        for (final Map.Entry<Cell, List<Cell>> entry : scoreMap.entrySet()) {
            final Board copy = new Board(board);
            try {
                handler.makeStep(copy, entry.getKey(), playerColor);
            } catch (final ReversiException ignored) {
            }
            final double win = getGameTree(copy, playerColor, playerColor.reverseColor(), deep - 1);
            awList.add(new AnswerAndWin(entry.getKey(), win));
        }
        return getGreedyDecision(awList, aw -> aw.win).answer;
    }


    protected final Double getGameTree(final Board board, final Color playerColor, final Color turnOrder, final int deepOfTree) {
        final ToDoubleFunction<AnswerAndWin> winCalculator;
        if (turnOrder == playerColor) {
            winCalculator = aw -> aw.win;
        } else {
            winCalculator = aw -> -aw.win;
        }

        final Handler handler = new Handler();
        if (!handler.isGameEnd(board) && deepOfTree < 1) {
            return utilityFunction.getScore(board, playerColor, turnOrder);
        }
        if (handler.isGameEnd(board)) {
            return computeWin(board, playerColor);
        }
        final Map<Cell, List<Cell>> scoreMap = board.getScoreMap(turnOrder);
        final List<AnswerAndWin> awList = new ArrayList<>();

        if (!handler.haveIStep(board, turnOrder)) {
            final Board copy = new Board(board);
            final double win = getGameTree(copy, playerColor, turnOrder.reverseColor(), deepOfTree - 1);
            awList.add(new AnswerAndWin(null, win));
        }
        for (final Map.Entry<Cell, List<Cell>> entry : scoreMap.entrySet()) {
            final Board copy = new Board(board);
            try {
                handler.makeStep(copy, entry.getKey(), turnOrder);
            } catch (final ReversiException ignored) {
            }
            final double win = getGameTree(copy, playerColor, turnOrder.reverseColor(), deepOfTree - 1);
            awList.add(new AnswerAndWin(entry.getKey(), win));
            if (win >= 0.95 || win <= -0.95) {
                break;
            }
            if (win >= -0.05 && win <= 0.05) {
                break;
            }
        }
        return getGreedyDecision(awList, winCalculator).win;
    }

    private double computeWin(final Board board, final Color playerColor) {
        final Handler handler = new Handler();
        if (handler.getScoreBlack(board) == handler.getScoreWhite(board)) {
            return 0;
        } else if (handler.getScoreBlack(board) > handler.getScoreWhite(board) && playerColor == Color.BLACK
                || handler.getScoreBlack(board) < handler.getScoreWhite(board) && playerColor == Color.WHITE) {
            return 1;
        } else {
            return -1;
        }
    }

    private AnswerAndWin getGreedyDecision(final List<AnswerAndWin> awList,
                                           final ToDoubleFunction<AnswerAndWin> winCalculator) {
        AnswerAndWin bestAW = awList.get(0);
        double bestWin = winCalculator.applyAsDouble(bestAW);
        for (int i = 1; i < awList.size(); i++) {
            final AnswerAndWin currentAW = awList.get(i);
            final double currentWin = winCalculator.applyAsDouble(currentAW);
            if (currentWin > bestWin) {
                bestAW = currentAW;
                bestWin = currentWin;
            }
        }
        return bestAW;
    }
}
