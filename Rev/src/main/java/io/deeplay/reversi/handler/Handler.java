package io.deeplay.reversi.handler;

import io.deeplay.reversi.models.board.Board;
import io.deeplay.reversi.models.board.Cell;
import io.deeplay.reversi.models.chip.Chip;
import io.deeplay.reversi.models.chip.Color;
import io.deeplay.reversi.exceptions.ReversiErrorCode;
import io.deeplay.reversi.exceptions.ReversiException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Handler {
    private static final Logger logger = LoggerFactory.getLogger(Handler.class);

    public void initializationBoard(Board board) throws ReversiException {
        if (board.getBoardSize() % 2 == 1) {
            throw new ReversiException(ReversiErrorCode.ODD_SIZE_BOARD);
        }

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

        cell.validation(0, board.getBoardSize() - 1);

        List<Cell> chipsOfOpponent = findWhiteOrBlackChips(board, turnOrder);
        Map<Cell, List<Cell>> mapNeighborhood = findNeighborhood(board, chipsOfOpponent);
        Map<Cell, List<Cell>> map = getScoreMap(board, mapNeighborhood, turnOrder);

        if (map.get(cell) != null && map.get(cell).size() != 0) {
            board.setChip(cell.getX(), cell.getY(), turnOrder);
            flipCells(board, map.get(cell));

            logger.debug("{} поставлен в клетку = ({}, {})", turnOrder.getString(), cell.getX(), cell.getY());
            return true;
        }

        logger.warn("{} не может быть установлен в клетку = ({}, {})", turnOrder.getString(), cell.getX(), cell.getY());
        return false;
    }

    private List<Cell> findWhiteOrBlackChips(Board board, Color turnOrder) {
        ArrayList<Cell> listOfWhiteOrBlackChips = new ArrayList<>();
        final Color findColor = turnOrder.reverseColor();
        for (int i = 0; i < board.getBoardSize(); i++) {
            for (int j = 0; j < board.getBoardSize(); j++) {
                if (findColor == board.getBoard()[i][j].getColor()) {
                    listOfWhiteOrBlackChips.add(new Cell(i, j));
                }
            }
        }

        return listOfWhiteOrBlackChips;
    }

    private Map<Cell, List<Cell>> findNeighborhood(Board board, List<Cell> listOfWhiteOrBlackChips) {
        Map<Cell, List<Cell>> neighborhood = new HashMap<>();
        for (Cell listOfWhiteOrBlackChip : listOfWhiteOrBlackChips) {
            List<Cell> tempList = new ArrayList<>();
            for (int j = -1; j < 2; j++) {
                for (int k = -1; k < 2; k++) {
                    if (j + listOfWhiteOrBlackChip.getX() >= 0 && j + listOfWhiteOrBlackChip.getX() < 8 &&
                            k + listOfWhiteOrBlackChip.getY() >= 0 && k + listOfWhiteOrBlackChip.getY() < 8) {
                        Chip chip = board.getBoard()[j + listOfWhiteOrBlackChip.getX()][k + listOfWhiteOrBlackChip.getY()];
                        if (chip.getColor() == Color.NEUTRAL) {
                            Cell tempCell = new Cell(j + listOfWhiteOrBlackChip.getX(), k + listOfWhiteOrBlackChip.getY());
                            tempList.add(tempCell);
                        }
                    }
                }
            }
            neighborhood.put(listOfWhiteOrBlackChip, tempList);
        }
        return neighborhood;
    }

    private Map<Cell, List<Cell>> getScoreMap(Board board, Map<Cell, List<Cell>> neighborhoods, Color turnOrder) throws ReversiException {
        Map<Cell, List<Cell>> scoreMap = new HashMap<>();
        for (Map.Entry<Cell, List<Cell>> entry : neighborhoods.entrySet()) {
            for (Cell cell : entry.getValue()) {
                scoreMap.put(cell, new ArrayList<>());
            }
        }

        for (Map.Entry<Cell, List<Cell>> entry : neighborhoods.entrySet()) {
            for (Cell cell : entry.getValue()) {
                scoreMap.get(cell).addAll(getListOfFlipCells(board, cell, entry.getKey(), turnOrder));
            }
        }

        return scoreMap;
    }

    private List<Cell> getListOfFlipCells(Board board, Cell neighbourCell, Cell mainCell, Color turnOrder) throws ReversiException {
        if (neighbourCell.equals(mainCell)) {
            throw new ReversiException(ReversiErrorCode.CELLS_ARE_EQUALS);
        }

        int differenceX = mainCell.getX() - neighbourCell.getX();
        int differenceY = mainCell.getY() - neighbourCell.getY();

        List<Cell> cells = new ArrayList<>();

        int neighbourX = neighbourCell.getX();
        int neighbourY = neighbourCell.getY();

        while (true) {
            neighbourX += differenceX;
            neighbourY += differenceY;

            if (neighbourX > 7 || neighbourX < 0 || neighbourY > 7 || neighbourY < 0) {
                return new ArrayList<>();
            }
            if (board.getChip(neighbourX, neighbourY).getColor() == Color.NEUTRAL) {
                return new ArrayList<>();
            }
            if (board.getChip(neighbourX, neighbourY).getColor() == turnOrder.reverseColor()) {
                cells.add(new Cell(neighbourX, neighbourY));
            }
            if (board.getChip(neighbourX, neighbourY).getColor() == turnOrder) {
                return cells;
            }
        }
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
