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
    public void initializingBoard(Board board) throws ReversiException {
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

    private List<Cell> findWhiteOrBlackChips(Board board, Color turnOrder) {
        ArrayList<Cell> listOfWhiteOrBlackChips = new ArrayList<>();
        Cell elem = new Cell(0, 0);
        final Color findColor = turnOrder.reverseColor();
        for (int i = 0; i < board.getArray().length; i++) {
            for (int j = 0; j < board.getArray().length; j++) {
                if (findColor == board.getArray()[i][j].getColor()) {
                    elem.setX(i);
                    elem.setY(j);
                    listOfWhiteOrBlackChips.add(elem);
                }
            }
        }

        return listOfWhiteOrBlackChips;
    }

    //    private List[] findNeighborhood(int x, int y) {
//
//        return listOfNeighborhood;
//    }

//
//    private int score(int x, int y) {
//
//    }


    private Map<Cell, Integer> getScoreMap(Board board, Map<Cell, List<Cell>> neighborhoods) {
        Map<Cell, Integer> score = new HashMap<>();
        for (Map.Entry<Cell, List<Cell>> entry : neighborhoods.entrySet()) {
            for (Cell cell : entry.getValue()) {
                score.put(cell, 0);
            }
        }



        return null;
    }
}
