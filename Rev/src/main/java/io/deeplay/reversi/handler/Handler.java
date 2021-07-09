package io.deeplay.reversi.handler;

import io.deeplay.reversi.Validator;
import io.deeplay.reversi.models.board.Board;
import io.deeplay.reversi.models.board.Cell;
import io.deeplay.reversi.models.chip.Chip;
import io.deeplay.reversi.models.chip.Color;
import io.deeplay.reversi.exceptions.ReversiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class Handler {
    private static final Logger logger = LoggerFactory.getLogger(Handler.class);

    public void initializationBoard(Board board) throws ReversiException {
        Validator.isBoardCorrect(board);

        int idx1 = board.getBoardSize() / 2 - 1;
        int idx2 = board.getBoardSize() / 2;

        board.getBoard()[idx1][idx1] = new Chip(Color.WHITE);
        board.getBoard()[idx1][idx2] = new Chip(Color.BLACK);
        board.getBoard()[idx2][idx1] = new Chip(Color.BLACK);
        board.getBoard()[idx2][idx2] = new Chip(Color.WHITE);

        logger.debug("Доска проинициализирована");
    }

    public boolean isGameEnd(Board board) {
        return isFullBoard(board) || isDeadEnd(board);
    }

    private boolean isDeadEnd(Board board) {
        return (getScoreWhite(board) == 0 || getScoreBlack(board) == 0);
    }

    private boolean isFullBoard(Board board) {
        for (Chip[] row : board.getBoard()) {
            for (Chip chip : row) {
                if (chip.getColor() == Color.NEUTRAL) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean makeStep(Board board, Color turnOrder, Cell cell) throws ReversiException {
        logger.debug("Попытка поставить {} в клетку = ({}, {})", turnOrder.getString(), cell.getX(), cell.getY());

        Validator.isCellCorrect(cell, board.getBoardSize());

        Map<Cell, List<Cell>> map = board.getScoreMap(turnOrder);

        if (map.get(cell) != null && map.get(cell).size() != 0) {
            board.setChip(cell.getX(), cell.getY(), turnOrder);
            flipCells(board, map.get(cell));

            logger.debug("{} поставлен в клетку = ({}, {})", turnOrder.getString(), cell.getX(), cell.getY());
            return true;
        }

        logger.warn("{} не может быть установлен в клетку = ({}, {})", turnOrder.getString(), cell.getX(), cell.getY());
        return false;
    }

    private void flipCells(Board board, List<Cell> cells) throws ReversiException {
        for (Cell cell : cells) {
            Color reverse = board.getChip(cell.getX(), cell.getY()).getColor().reverseColor();
            board.setChip(cell.getX(), cell.getY(), reverse);
        }
    }

    public int getScoreWhite(Board board) {
        int scoreWhite = 0;
        for (Chip[] rows : board.getBoard()) {
            for (Chip chip : rows) {
                if (chip.getColor() == Color.WHITE) {
                    scoreWhite += 1;
                }
            }
        }
        return scoreWhite;
    }

    public int getScoreBlack(Board board) {
        int scoreBlack = 0;
        for (Chip[] rows : board.getBoard()) {
            for (Chip chip : rows) {
                if (chip.getColor() == Color.BLACK) {
                    scoreBlack += 1;
                }
            }
        }
        return scoreBlack;
    }
}
