package io.deeplay.reversi;

import io.deeplay.reversi.chip.Chip;
import io.deeplay.reversi.chip.Color;

import java.util.Arrays;

public class Board {
    private final Chip board[][];
    private final int size = 8;

    public Board() {
        board = new Chip[size][size];

        for (Chip[] chips : board) {
            for (int j = 0; j < board.length; j++) {
                chips[j] = new Chip(Color.NEUTRAL);
            }
        }
    }

    public void setBlack(int x, int y) {
        board[x][y].setColor(Color.BLACK);
    }

    public void setWhite(int x, int y) {
        board[x][y].setColor(Color.WHITE);
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

    public Chip getChip(int x, int y) {
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
