package Kirill.UtilityFunctions;

import models.board.Board;
import models.chip.Color;

public final class ExpertTableScoreFunction implements IFunction {
    @Override
    public final double getScore(final Board board, final Color color) {
        double score = 0;
        final double[][] expertBoard = getPriorityBoard(board);
        for (int i = 0; i < board.getBoardSize(); i++) {
            for (int j = 0; j < board.getBoardSize(); j++) {
                if (board.getChipColor(i, j) == color) {
                    score++;
                    score += expertBoard[i][j];
                }
            }
        }
        return score;
    }

    private double[][] getPriorityBoard(final Board board) {
        final double[][] priorityBoard = new double[board.getBoardSize()][board.getBoardSize()];
        for (int i = 0; i < board.getBoardSize(); i++) {
            for (int j = 0; j < board.getBoardSize(); j++) {
                priorityBoard[i][j] = 0;
            }
        }

        for (int i = 0; i < board.getBoardSize(); i++) {
            for (int j = 0; j < board.getBoardSize(); j++) {
                if ((i == 0) || (i == board.getBoardSize() - 1)) {
                    priorityBoard[i][j] += 5;
                }
                if ((j == 0) || (j == board.getBoardSize() - 1)) {
                    priorityBoard[i][j] += 5;
                }
            }
        }

        priorityBoard[0][0] = 100;
        priorityBoard[0][board.getBoardSize() - 1] = 100;
        priorityBoard[board.getBoardSize() - 1][0] = 100;
        priorityBoard[board.getBoardSize() - 1][board.getBoardSize() - 1] = 100;

        priorityBoard[1][0] = -2;
        priorityBoard[1][1] = -2;
        priorityBoard[1][board.getBoardSize() - 1] = -2;
        priorityBoard[1][board.getBoardSize() - 2] = -2;
        priorityBoard[0][1] = -2;
        priorityBoard[0][board.getBoardSize() - 1] = -2;

        priorityBoard[board.getBoardSize() - 2][0] = -2;
        priorityBoard[board.getBoardSize() - 2][1] = -2;
        priorityBoard[board.getBoardSize() - 1][1] = -2;
        priorityBoard[board.getBoardSize() - 2][board.getBoardSize() - 1] = -2;
        priorityBoard[board.getBoardSize() - 2][board.getBoardSize() - 2] = -2;
        priorityBoard[board.getBoardSize() - 1][board.getBoardSize() - 2] = -2;

        return priorityBoard;
    }

    @Override
    public final String toString() {
        return "ExpertTableScoreFunction";
    }
}
