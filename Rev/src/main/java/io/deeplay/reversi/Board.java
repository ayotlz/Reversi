package io.deeplay.reversi;

import io.deeplay.reversi.chip.Chip;
import io.deeplay.reversi.chip.Color;

public class Board {
    private final Chip[][] board;
    private final int size = 8;

    public Board() {
        board = new Chip[size][size];

        for (Chip[] chips : board) {
            for (int j = 0; j < board.length; j++) {
                chips[j] = new Chip(Color.NEUTRAL);
            }
        }
    }

    public Chip[][] getArray() {
        return board;
    }

//  Добавить валидацию
    public Color getColor(int x, int y) {
        return board[x][y].getColor();
    }

    public int getSize() {
        return size;
    }
}
