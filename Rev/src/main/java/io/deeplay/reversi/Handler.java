package io.deeplay.reversi;

import io.deeplay.reversi.chip.Chip;
import io.deeplay.reversi.chip.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private Map<Cell, List> findNeighborhood(List<Cell> listOfWhiteOrBlackChips, Board board) {
        Map<Cell, List> neighborhood = new HashMap<>();
        for (int i = 0; i < listOfWhiteOrBlackChips.size(); i++) {
            List<Cell> tempList = new ArrayList<>();
            for (int j = -1; j < 2; j++) {
                for (int k = -1; k < 2; k++) {
                    Chip chip = board.getArray()[j+listOfWhiteOrBlackChips.get(i).getX()][k+listOfWhiteOrBlackChips.get(i).getY()];
                        if (chip.getColor() == Color.NEUTRAL){
                            Cell tempCell = new Cell(j+listOfWhiteOrBlackChips.get(i).getX(),k+listOfWhiteOrBlackChips.get(i).getY());
                            tempList.add(tempCell);
                        }
                    }
                }
            neighborhood.put(listOfWhiteOrBlackChips.get(i),tempList);
            }
        return neighborhood;
    }
}

//
//    private int score(int x, int y) {
//
//    }
