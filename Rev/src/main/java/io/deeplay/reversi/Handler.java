package io.deeplay.reversi;

import io.deeplay.reversi.chip.Chip;
import io.deeplay.reversi.chip.Color;

public class Handler {
    public void beforeGame(Board board) {
        if (board.getBoard().length % 2 == 1) {
//            throw new
        }

        int idx1 = board.getBoard().length / 2 - 1;
        int idx2 = board.getBoard().length / 2;

        board.getBoard()[idx1][idx1] = new Chip(Color.WHITE);
        board.getBoard()[idx1][idx2] = new Chip(Color.BLACK);
        board.getBoard()[idx2][idx1] = new Chip(Color.BLACK);
        board.getBoard()[idx2][idx2] = new Chip(Color.WHITE);
    }

    public void makeStep(Board board, Color turnOrder, Cell cell) {

    }
}
