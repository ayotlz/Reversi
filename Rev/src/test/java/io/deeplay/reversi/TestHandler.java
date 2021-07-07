package io.deeplay.reversi;

import io.deeplay.reversi.chip.Color;
import io.deeplay.reversi.exceptions.ReversiException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestHandler {
    @Test
    public void testInitializationBoard() throws ReversiException {
        Board board = new Board();
        Handler handler = new Handler();
        handler.initializationBoard(board);

        assertEquals(board.getArray()[3][3].getColor(), Color.WHITE);
        assertEquals(board.getArray()[4][4].getColor(), Color.WHITE);
        assertEquals(board.getArray()[3][4].getColor(), Color.BLACK);
        assertEquals(board.getArray()[4][3].getColor(), Color.BLACK);
    }

    @Test
    public void testGameEnd() throws ReversiException {
        Board board = new Board();
        Handler handler = new Handler();
        handler.initializationBoard(board);

        assertFalse(handler.isGameEnd(board));

        handler.makeStep(board, Color.BLACK, new Cell(3, 2));
        handler.makeStep(board, Color.WHITE, new Cell(4, 2));
        handler.makeStep(board, Color.BLACK, new Cell(5, 2));
    }
}
