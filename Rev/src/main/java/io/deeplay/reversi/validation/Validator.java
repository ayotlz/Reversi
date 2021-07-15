package io.deeplay.reversi.validation;

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

/**
 * Класс Validator - класс валидации
 */
public class Validator {
    /**
     * Поле логгера
     */
    private static final Logger logger = LoggerFactory.getLogger(Handler.class);

    /**
     * Функция проверки клетки на корректность
     *
     * @param cell      - клетка
     * @param boardSize - размеры доски
     * @throws ReversiException выбрасиывает исключение, если клетка вылезает за границы доски или равна null
     */
    public static void isCellCorrect(final Cell cell, final int boardSize) throws ReversiException {
        if (cell == null) {
            throw new ReversiException(ReversiErrorCode.CELL_IS_NULL);
        }

        if (cell.getX() < 0 || cell.getX() >= boardSize || cell.getY() < 0 || cell.getY() >= boardSize) {
            throw new ReversiException(ReversiErrorCode.OUT_OF_BOARD);
        }
    }

    /**
     * Функция проверки доски на корректность
     *
     * @param board - доска
     * @throws ReversiException выбрасывает исключение, если размер доски - нечётное число или доска равна null
     */
    public static void isBoardCorrect(final Board board) throws ReversiException {
        if (board == null) {
            throw new ReversiException(ReversiErrorCode.BOARD_IS_NULL);
        }

        if (board.getBoardSize() % 2 == 1) {
            throw new ReversiException(ReversiErrorCode.ODD_SIZE_BOARD);
        }
    }

    /**
     * Функция проверки возможности шага
     *
     * @param board     - доска
     * @param cell      - клетка
     * @param turnOrder - цвет, который совершает ход в данный момент
     * @throws ReversiException выбрасывает исключение, если игрок не может совершить ход в данную клетку
     */
    public static void canIMakeStep(final Board board, final Cell cell, final Color turnOrder) throws ReversiException {
        final Map<Cell, List<Cell>> map = board.getScoreMap(turnOrder);

        if (map.get(cell) == null || map.get(cell).size() == 0) {
            logger.warn("{} не может быть установлен в клетку = ({}, {})", turnOrder.getString(), cell.getX(), cell.getY());
            throw new ReversiException(ReversiErrorCode.INCORRECT_PLACE_FOR_CHIP);
        }
    }
}
