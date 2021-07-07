package io.deeplay.reversi;

import io.deeplay.reversi.chip.Color;
import io.deeplay.reversi.exceptions.ReversiException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestHandler {
    static Board board;
    static Handler handler;

    @BeforeEach
    public void init() throws ReversiException {
        board = new Board();
        handler = new Handler();
        handler.initializationBoard(board);
    }


    @Test
    public void testInitializationBoard() throws ReversiException {
        assertEquals(board.getArray()[3][3].getColor(), Color.WHITE);
        assertEquals(board.getArray()[4][4].getColor(), Color.WHITE);
        assertEquals(board.getArray()[3][4].getColor(), Color.BLACK);
        assertEquals(board.getArray()[4][3].getColor(), Color.BLACK);
    }

    @Test
    public void testGameEndWithNoFullBoard() throws ReversiException {
        assertFalse(handler.isGameEnd(board));

        handler.makeStep(board, Color.BLACK, new Cell(3, 2));
        handler.makeStep(board, Color.WHITE, new Cell(4, 2));
        handler.makeStep(board, Color.BLACK, new Cell(5, 2));
        handler.makeStep(board, Color.WHITE, new Cell(2, 4));
        handler.makeStep(board, Color.BLACK, new Cell(1, 5));
        handler.makeStep(board, Color.WHITE, new Cell(4, 1));
        handler.makeStep(board, Color.BLACK, new Cell(5, 4));
        handler.makeStep(board, Color.WHITE, new Cell(6, 2));
        handler.makeStep(board, Color.BLACK, new Cell(5, 1));
        handler.makeStep(board, Color.WHITE, new Cell(6, 1));
        handler.makeStep(board, Color.BLACK, new Cell(7, 0));
        handler.makeStep(board, Color.WHITE, new Cell(7, 1));
        handler.makeStep(board, Color.BLACK, new Cell(3, 0));
        handler.makeStep(board, Color.WHITE, new Cell(5, 3));
        handler.makeStep(board, Color.BLACK, new Cell(7, 2));
        handler.makeStep(board, Color.WHITE, new Cell(5, 5));
        handler.makeStep(board, Color.BLACK, new Cell(5, 6));
        handler.makeStep(board, Color.WHITE, new Cell(6, 3));
        handler.makeStep(board, Color.BLACK, new Cell(7, 3));
        handler.makeStep(board, Color.WHITE, new Cell(6, 4));
        handler.makeStep(board, Color.BLACK, new Cell(6, 0));
        handler.makeStep(board, Color.WHITE, new Cell(4, 0));
        handler.makeStep(board, Color.BLACK, new Cell(5, 0));
        handler.makeStep(board, Color.WHITE, new Cell(4, 6));
        handler.makeStep(board, Color.BLACK, new Cell(3, 7));
        handler.makeStep(board, Color.WHITE, new Cell(6, 5));
        handler.makeStep(board, Color.BLACK, new Cell(6, 6));

        assertTrue(handler.isGameEnd(board));
    }

    @Test
    public void testGameEndWithFullBoard() throws ReversiException {
        Board board = new Board();
        Handler handler = new Handler();
        handler.initializationBoard(board);

        assertFalse(handler.isGameEnd(board));

        int[][] arr =
                {
                        { 1,  2,  3,  4,  5, 6,  7, 8},
                        {46, 12, 12, 13, 15, 1, 46, 1},
                        {10, 11, 12,  1,  2, 1, 46, 1},
                        {10, 11, 12, 13, 15, 1, 46, 1},
                        {10, 11, 12, 13, 15, 1, 46, 1},
                        {10, 11, 12, 13, 15, 1, 46, 1},
                        {10, 11, 12, 13, 15, 1, 46, 1}
                };


        handler.makeStep(board, Color.BLACK, new Cell(2, 3));
        handler.makeStep(board, Color.WHITE, new Cell(2, 4));
        handler.makeStep(board, Color.BLACK, new Cell(2, 5));
        handler.makeStep(board, Color.WHITE, new Cell(1, 4));
        handler.makeStep(board, Color.BLACK, new Cell(0, 5));
        handler.makeStep(board, Color.WHITE, new Cell(0, 4));
        handler.makeStep(board, Color.BLACK, new Cell(0, 3));
        handler.makeStep(board, Color.WHITE, new Cell(1, 6));
        handler.makeStep(board, Color.BLACK, new Cell(0, 7));
        handler.makeStep(board, Color.WHITE, new Cell(2, 6));
        handler.makeStep(board, Color.BLACK, new Cell(2, 7));
        handler.makeStep(board, Color.WHITE, new Cell(2, 2));
        handler.makeStep(board, Color.BLACK, new Cell(3, 2));
        handler.makeStep(board, Color.WHITE, new Cell(4, 2));
        handler.makeStep(board, Color.BLACK, new Cell(5, 2));
        handler.makeStep(board, Color.WHITE, new Cell(6, 2));
        handler.makeStep(board, Color.BLACK, new Cell(5, 4));
        handler.makeStep(board, Color.WHITE, new Cell(5, 5));
        handler.makeStep(board, Color.BLACK, new Cell(5, 6));
        handler.makeStep(board, Color.WHITE, new Cell(5, 3));
        handler.makeStep(board, Color.BLACK, new Cell(6, 3));
        handler.makeStep(board, Color.WHITE, new Cell(6, 4));
        handler.makeStep(board, Color.BLACK, new Cell(7, 4));
        handler.makeStep(board, Color.WHITE, new Cell(6, 5));
        handler.makeStep(board, Color.BLACK, new Cell(7, 5));
        handler.makeStep(board, Color.WHITE, new Cell(7, 6));
        handler.makeStep(board, Color.BLACK, new Cell(7, 7));
        handler.makeStep(board, Color.WHITE, new Cell(5, 7));
        handler.makeStep(board, Color.BLACK, new Cell(4, 7));
        handler.makeStep(board, Color.WHITE, new Cell(3, 7));
        handler.makeStep(board, Color.BLACK, new Cell(6, 7));
        handler.makeStep(board, Color.WHITE, new Cell(6, 6));
        handler.makeStep(board, Color.BLACK, new Cell(7, 3));
        handler.makeStep(board, Color.WHITE, new Cell(7, 2));
        handler.makeStep(board, Color.BLACK, new Cell(7, 1));
        handler.makeStep(board, Color.WHITE, new Cell(6, 1));
        handler.makeStep(board, Color.BLACK, new Cell(5, 1));
        handler.makeStep(board, Color.WHITE, new Cell(4, 1));
        handler.makeStep(board, Color.BLACK, new Cell(3, 1));
        handler.makeStep(board, Color.WHITE, new Cell(2, 1));
        handler.makeStep(board, Color.BLACK, new Cell(1, 1));
        handler.makeStep(board, Color.WHITE, new Cell(0, 0));
        handler.makeStep(board, Color.BLACK, new Cell(1, 2));
        handler.makeStep(board, Color.WHITE, new Cell(1, 0));
        handler.makeStep(board, Color.BLACK, new Cell(2, 0));
        handler.makeStep(board, Color.WHITE, new Cell(3, 0));
        handler.makeStep(board, Color.BLACK, new Cell(4, 0));
        handler.makeStep(board, Color.WHITE, new Cell(5, 0));
        handler.makeStep(board, Color.BLACK, new Cell(6, 0));
        handler.makeStep(board, Color.WHITE, new Cell(7, 0));
        handler.makeStep(board, Color.BLACK, new Cell(0, 1));
        handler.makeStep(board, Color.WHITE, new Cell(0, 2));
        handler.makeStep(board, Color.BLACK, new Cell(1, 3));
        handler.makeStep(board, Color.WHITE, new Cell(1, 5));
        handler.makeStep(board, Color.BLACK, new Cell(0, 6));
        handler.makeStep(board, Color.WHITE, new Cell(3, 5));
        handler.makeStep(board, Color.BLACK, new Cell(4, 5));
        handler.makeStep(board, Color.WHITE, new Cell(4, 6));
        handler.makeStep(board, Color.BLACK, new Cell(3, 6));

        System.out.println(board.toString());

        assertFalse(handler.isGameEnd(board));

        handler.makeStep(board, Color.WHITE, new Cell(1, 7));

        assertTrue(handler.isGameEnd(board));
    }

    @Test
    public void testWrongStep() throws ReversiException {
        Board board = new Board();
        Handler handler = new Handler();
        handler.initializationBoard(board);

        assertFalse(handler.makeStep(board, Color.BLACK, new Cell(0, 0)));
    }

    @Test
    public void testIncorrectStep() throws ReversiException {
        Board board = new Board();
        Handler handler = new Handler();
        handler.initializationBoard(board);

        handler.makeStep(board, Color.BLACK, new Cell(-1, -1));
    }
}