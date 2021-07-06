package io.deeplay.reversi;

import io.deeplay.reversi.chip.Chip;
import io.deeplay.reversi.chip.Color;
import io.deeplay.reversi.exceptions.ReversiErrorCode;
import io.deeplay.reversi.exceptions.ReversiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Handler {
    public void beforeGame(Board board) throws ReversiException {
        if (board.getArray().length % 2 == 1) {
            throw new ReversiException(ReversiErrorCode.ODD_SIZE_BOARD);
        }


        int idx1 = board.getArray().length / 2 - 1;
        int idx2 = board.getArray().length / 2;

        board.getArray()[idx1][idx1] = new Chip(Color.WHITE);
        board.getArray()[idx1][idx2] = new Chip(Color.BLACK);
        board.getArray()[idx2][idx1] = new Chip(Color.BLACK);
        board.getArray()[idx2][idx2] = new Chip(Color.WHITE);
    }

    public boolean makeStep(Board board, Color turnOrder, Cell cell) {
        // Находим все фишки противника
        List<Cell> chipsOfOpponent = findWhiteOrBlackChips(board, turnOrder);

        // Находим map(Cell *клетка цвета ходящего*: List<Cell>  *лист всех соседих пустых клеток*)
//        findNeighborhoods(chipsOfOpponent)

        // Получаем map(Cell *пустая клетка*: int *очки, которые мы получим за ход сюда*)
//        getScoreMap()

        // Бросаем исклющение если вдруг нельзя походить в данную клетку
//      throw new Exception

        // Ставим фишку, переворачиваем выигранные фишки, возвращаем true

        return true;
    }

    public List<Cell> findWhiteOrBlackChips(Board board, Color turnOrder) {
        ArrayList<Cell> listOfWhiteOrBlackChips = new ArrayList<>();
        final Color findColor = turnOrder.reverseColor();
        for (int i = 0; i < board.getArray().length; i++) {
            for (int j = 0; j < board.getArray().length; j++) {
                if (findColor == board.getArray()[i][j].getColor()) {
                    listOfWhiteOrBlackChips.add(new Cell(i, j));
                }
            }
        }

        return listOfWhiteOrBlackChips;
    }

    public Map<Cell, List<Cell>> findNeighborhood(List<Cell> listOfWhiteOrBlackChips, Board board) {
        Map<Cell, List<Cell>> neighborhood = new HashMap<>();
        for (Cell listOfWhiteOrBlackChip : listOfWhiteOrBlackChips) {
            List<Cell> tempList = new ArrayList<>();
            for (int j = -1; j < 2; j++) {
                for (int k = -1; k < 2; k++) {
                    if (j + listOfWhiteOrBlackChip.getX() >= 0 && j + listOfWhiteOrBlackChip.getX() < 8 &&
                            k + listOfWhiteOrBlackChip.getY() >= 0 && k + listOfWhiteOrBlackChip.getY() < 8) {
                        Chip chip = board.getArray()[j + listOfWhiteOrBlackChip.getX()][k + listOfWhiteOrBlackChip.getY()];
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

    public Map<Cell, Integer> getScoreMap(Board board, Map<Cell, List<Cell>> neighborhoods, Color turnOrder) {
        Map<Cell, Integer> scoreMap = new HashMap<>();
        for (Map.Entry<Cell, List<Cell>> entry : neighborhoods.entrySet()) {
            for (Cell cell : entry.getValue()) {
                scoreMap.put(cell, 0);
            }
        }

        for (Map.Entry<Cell, List<Cell>> entry : neighborhoods.entrySet()) {
            for (Cell cell : entry.getValue()) {
                Integer score = getScore(board, cell, entry.getKey(), turnOrder);
                scoreMap.put(cell, scoreMap.get(cell) + score);
            }
        }

        return scoreMap;
    }

    public Integer getScore(Board board, Cell neighbourCell, Cell mainCell, Color turnOrder) {
        int differenceX = mainCell.getX() - neighbourCell.getX();
        int differenceY = mainCell.getY() - neighbourCell.getY();

        Integer score = 0;

        int neighbourX = neighbourCell.getX();
        int neighbourY = neighbourCell.getY();

        //КОД ГОВНА ПЕРЕДЕЛАТЬ
        int schetchik = 0;
        while (schetchik < 10) {
            neighbourX += differenceX;
            neighbourY += differenceY;

            if (neighbourX > 7 || neighbourX < 0 || neighbourY > 7 || neighbourY < 0) {
                return 0;
            }
            if (board.getChip(neighbourX, neighbourY).getColor() == turnOrder.reverseColor()) {
                score += 1;
            }
            if (board.getChip(neighbourX, neighbourY).getColor() == Color.NEUTRAL) {
                return 0;
            }
            if (board.getChip(neighbourX, neighbourY).getColor() == turnOrder) {
                return score;
            }

            schetchik += 1;
        }
        return 0;
    }
}
