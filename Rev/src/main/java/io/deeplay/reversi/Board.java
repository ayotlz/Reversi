package io.deeplay.reversi;

public class Board {
    private int board[][];
    private int turnOrder;

    public Board() {
        board = new int[8][8];
        board[3][3] = -1;
        board[3][4] = 1;
        board[4][3] = -1;
        board[4][4] = 1;
        turnOrder = 1;
    }

    public int[][] getBoard() {
        return board;
    }

    public void makeTurn(int x, int y) {
        board[x][y] = turnOrder;
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
