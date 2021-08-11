package Kirill.UtilityFunctions;

import models.board.Board;
import models.chip.Color;

public class PetrozavodskStateUniversityExpertTableScoreFunction implements IFunction {
    @Override
    public double getScore(final Board board, final Color color) {
        double score = 0;
        final double[][] expertBoard = getPriorityBoard(board);
        for (int i = 0; i < board.getBoardSize(); i++) {
            for (int j = 0; j < board.getBoardSize(); j++) {
                if (board.getChipColor(i, j) == color) {
                    if (i == 0 || i == board.getBoardSize() - 1
                            || j == 0 || j == board.getBoardSize() - 1) {
                        score += 2;
                    } else {
                        score++;
                    }
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
                    priorityBoard[i][j] += 0.4;
                }
                if ((j == 0) || (j == board.getBoardSize() - 1)) {
                    priorityBoard[i][j] += 0.4;
                }
            }
        }
        return priorityBoard;
    }

    @Override
    public final String toString() {
        return "PetrozavodskStateUniversityExpertTableScoreFunction";
    }
}
