package Kirill.bots;

import Kirill.property.Property;
import Kirill.utilityFunctions.IFunction;
import Kirill.utilityFunctions.SimpleScoreFunction;
import exceptions.ReversiException;
import handler.Handler;
import models.board.Board;
import models.board.Cell;
import models.chip.Color;
import player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.function.ToDoubleFunction;

public final class MiniMaxBotFJP extends Player {

    private IFunction utilityFunction = new SimpleScoreFunction();
    private final Handler handler = new Handler();
    private double alpha = Double.MIN_VALUE;
    private double beta = Double.MAX_VALUE;
    private int maxRecLevel = Property.getMaxRecLevel();

    public MiniMaxBotFJP(final Color color) {
        super(color);
        setName("KirillMiniMaxBotFJP " + utilityFunction.toString() + " " + maxRecLevel);
    }

    public MiniMaxBotFJP(final Color color, final IFunction function) {
        super(color);
        this.utilityFunction = function;
        setName("KirillMiniMaxBotFJP " + utilityFunction.toString() + " " + maxRecLevel);
    }

    public MiniMaxBotFJP(final Color color, final IFunction function, final int maxRecLevel) {
        super(color);
        this.utilityFunction = function;
        this.maxRecLevel = maxRecLevel;
        setName("KirillMiniMaxBotFJP " + utilityFunction.toString() + " " + maxRecLevel);
    }

    private static final class AnswerAndWin {
        final Cell cell;
        final double win;

        private AnswerAndWin(final Cell cell, final double win) {
            this.cell = cell;
            this.win = win;
        }

        @Override
        public final String toString() {
            return cell.toString() + " " + win;
        }
    }

    private final class GameTree extends RecursiveTask<Double> {
        private final Board board;
        private final int maxRecLevel;

        private GameTree(final Board board, final int maxRecLevel) {
            this.board = board;
            this.maxRecLevel = maxRecLevel;
        }

        @Override
        protected Double compute() {
            final Color currentPlayer = board.getNextPlayer();

            ToDoubleFunction<AnswerAndWin> winCalculator;
            if (currentPlayer == getPlayerColor()) {
                winCalculator = (aw -> aw.win);
            } else {
                winCalculator = (aw -> -aw.win);
            }

            if (handler.isGameEnd(board)) {
                return computeWin(board);
            }

            if ((!handler.isGameEnd(board) && maxRecLevel < 1)) {
                return getWinScore(board);
            }


            final Map<Cell, List<Cell>> scoreMap = board.getScoreMap(currentPlayer);
            final List<AnswerAndWin> awList = new ArrayList<>();
            final List<Cell> cellsToAct = new ArrayList<>(scoreMap.keySet());

            for (final Cell key : cellsToAct) {
                final Board copyBoard = new Board(board);
                try {
                    handler.makeStep(copyBoard, key, currentPlayer);
                } catch (final ReversiException ignored) {
                }
                final GameTree subTask = new GameTree(copyBoard, maxRecLevel - 1);
                final double win = subTask.fork().join();
                if (currentPlayer == getPlayerColor()) {
                    if (win >= beta) {
                        return win;
                    }
                    alpha = Math.max(alpha, win);
                } else {
                    if (win <= alpha) {
                        return win;
                    }
                    beta = Math.min(beta, win);
                }
                awList.add(new AnswerAndWin(key, win));
            }
            return getGreedyDecision(awList, winCalculator).win;
        }
    }

    @Override
    public final Cell getAnswer(final Board board) {

        final Map<Cell, List<Cell>> scoreMap = board.getScoreMap(getPlayerColor());
        final List<AnswerAndWin> awList = new ArrayList<>();
        final List<Cell> cellsToAct = new ArrayList<>(scoreMap.keySet());

        for (final Cell key : cellsToAct) {
            final Board copyBoard = new Board(board);
            try {
                handler.makeStep(copyBoard, key, getPlayerColor());
            } catch (final ReversiException ignored) {
            }

            final ForkJoinPool pool = new ForkJoinPool(3);
            final double win = pool.invoke(new GameTree(copyBoard, maxRecLevel - 1));
            awList.add(new AnswerAndWin(key, win));
        }

        return getGreedyDecision(awList, aw -> aw.win).cell;
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

    private double getWinScore(final Board board) {
        return utilityFunction.getScore(board, getPlayerColor()) -
                utilityFunction.getScore(board, getPlayerColor().reverseColor());
    }

    private double computeWin(final Board board) {
        return Double.compare(utilityFunction.getScore(board, getPlayerColor()),
                utilityFunction.getScore(board, getPlayerColor().reverseColor()));
    }
}
