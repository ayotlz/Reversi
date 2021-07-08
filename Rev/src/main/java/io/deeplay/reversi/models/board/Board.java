package io.deeplay.reversi.models.board;

import io.deeplay.reversi.models.chip.Chip;
import io.deeplay.reversi.models.chip.Color;
import io.deeplay.reversi.exceptions.ReversiErrorCode;
import io.deeplay.reversi.exceptions.ReversiException;

public class Board {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";



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
            b.append(ANSI_PURPLE + " ").append(i).append(" " + ANSI_RESET);
        }
        b.append("\n");

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j].getColor() == Color.BLACK) {
                    b.append(ANSI_RED + " B " + ANSI_RESET);
                }
                if (board[i][j].getColor() == Color.WHITE) {
                    b.append(ANSI_YELLOW + " W " + ANSI_RESET);
                }
                if (board[i][j].getColor() == Color.NEUTRAL) {
                    b.append(ANSI_CYAN + " . " + ANSI_RESET);
                }
            }
            b.append(ANSI_PURPLE + " ").append(i).append("\n" + ANSI_RESET);

        }
        return b.toString();
    }
}
