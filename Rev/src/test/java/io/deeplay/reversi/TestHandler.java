package io.deeplay.reversi;

import io.deeplay.reversi.exceptions.ReversiException;
import io.deeplay.reversi.handler.Handler;
import io.deeplay.reversi.models.board.Board;
import io.deeplay.reversi.models.board.Cell;
import io.deeplay.reversi.models.chip.Color;
import org.apache.log4j.BasicConfigurator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestHandler {
    private Board board;
    private Handler handler;

    @BeforeEach
    public void init() throws ReversiException {
        board = new Board();
        handler = new Handler();
        handler.initializationBoard(board);
        BasicConfigurator.configure();
    }


    @Test
    public void testInitializationBoard() {
        assertEquals(board.getChipColor(3, 3), Color.WHITE);
        assertEquals(board.getChipColor(4, 4), Color.WHITE);
        assertEquals(board.getChipColor(3, 4), Color.BLACK);
        assertEquals(board.getChipColor(4, 3), Color.BLACK);
    }

    @Test
    public void testGameEndWithFullBoard() throws ReversiException {
        assertFalse(handler.isGameEnd(board));

        final int[][] arr =
                {
                        // 0   1   2   3   4   5   6   7
                        {42, 51, 52, 7, 6, 5, 55, 9}, //0
                        {44, 41, 43, 53, 4, 54, 8, 60}, //1
                        {45, 40, 12, 1, 2, 3, 10, 11}, //2
                        {46, 39, 13, 0, 0, 56, 59, 30}, //3
                        {47, 38, 14, 0, 0, 57, 58, 29}, //4
                        {48, 37, 15, 20, 17, 18, 19, 28}, //5
                        {49, 36, 16, 21, 22, 24, 32, 31}, //6
                        {50, 35, 34, 33, 23, 25, 26, 27}  //7
                };

        Color color = Color.BLACK;

        for (int i = 1; i <= 60; i++) {
            for (int row = 0; row < arr.length; row++) {
                for (int column = 0; column < arr[row].length; column++) {
                    if (arr[row][column] == i) {
                        assertFalse(handler.isGameEnd(board));
                        handler.makeStep(board, new Cell(row, column), color);
                        color = color.reverseColor();
                    }
                }
            }
        }

        assertTrue(handler.isGameEnd(board));
        assertEquals(handler.getScoreBlack(board), 34);
        assertEquals(handler.getScoreWhite(board), 30);
    }

    @Test
    public void testGameEndWithNoFullBoard() throws ReversiException {
        assertFalse(handler.isGameEnd(board));

        handler.makeStep(board, new Cell(3, 2), Color.BLACK);
        handler.makeStep(board, new Cell(4, 2), Color.WHITE);
        handler.makeStep(board, new Cell(5, 2), Color.BLACK);
        handler.makeStep(board, new Cell(2, 4), Color.WHITE);
        handler.makeStep(board, new Cell(1, 5), Color.BLACK);
        handler.makeStep(board, new Cell(4, 1), Color.WHITE);
        handler.makeStep(board, new Cell(5, 4), Color.BLACK);
        handler.makeStep(board, new Cell(6, 2), Color.WHITE);
        handler.makeStep(board, new Cell(5, 1), Color.BLACK);
        handler.makeStep(board, new Cell(6, 1), Color.WHITE);
        handler.makeStep(board, new Cell(7, 0), Color.BLACK);
        handler.makeStep(board, new Cell(7, 1), Color.WHITE);
        handler.makeStep(board, new Cell(3, 0), Color.BLACK);
        handler.makeStep(board, new Cell(5, 3), Color.WHITE);
        handler.makeStep(board, new Cell(7, 2), Color.BLACK);
        handler.makeStep(board, new Cell(5, 5), Color.WHITE);
        handler.makeStep(board, new Cell(5, 6), Color.BLACK);
        handler.makeStep(board, new Cell(6, 3), Color.WHITE);
        handler.makeStep(board, new Cell(7, 3), Color.BLACK);
        handler.makeStep(board, new Cell(6, 4), Color.WHITE);
        handler.makeStep(board, new Cell(6, 0), Color.BLACK);
        handler.makeStep(board, new Cell(4, 0), Color.WHITE);
        handler.makeStep(board, new Cell(5, 0), Color.BLACK);
        handler.makeStep(board, new Cell(4, 6), Color.WHITE);
        handler.makeStep(board, new Cell(3, 7), Color.BLACK);
        handler.makeStep(board, new Cell(6, 5), Color.WHITE);
        handler.makeStep(board, new Cell(6, 6), Color.BLACK);

        assertTrue(handler.isGameEnd(board));
        assertEquals(handler.getScoreBlack(board), 31);
        assertEquals(handler.getScoreWhite(board), 0);
    }

    @Test
    public void testWrongStep() {
        assertThrows(ReversiException.class, () -> handler.makeStep(board, new Cell(0, 0), Color.BLACK));
    }

    @Test
    public void testIncorrectStep() {
        assertThrows(ReversiException.class, () -> handler.makeStep(board, new Cell(-1, 8), Color.BLACK));
    }
}
