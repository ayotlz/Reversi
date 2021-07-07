package io.deeplay.reversi;

import io.deeplay.reversi.chip.Chip;
import io.deeplay.reversi.chip.Color;
import io.deeplay.reversi.exceptions.ReversiException;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestBoard {
//    @Test
//    public void test() throws ReversiException {
//        Board board = new Board();
//        Handler handler = new Handler();
//        handler.beforeGame(board);
//        System.out.println(board.toString());
//        handler.makeStep(board, Color.BLACK, new Cell(2, 3));
//        System.out.println(board.toString());
//        handler.makeStep(board, Color.WHITE, new Cell(4, 2));
//        System.out.println(board.toString());
//        handler.makeStep(board, Color.BLACK, new Cell(5, 1));
//        System.out.println(board.toString());
//        handler.makeStep(board, Color.WHITE, new Cell(2, 2));
//        System.out.println(board.toString());
//
//        handler.makeStep(board, Color.BLACK, new Cell(0, 0));
//        System.out.println(board.toString());
//    }

    @Test (expected = ReversiException.class)
    public void testGetColorException() throws ReversiException {
        Board board = new Board();
        board.getColor(-2,4);
    }

    @Test
    public void testGetColor() throws ReversiException {
        Board board = new Board();
        assertEquals(Color.NEUTRAL, board.getColor(0,0));
    }

    @Test
    public void testGetColorBlack() throws ReversiException {
        Board board = new Board();
        board.getArray()[0][0].setColor(Color.BLACK);
        assertEquals(Color.BLACK, board.getColor(0,0));
    }
    @Test
    public void testGetColorWhite() throws ReversiException {
        Board board = new Board();
        board.getArray()[0][0].setColor(Color.WHITE);
        assertEquals(Color.WHITE, board.getColor(0,0));
    }

    @Test
    public void testGetAllColors() throws ReversiException {
        Board board = new Board();
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                assertEquals(Color.NEUTRAL, board.getColor(i,j));
            }
        }
    }

    @Test
    public void testGetWhiteChip() throws ReversiException {
        Board board = new Board();
        Chip chip = new Chip(Color.WHITE);
        board.getArray()[0][0].setColor(Color.WHITE);
        assertEquals(chip, board.getChip(0,0));
    }

    @Test
    public void testGetBlackChip() throws ReversiException {
        Board board = new Board();
        Chip chip = new Chip(Color.BLACK);
        board.getArray()[0][0].setColor(Color.BLACK);
        assertEquals(chip, board.getChip(0,0));
    }



//    @Test
//    public void testest() {
//        Board board = new Board();
//        board.setBlack(3, 4);
//        board.setBlack(4, 5);
//        board.setBlack(5, 3);
//        board.setBlack(6, 2);
//
//        board.setWhite(3, 3);
//        board.setWhite(4, 3);
//        board.setWhite(4, 4);
//        board.setWhite(5, 5);
//
//        Handler handler = new Handler();
//
//        List<Cell> c = handler.findWhiteOrBlackChips(board, Color.BLACK);
//        System.out.println("Размер c: " + c.size());
//
//        Map<Cell, List<Cell>> m = handler.findNeighborhood(c, board);
//        System.out.println("Размер m: " + m.size());
//
//        Map<Cell, Integer> s = handler.getScoreMap(board, m, Color.BLACK);
//
//        for (Map.Entry<Cell, Integer> entry : s.entrySet()) {
//            System.out.print(entry.getKey().getX());
//            System.out.print(" ");
//            System.out.print(entry.getKey().getY());
//            System.out.print(": ");
//            System.out.println(entry.getValue());
//        }
//
//        System.out.println(board.toString());
//    }
//    @Test
//    public void testNormalGame() {
//        Board board = new Board();
//        board.makeTurn(3, 2);
//        board.makeTurn(4, 2);
//        board.makeTurn(5, 1);
//        board.makeTurn(2, 2);
//        board.makeTurn(4, 5);
//        board.makeTurn(5, 2);
//
//        Chip[][] boardResult = board.getArray();
//
//        assertEquals(board.getColor(5, 1), Color.BLACK);
//        assertEquals(board.getColor(2, 2), Color.WHITE);
//        assertEquals(board.getColor(3, 2), Color.WHITE);
//        assertEquals(board.getColor(4, 2), Color.WHITE);
//        assertEquals(board.getColor(5, 2), Color.WHITE);
//        assertEquals(board.getColor(3, 3), Color.WHITE);
//        assertEquals(board.getColor(4, 3), Color.BLACK);
//        assertEquals(board.getColor(3, 4), Color.BLACK);
//        assertEquals(board.getColor(4, 4), Color.BLACK);
//        assertEquals(board.getColor(4, 5), Color.BLACK);
//    }
//
//
//    @Test
//    public void testIncorrectStep() {
//        Board board = new Board();
//        board.makeTurn(1, 1);
//
//        assertEquals(board.getColor(1, 1), Color.NEUTRAL);
//    }
//
//    @Test
//    public void testWrongData() {
//        Board board = new Board();
//        board.makeTurn(9, 9);
//    }
//
//    @Test
//    public void testst() {
//        Handler handler = new Handler();
//        Board board = new Board();
//
//        handler.makeStep(board, );
//    }
}
