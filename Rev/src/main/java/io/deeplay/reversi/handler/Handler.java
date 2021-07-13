package io.deeplay.reversi.handler;

import io.deeplay.reversi.validation.Validator;
import io.deeplay.reversi.models.board.Board;
import io.deeplay.reversi.models.board.Cell;
import io.deeplay.reversi.models.chip.Color;
import io.deeplay.reversi.exceptions.ReversiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Класс Handler отвечает за работу с доской и контролирует соблюдение правил
 */
public class Handler {
    /**
     * Поле логгера
     */
    private static final Logger logger = LoggerFactory.getLogger(Handler.class);

    /**
     * Функция инициализации доски начальными значениями
     *
     * @param board - доска
     * @throws ReversiException выбрасывает исключение при некорректно переданной доске
     */
    public void initializationBoard(Board board) throws ReversiException {
        Validator.isBoardCorrect(board);

        final int idx1 = board.getBoardSize() / 2 - 1;
        final int idx2 = board.getBoardSize() / 2;

        board.setColor(idx1, idx1, Color.WHITE);
        board.setColor(idx1, idx2, Color.BLACK);
        board.setColor(idx2, idx1, Color.BLACK);
        board.setColor(idx2, idx2, Color.WHITE);

        logger.debug("Доска проинициализирована");
    }

    /**
     * Функция, котороая отвечает за совершение ходов
     *
     * @param board     - доска
     * @param cell      - клетка
     * @param turnOrder - цвет, который совершает ход в данный момент
     * @throws ReversiException выбрасывает исключение при нарушении игровой логики
     */
    public void makeStep(Board board, Cell cell, Color turnOrder) throws ReversiException {
        logger.debug("Попытка поставить {} в клетку = ({}, {})", turnOrder.getString(), cell.getX(), cell.getY());

        checkStep(board, cell, turnOrder);
        setChips(board, cell, turnOrder);
    }

    /**
     * Функция проверки корректности совершения хода
     *
     * @param board     - доска
     * @param cell      - клетка
     * @param turnOrder - цвет, который совершает ход в данный момент
     * @throws ReversiException выбрасывает исключение при нарушении игровой логики
     */
    private void checkStep(Board board, Cell cell, Color turnOrder) throws ReversiException {
        Validator.isBoardCorrect(board);
        Validator.isCellCorrect(cell, board.getBoardSize());
        Validator.canIMakeStep(board, cell, turnOrder);
    }

    /**
     * Функция установки фишек в соответствии с правилами игры
     *
     * @param board     - доска
     * @param cell      - клетка
     * @param turnOrder - цвет, который совершает ход в данный момент
     */
    private void setChips(Board board, Cell cell, Color turnOrder) {
        final Map<Cell, List<Cell>> map = board.getScoreMap(turnOrder);
        board.setColor(cell.getX(), cell.getY(), turnOrder);
        flipCells(board, map.get(cell));
        logger.debug("{} поставлен в клетку = ({}, {})", turnOrder.getString(), cell.getX(), cell.getY());
    }

    /**
     * Функция проверки конца игры
     *
     * @param board - доска
     * @return возвращает boolean значение в зависимости от того, закончилась игра или нет
     */
    public boolean isGameEnd(Board board) {
        return isFullBoard(board) || isDeadEnd(board) || noOneCanStep(board);
    }

    /**
     * Функция проверки ситуации тупика, когда на поле нет фишек какого-либо цвета и игра не может быть продолжена
     *
     * @param board - доска
     * @return возвращает false если на поле ещё есть фишки обоих цветов, в противоположном случае возвращает true
     */
    private boolean isDeadEnd(Board board) {
        return (getScoreWhite(board) == 0 || getScoreBlack(board) == 0);
    }

    /**
     * Функция проверки заполненности доски
     *
     * @param board - доска
     * @return возвращает false если на доске ещё есть пустые клетки, в противоположном случае возвращает true
     */
    private boolean isFullBoard(Board board) {
        for (int i = 0; i < board.getBoardSize(); i++) {
            for (int j = 0; j < board.getBoardSize(); j++) {
                if (board.getColor(i, j) == Color.NEUTRAL) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Функция проверки тупика, когда на поле есть фишки обоих цветов, но расположены они так, что никто не может сделать ход
     *
     * @param board - доска
     * @return возвращает false если фишки хотя бы одного цвета ещё могут сделать ход, в противоположном случае true
     */
    private boolean noOneCanStep(Board board) {
        return !haveIStep(board, Color.BLACK) && !haveIStep(board, Color.WHITE);
    }

    /**
     * Функция проверки возможности походить в данный момент
     *
     * @param board     - доска
     * @param turnOrder - цвет, который совершает ход в данный момент
     * @return возвращает true если данный цвет может сделать ход, в противоположном случае false
     */
    public boolean haveIStep(Board board, Color turnOrder) {
        return board.getScoreMap(turnOrder).size() != 0;
    }

    /**
     * Функция переворота фишек (смены цвета на противоположный)
     *
     * @param board - доска
     * @param cells - клетка
     */
    private void flipCells(Board board, List<Cell> cells) {
        for (Cell cell : cells) {
            board.reverseChip(cell.getX(), cell.getY());
        }
    }

    /**
     * Функция получения количества белых фишек
     *
     * @param board - клетка
     * @return возвращает количество белых фишек на доске в данный момент
     */
    public int getScoreWhite(Board board) {
        int scoreWhite = 0;
        for (int i = 0; i < board.getBoardSize(); i++) {
            for (int j = 0; j < board.getBoardSize(); j++) {
                if (board.getColor(i, j) == Color.WHITE) {
                    scoreWhite++;
                }
            }
        }

        return scoreWhite;
    }

    /**
     * Функция получения количества чёрных фишек
     *
     * @param board - клетка
     * @return возвращает количество чёрных фишек на доске в данный момент
     */
    public int getScoreBlack(Board board) {
        int scoreBlack = 0;
        for (int i = 0; i < board.getBoardSize(); i++) {
            for (int j = 0; j < board.getBoardSize(); j++) {
                if (board.getColor(i, j) == Color.BLACK) {
                    scoreBlack++;
                }
            }
        }

        return scoreBlack;
    }
}
