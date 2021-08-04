package io.deeplay.reversi.player;

import io.deeplay.reversi.exceptions.ReversiException;
import io.deeplay.reversi.handler.Handler;
import io.deeplay.reversi.models.board.Board;
import io.deeplay.reversi.models.board.Cell;
import io.deeplay.reversi.models.chip.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.ToDoubleFunction;

public class KirillBot extends Player {

    private Color turnOrder = getPlayerColor();
    private final Handler handler = new Handler();
    private int activeRecLevel = -1;
    private static final int MAXRECLEVEL = 3;

    /**
     * Конструктор - создание игрока по цвету
     *
     * @param color - цвет игрока
     */
    public KirillBot(Color color) {
        super(color);
    }

    private static final class AnswerAndWin {
        final Cell cell;
        final double win;

        private AnswerAndWin(final Cell cell, final double win) {
            this.cell = cell;
            this.win = win;
        }

        @Override
        public String toString() {
            return cell.toString() + " " + win;
        }
    }

    @Override
    public Cell getAnswer(final Board board) {
        final Map<Cell, List<Cell>> scoreMap = board.getScoreMap(getPlayerColor());
        final List<AnswerAndWin> awList = new ArrayList<>();
        final List<Cell> cellsToAct = new ArrayList<>() {
        };
        cellsToAct.addAll(scoreMap.keySet());
        System.out.println(cellsToAct);

        for (final Cell key : cellsToAct) {
            final Board copyBoard = new Board(board);
            try {
                handler.makeStep(copyBoard, key, getPlayerColor());
            } catch (ReversiException ignored) {
            }
            System.out.println("Предположение " + key + " " + getPlayerColor());
            System.out.println(board);

            final double win = getWinByGameTree(copyBoard, 0);
            awList.add(new AnswerAndWin(key, win));
        }
        return getGreedyDecision(awList, aw -> aw.win).cell;
    }

    private double getWinByGameTree(final Board board, final int recLevel) {
        final Color curPlayer = board.getNextPlayer();
        System.out.println(curPlayer.reverseColor());
        System.out.println(recLevel);
        System.out.print(board);
        System.out.println();
        System.out.println();
        ToDoubleFunction<AnswerAndWin> winCalculator;
        if (curPlayer == getPlayerColor()) {
            winCalculator = aw -> aw.win;
        } else {
            winCalculator = aw -> -aw.win;
        }

        if (handler.isGameEnd(board)) {
            return computeWin(board);
        }

        if (recLevel == MAXRECLEVEL) {
            return getWinScore(board);
        }

        final Map<Cell, List<Cell>> scoreMap = board.getScoreMap(curPlayer);
        final List<AnswerAndWin> awList = new ArrayList<>();
        final List<Cell> cellsToAct = new ArrayList<>() {
        };
        cellsToAct.addAll(scoreMap.keySet());
        for (final Cell key : cellsToAct) {
            final Board copyBoard = new Board(board);
            try {
                handler.makeStep(copyBoard, key, curPlayer);
            } catch (ReversiException reversiException) {
                reversiException.printStackTrace();
            }

//            if (activeRecLevel != recLevel) {
//                if (handler.haveIStep(board, turnOrder.reverseColor())) {
//                    turnOrder = turnOrder.reverseColor();
//                }
//                activeRecLevel = recLevel;
//            }

            final double win = getWinByGameTree(copyBoard, recLevel + 1);
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

    private double getColorScore(final Board board, final Color color) {
        double score = 0;
        for (int i = 0; i < board.getBoardSize(); i++) {
            for (int j = 0; j < board.getBoardSize(); j++) {
                if (board.getChipColor(i, j) == color) {
                    score++;
                }
            }
        }
        return score;
    }

    private double getWinScore(final Board board) {
        final double resultScore = getColorScore(board, getPlayerColor()) -
                getColorScore(board, getPlayerColor().reverseColor());
        if (resultScore > 0) {
            return 1;
        } else if (resultScore < 0) {
            return -1;
        } else
            return 0;
    }

    private double computeWin(final Board board) {
        if (handler.getScoreBlack(board) == handler.getScoreWhite(board)) {
            return 0;
        } else if (getColorScore(board, getPlayerColor()) > getColorScore(board, getPlayerColor().reverseColor())) {
            return 1;
        } else {
            return -1;
        }
    }

}
