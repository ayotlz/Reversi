package io.deeplay.reversi;

import io.deeplay.reversi.chip.Chip;
import io.deeplay.reversi.chip.Color;

import java.util.ArrayList;

public class Board {
    private Chip board[][];
    private Color turnOrder;


    public Board() {
        board = new Chip[8][8];

        for (Chip[] chips : board) {
            for (int j = 0; j < board.length; j++) {
                chips[j] = new Chip(Color.NEUTRAL);
            }
        }

        board[3][3] = new Chip(Color.WHITE);
        board[3][4] = new Chip(Color.BLACK);
        board[4][3] = new Chip(Color.BLACK);
        board[4][4] = new Chip(Color.WHITE);
        turnOrder = Color.BLACK;
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

    private ArrayList<Coordinate> findWhiteOrBlackChips() {
            ArrayList<Coordinate> listOfWhiteOrBlackChips = new ArrayList<>();
            Coordinate elem = new Coordinate(0, 0);
        if (turnOrder == Color.BLACK) {
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board.length; j++) {
                        if (Color.WHITE == board[i][j].getColor()) {
                            elem.setX(i);
                            elem.setY(j);
                            listOfWhiteOrBlackChips.add(elem);
                        }
                    }
            }
        }
        else {
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board.length; j++) {
                    if (Color.BLACK == board[i][j].getColor()) {
                        elem.setX(i);
                        elem.setY(j);
                        listOfWhiteOrBlackChips.add(elem);
                    }
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
