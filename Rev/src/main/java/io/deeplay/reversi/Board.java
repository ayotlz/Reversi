package io.deeplay.reversi;

import io.deeplay.reversi.chip.Chip;
import io.deeplay.reversi.chip.Color;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final Chip board[][];
    private final int size = 8;

    public Board() {
        board = new Chip[size][size];

        for (Chip[] chips : board) {
            for (int j = 0; j < board.length; j++) {
                chips[j] = new Chip(Color.NEUTRAL);
            }
        }

        board[3][3] = new Chip(Color.WHITE);
        board[3][4] = new Chip(Color.BLACK);
        board[4][3] = new Chip(Color.BLACK);
        board[4][4] = new Chip(Color.WHITE);
    }

    public Chip[][] getBoard() {
        return board;
    }

    public void makeTurn(int x, int y) {

    }

    public Color getColor(int x, int y) {
        return board[x][y].getColor();
    }

//    private List[] findNeighborhood(int x, int y) {
//
//        return listOfNeighborhood;
//    }

    private List<Cell> findWhiteOrBlackChips() {
        ArrayList<Cell> listOfWhiteOrBlackChips = new ArrayList<>();
        Cell elem = new Cell(0, 0);
        final Color findColor = turnOrder.reverseColor();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (findColor == board[i][j].getColor()) {
                    elem.setX(i);
                    elem.setY(j);
                    listOfWhiteOrBlackChips.add(elem);
                }
            }
        }


        return listOfWhiteOrBlackChips;

    }
//
//    private int score(int x, int y) {
//
//    }
}
