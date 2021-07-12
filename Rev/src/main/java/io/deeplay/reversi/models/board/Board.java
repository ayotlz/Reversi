package io.deeplay.reversi.models.board;

import io.deeplay.reversi.Validator;
import io.deeplay.reversi.models.chip.Chip;
import io.deeplay.reversi.models.chip.Color;
import io.deeplay.reversi.exceptions.ReversiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Map<Cell, List<Cell>> getScoreMap(Color turnOrder) throws ReversiException {
        final List<Cell> chipsOfOpponent = findWhiteOrBlackChips(turnOrder);
        final Map<Cell, List<Cell>> mapNeighborhood = findNeighborhood(chipsOfOpponent);

        final Map<Cell, List<Cell>> scoreMap = new HashMap<>();
        for (Map.Entry<Cell, List<Cell>> entry : mapNeighborhood.entrySet()) {
            for (Cell cell : entry.getValue()) {
                scoreMap.put(cell, new ArrayList<>());
            }
        }

        for (Map.Entry<Cell, List<Cell>> entry : mapNeighborhood.entrySet()) {
            for (Cell cell : entry.getValue()) {
                scoreMap.get(cell).addAll(getListOfFlipCells(cell, entry.getKey(), turnOrder));
            }
        }

        final List<Cell> listToDelete = new ArrayList<>();
        for (Map.Entry<Cell, List<Cell>> entry : scoreMap.entrySet()) {
            if (scoreMap.get(entry.getKey()).size() == 0) {
                listToDelete.add(entry.getKey());
            }
        }

        for (Cell cell : listToDelete) {
            scoreMap.remove(cell);
        }

        return scoreMap;
    }


    private List<Cell> findWhiteOrBlackChips(Color turnOrder) {
        final ArrayList<Cell> listOfWhiteOrBlackChips = new ArrayList<>();
        final Color findColor = turnOrder.reverseColor();
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (findColor == board[i][j].getColor()) {
                    listOfWhiteOrBlackChips.add(new Cell(i, j));
                }
            }
        }

        return listOfWhiteOrBlackChips;
    }

    private Map<Cell, List<Cell>> findNeighborhood(List<Cell> listOfWhiteOrBlackChips) {
        final Map<Cell, List<Cell>> neighborhood = new HashMap<>();
        for (Cell listOfWhiteOrBlackChip : listOfWhiteOrBlackChips) {
            final List<Cell> tempList = new ArrayList<>();
            for (int j = -1; j < 2; j++) {
                for (int k = -1; k < 2; k++) {
                    if (j + listOfWhiteOrBlackChip.getX() >= 0 && j + listOfWhiteOrBlackChip.getX() < 8 &&
                            k + listOfWhiteOrBlackChip.getY() >= 0 && k + listOfWhiteOrBlackChip.getY() < 8) {
                        final Chip chip = board[j + listOfWhiteOrBlackChip.getX()][k + listOfWhiteOrBlackChip.getY()];
                        if (chip.getColor() == Color.NEUTRAL) {
                            final Cell tempCell = new Cell(j + listOfWhiteOrBlackChip.getX(), k + listOfWhiteOrBlackChip.getY());
                            tempList.add(tempCell);
                        }
                    }
                }
            }
            neighborhood.put(listOfWhiteOrBlackChip, tempList);
        }
        return neighborhood;
    }

    private List<Cell> getListOfFlipCells(Cell neighbourCell, Cell mainCell, Color turnOrder) throws ReversiException {
        Validator.isCellEquals(neighbourCell, mainCell);

        int differenceX = mainCell.getX() - neighbourCell.getX();
        int differenceY = mainCell.getY() - neighbourCell.getY();

        final List<Cell> cells = new ArrayList<>();

        int neighbourX = neighbourCell.getX();
        int neighbourY = neighbourCell.getY();

        while (true) {
            neighbourX += differenceX;
            neighbourY += differenceY;

            if (neighbourX > 7 || neighbourX < 0 || neighbourY > 7 || neighbourY < 0) {
                return new ArrayList<>();
            }
            if (getChip(neighbourX, neighbourY).getColor() == Color.NEUTRAL) {
                return new ArrayList<>();
            }
            if (getChip(neighbourX, neighbourY).getColor() == turnOrder.reverseColor()) {
                cells.add(new Cell(neighbourX, neighbourY));
            }
            if (getChip(neighbourX, neighbourY).getColor() == turnOrder) {
                return cells;
            }
        }
    }


    @Override
    public String toString() {
        final StringBuilder b = new StringBuilder();
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
