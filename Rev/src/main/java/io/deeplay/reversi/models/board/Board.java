package io.deeplay.reversi.models.board;

import io.deeplay.reversi.models.chip.Chip;
import io.deeplay.reversi.models.chip.Color;
import io.deeplay.reversi.exceptions.ReversiErrorCode;
import io.deeplay.reversi.exceptions.ReversiException;

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

    public Color getColor(int x, int y) throws ReversiException {
        if (x < 0 || x > size || y < 0 || y > size) {
            throw new ReversiException(ReversiErrorCode.OUT_OF_BOARD);
        }
        return board[x][y].getColor();
    }

    public int getSize() {
        return size;
    }

    public Chip getChip(int x, int y) throws ReversiException {
        if (x < 0 || x > size || y < 0 || y > size) {
            throw new ReversiException(ReversiErrorCode.OUT_OF_BOARD);
        } 
        return board[x][y];
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j].getColor() == Color.BLACK) {
                    b.append("Ч");
                }
                if (board[i][j].getColor() == Color.WHITE) {
                    b.append("Б");
                }
                if (board[i][j].getColor() == Color.NEUTRAL) {
                    b.append(".");
                }
            }
            b.append("\n");
        }
        return b.toString();
    }
}
