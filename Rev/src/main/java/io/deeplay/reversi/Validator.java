package io.deeplay.reversi;

import io.deeplay.reversi.exceptions.ReversiErrorCode;
import io.deeplay.reversi.exceptions.ReversiException;
import io.deeplay.reversi.handler.Handler;
import io.deeplay.reversi.models.board.Board;
import io.deeplay.reversi.models.board.Cell;
import io.deeplay.reversi.models.chip.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class Validator {
    private static final Logger logger = LoggerFactory.getLogger(Handler.class);

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

    public static void canIMakeStep(Board board, Cell cell, Color turnOrder) throws ReversiException {
        final Map<Cell, List<Cell>> map = board.getScoreMap(turnOrder);

        if (map.get(cell) == null || map.get(cell).size() == 0) {
            logger.warn("{} не может быть установлен в клетку = ({}, {})", turnOrder.getString(), cell.getX(), cell.getY());
            throw new ReversiException(ReversiErrorCode.INCORRECT_PLACE_FOR_CHIP);
        }
    }
}
