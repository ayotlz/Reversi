package io.deeplay.reversi;

import io.deeplay.reversi.chip.Chip;
import io.deeplay.reversi.chip.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class testBoard {
    @Test
    public void testNormalGame() {
        Board board = new Board();
        board.makeTurn(3, 2);
        board.makeTurn(4, 2);
        board.makeTurn(5, 1);
        board.makeTurn(2, 2);
        board.makeTurn(4, 5);
        board.makeTurn(5, 2);

        Chip[][] boardResult = board.getBoard();

        assertEquals(board.getColor(5, 1), Color.BLACK);
        assertEquals(board.getColor(2, 2), Color.WHITE);
        assertEquals(board.getColor(3, 2), Color.WHITE);
        assertEquals(board.getColor(4, 2), Color.WHITE);
        assertEquals(board.getColor(5, 2), Color.WHITE);
        assertEquals(board.getColor(3, 3), Color.WHITE);
        assertEquals(board.getColor(4, 3), Color.BLACK);
        assertEquals(board.getColor(3, 4), Color.BLACK);
        assertEquals(board.getColor(4, 4), Color.BLACK);
        assertEquals(board.getColor(4, 5), Color.BLACK);
    }


    @Test
    public void testIncorrectStep() {
        Board board = new Board();
        board.makeTurn(1, 1);

        assertEquals(board.getColor(1, 1), Color.NEUTRAL);
    }

    @Test
    public void testWrongData() {
        Board board = new Board();
        board.makeTurn(9, 9);
    }

//    @Test
//    public void testst() {
//        Handler handler = new Handler();
//        Board board = new Board();
//
//        handler.makeStep(board, );
//    }
}
