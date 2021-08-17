package Kirill;

import Kirill.UtilityFunctions.IFunction;
import Kirill.UtilityFunctions.SimpleScoreFunction;
import exceptions.ReversiException;
import handler.Handler;
import models.board.Board;
import models.board.Cell;
import models.chip.Color;
import player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.ToDoubleFunction;

public final class MiniMaxBot extends Player {

    private IFunction utilityFunction = new SimpleScoreFunction();
    private final Handler handler = new Handler();
    private double alpha = Double.MIN_VALUE;
    private double beta = Double.MAX_VALUE;
    private static final int MAXRECLEVEL = 5;

    public MiniMaxBot(final Color color) {
        super(color);
        setName("KirillMiniMaxBot");
    }

    public MiniMaxBot(final Color color, final IFunction function) {
        super(color);
        setName("KirillMiniMaxBot "+function.toString());
        this.utilityFunction = function;
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

            final double win = getWinByGameTree(copyBoard, 0);
            awList.add(new AnswerAndWin(key, win));
        }
        return getGreedyDecision(awList, aw -> aw.win).cell;
    }

    private double getWinByGameTree(final Board board, final int recLevel) {

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

        if (recLevel == MAXRECLEVEL) {
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

            final double win = getWinByGameTree(copyBoard, recLevel + 1);
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
