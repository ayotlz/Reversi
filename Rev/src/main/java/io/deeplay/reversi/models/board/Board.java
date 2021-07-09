package io.deeplay.reversi.models.board;

import io.deeplay.reversi.Validator;
import io.deeplay.reversi.models.chip.Chip;
import io.deeplay.reversi.models.chip.Color;
import io.deeplay.reversi.exceptions.ReversiException;

public class Board {
    private final Chip[][] board;
    private final int boardSize = 8;

    public Board() {
        board = new Chip[boardSize][boardSize];

        for (Chip[] chips : board) {
            for (int j = 0; j < board.length; j++) {
                chips[j] = new Chip(Color.NEUTRAL);
            }
        }
    }

    public Chip[][] getBoard() {
        return board;
    }

    public Color getColor(int x, int y) throws ReversiException {
        Validator.isCellCorrect(new Cell(x, y), boardSize);

        return board[x][y].getColor();
    }

    public int getBoardSize() {
        return boardSize;
    }

    public Chip getChip(int x, int y) throws ReversiException {
        Validator.isCellCorrect(new Cell(x, y), boardSize);
        return board[x][y];
    }

    public void setChip(int x, int y, Color color) throws ReversiException {
        Validator.isCellCorrect(new Cell(x, y), boardSize);
        board[x][y] = new Chip(color);
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < boardSize; i++) {
            b.append(BoardColor.PURPLE.getColor()).append(" ").append(i).append(" ").append(BoardColor.RESET.getColor());
        }
        b.append("\n");

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j].getColor() == Color.BLACK) {
                    b.append(BoardColor.RED.getColor()).append(" B ").append(BoardColor.RESET.getColor());
                }
                if (board[i][j].getColor() == Color.WHITE) {
                    b.append(BoardColor.YELLOW.getColor()).append(" W ").append(BoardColor.RESET.getColor());
                }
                if (board[i][j].getColor() == Color.NEUTRAL) {
                    b.append(BoardColor.CYAN.getColor()).append(" . ").append(BoardColor.RESET.getColor());
                }
            }
            b.append(BoardColor.PURPLE.getColor()).append(" ").append(i).append("\n").append(BoardColor.RESET.getColor());

        }
        return b.toString();
    }
}
