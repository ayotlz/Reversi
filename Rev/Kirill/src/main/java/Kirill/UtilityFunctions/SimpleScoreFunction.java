package Kirill.UtilityFunctions;

import models.board.Board;
import models.chip.Color;

public class SimpleScoreFunction implements IFunctions {
    @Override
    public double getScore(final Board board, final Color color) {
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
}
