package io.deeplay.reversi;

import io.deeplay.reversi.exceptions.ReversiErrorCode;
import io.deeplay.reversi.exceptions.ReversiException;
import io.deeplay.reversi.models.board.Cell;

public class Validator {
    public static void isCorrectCell(Cell cell, int boardSize) throws ReversiException {
        if (cell.getX() < 0 || cell.getX() >= boardSize || cell.getY() < 0 || cell.getY() >= boardSize) {
            throw new ReversiException(ReversiErrorCode.OUT_OF_BOARD);
        }
    }
}
