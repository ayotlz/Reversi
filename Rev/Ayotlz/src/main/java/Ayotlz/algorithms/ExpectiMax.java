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
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.function.ToDoubleFunction;

public final class ExpectiMax implements IAlgorithms {
    private final int deep;
    private final Handler handler = new Handler();
    private final IFunction utilityFunction;

    public ExpectiMax(final IFunction utilityFunction, final int deep) {
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
            final ForkJoinPool pool = new ForkJoinPool(2);
            final double win = pool.invoke(new GameTree(copy, playerColor,
                    playerColor.reverseColor(), deep - 1));
            awList.add(new AnswerAndWin(entry.getKey(), win));
        }
        return getGreedyDecision(awList, aw -> aw.win).answer;
    }

    private final class GameTree extends RecursiveTask<Double> {
        private final Board board;
        private final Color playerColor;
        private final Color turnOrder;
        private final int deepOfTree;

        private GameTree(final Board board, final Color playerColor, final Color turnOrder, final int deepOfTree) {
            this.board = board;
            this.playerColor = playerColor;
            this.turnOrder = turnOrder;
            this.deepOfTree = deepOfTree;
        }

        @Override
        protected final Double compute() {
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
                return computeWin(board);
            }
            final Map<Cell, List<Cell>> scoreMap = board.getScoreMap(turnOrder);
            final List<AnswerAndWin> awList = new ArrayList<>();

            if (!handler.haveIStep(board, turnOrder)) {
                final Board copy = new Board(board);
                final GameTree subTask = new GameTree(copy, playerColor,
                        turnOrder.reverseColor(), deepOfTree - 1);
                final double win = subTask.fork().join();
                awList.add(new AnswerAndWin(null, win));
            }
            for (final Map.Entry<Cell, List<Cell>> entry : scoreMap.entrySet()) {
                final Board copy = new Board(board);
                try {
                    handler.makeStep(copy, entry.getKey(), turnOrder);
                } catch (final ReversiException ignored) {
                }
                final GameTree subTask = new GameTree(copy, playerColor,
                        turnOrder.reverseColor(), deepOfTree - 1);
                final double win = subTask.fork().join();
                awList.add(new AnswerAndWin(entry.getKey(), win));
                if (win >= 0.9 || win <= -0.9) {
                    break;
                }
                if (win >= -0.1 && win <= 0.1) {
                    break;
                }
            }
            return getGreedyDecision(awList, winCalculator).win;
        }

        private double computeWin(final Board board) {
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
    }

    private AnswerAndWin getGreedyDecision(final List<AnswerAndWin> awList,
                                           final ToDoubleFunction<AnswerAndWin> winCalculator) {
        AnswerAndWin bestAW = awList.get(0);
        double sumOfWin = awList.get(0).win;

        double bestWin = winCalculator.applyAsDouble(bestAW);
        for (int i = 1; i < awList.size(); i++) {
            final AnswerAndWin currentAW = awList.get(i);
            final double currentWin = winCalculator.applyAsDouble(currentAW);
            sumOfWin += currentWin;
            if (currentWin > bestWin) {
                bestAW = currentAW;
                bestWin = currentWin;
            }
        }
        bestAW.win = sumOfWin;
        return bestAW;
    }
}
