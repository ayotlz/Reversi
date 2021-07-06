package io.deeplay.reversi;

import io.deeplay.reversi.chip.Chip;
import io.deeplay.reversi.chip.Color;

import java.util.ArrayList;
import java.util.List;

public class Handler {
    public void beforeGame(Board board) {
        if (board.getArray().length % 2 == 1) {
//            throw new
        }

        int idx1 = board.getArray().length / 2 - 1;
        int idx2 = board.getArray().length / 2;

        board.getArray()[idx1][idx1] = new Chip(Color.WHITE);
        board.getArray()[idx1][idx2] = new Chip(Color.BLACK);
        board.getArray()[idx2][idx1] = new Chip(Color.BLACK);
        board.getArray()[idx2][idx2] = new Chip(Color.WHITE);
    }

    public void makeStep(Board board, Color turnOrder, Cell cell) {

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

}
