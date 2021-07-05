package io.deeplay.reversi;

import io.deeplay.reversi.chip.Chip;
import io.deeplay.reversi.chip.Color;

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

//    private int[][] findNeighborhood(int x, int y) {
//
//    }
//
//    private int[][] findBlackChips() {
//
//    }
//
//    private int[][] findWhiteChips() {
//
//    }
//
//    private int score(int x, int y) {
//
//    }
}
