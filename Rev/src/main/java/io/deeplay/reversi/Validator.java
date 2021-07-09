package io.deeplay.reversi;

import io.deeplay.reversi.exceptions.ReversiErrorCode;
import io.deeplay.reversi.exceptions.ReversiException;
import io.deeplay.reversi.models.board.Board;
import io.deeplay.reversi.models.board.Cell;

public class Validator {
    public static void isCellCorrect(Cell cell, int boardSize) throws ReversiException {
        if (cell == null) {
            throw new ReversiException(ReversiErrorCode.CELL_IS_NULL);
        }

        if (cell.getX() < 0 || cell.getX() >= boardSize || cell.getY() < 0 || cell.getY() >= boardSize) {
            throw new ReversiException(ReversiErrorCode.OUT_OF_BOARD);
        }
    }

    public static void isBoardCorrect(Board board) throws ReversiException {
        if (board == null) {
            throw new ReversiException(ReversiErrorCode.BOARD_IS_NULL);
        }

        if (board.getBoardSize() % 2 == 1) {
            throw new ReversiException(ReversiErrorCode.ODD_SIZE_BOARD);
        }
    }

    public static void isCellEquals(Cell cell1, Cell cell2) throws ReversiException {
        if (cell1.equals(cell2)) {
            throw new ReversiException(ReversiErrorCode.CELLS_ARE_EQUALS);
        }
    }
}
