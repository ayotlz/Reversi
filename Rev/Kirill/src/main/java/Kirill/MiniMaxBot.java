package Kirill;

import exceptions.ReversiException;
import handler.Handler;
import models.board.Board;
import models.board.Cell;
import models.chip.Color;
import player.Player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.function.ToDoubleFunction;

public class MiniMaxBot extends Player {

    private final Handler handler = new Handler();
    private double alpha = Double.MIN_VALUE;
    private double beta = Double.MAX_VALUE;
    private static final int MAXRECLEVEL = 5;
    private int countRoots = 0;

    public MiniMaxBot(Color color) {
        super(color);
        setName("KirillMiniMaxBot");
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

        List<Cell> subTasks = new LinkedList<>();
        final ForkJoinPool pool = new ForkJoinPool(4);


        final Map<Cell, List<Cell>> scoreMap = board.getScoreMap(getPlayerColor());
        final List<AnswerAndWin> awList = new ArrayList<>();
        final List<Cell> cellsToAct = new ArrayList<>(scoreMap.keySet());


        for (final Cell key : cellsToAct) {
            final Board copyBoard = new Board(board);
            try {
                handler.makeStep(copyBoard, key, getPlayerColor());
            } catch (ReversiException ignored) {
            }
//            Cell task = new Cell(key);
//            task.fork(); // запустим асинхронно
//            subTasks.add(task);

            final double win = getWinByGameTree(copyBoard, 0);
            awList.add(new AnswerAndWin(key, win));
        }
        System.out.println(countRoots);
        return getGreedyDecision(awList, aw -> aw.win).cell;
    }

    private double getWinByGameTree(final Board board, final int recLevel) {
        countRoots++;

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
        final List<Cell> cellsToAct = new ArrayList<>() {
        };
        cellsToAct.addAll(scoreMap.keySet());

        for (final Cell key : cellsToAct) {
            final Board copyBoard = new Board(board);
            try {
                handler.makeStep(copyBoard, key, currentPlayer);
            } catch (ReversiException ignored) {
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

    private double getScoreFromPriorityBoardAndColor(final Board board, final Color color) {
        double score = 0;
        double[][] priorityBoard = getPriorityBoard(board);
        for (int i = 0; i < board.getBoardSize(); i++) {
            for (int j = 0; j < board.getBoardSize(); j++) {
                if (board.getChipColor(i, j) == color) {
                    score++;
                    score += priorityBoard[i][j];
                }
            }
        }
        return score;
    }

    private double[][] getPriorityBoard(final Board board) {
        final double[][] priorityBoard = new double[board.getBoardSize()][board.getBoardSize()];
        for (int i = 0; i < board.getBoardSize(); i++) {
            for (int j = 0; j < board.getBoardSize(); j++) {
                priorityBoard[i][j] = 1;
            }
        }

        for (int i = 0; i < board.getBoardSize(); i++) {
            for (int j = 0; j < board.getBoardSize(); j++) {
                if ((i == 0) || (i == board.getBoardSize() - 1)) {
                    priorityBoard[i][j] += 2;
                }
                if ((j == 0) || (j == board.getBoardSize() - 1)) {
                    priorityBoard[i][j] += 2;
                }
            }
        }

        for (int i = 0; i < board.getBoardSize() / 2; i++) {
            for (int j = 0; j < board.getBoardSize(); j++) {
                if (i % 2 == 1) {
                    priorityBoard[i][j] -= 1;
                }
            }
        }

        for (int i = board.getBoardSize() / 2; i < board.getBoardSize(); i++) {
            for (int j = 0; j < board.getBoardSize(); j++) {
                if (i % 2 == 0) {
                    priorityBoard[i][j] -= 1;
                }
            }
        }

        for (int i = 0; i < board.getBoardSize(); i++) {
            for (int j = 0; j < board.getBoardSize() / 2; j++) {
                if (j % 2 == 1) {
                    priorityBoard[i][j] -= 1;
                }
            }
        }

        for (int i = 0; i < board.getBoardSize(); i++) {
            for (int j = board.getBoardSize() / 2; j < board.getBoardSize(); j++) {
                if (j % 2 == 0) {
                    priorityBoard[i][j] -= 1;
                }
            }
        }
        return priorityBoard;
    }

    private double getWinScore(final Board board) {
        return getColorScore(board, getPlayerColor()) -
                getColorScore(board, getPlayerColor().reverseColor());
    }

    private double computeWin(final Board board) {
        return Double.compare(getColorScore(board, getPlayerColor()),
                getColorScore(board, getPlayerColor().reverseColor()));
    }

}
