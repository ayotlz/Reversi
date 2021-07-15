package io.deeplay.reversi;

import io.deeplay.reversi.models.board.Board;
import io.deeplay.reversi.models.chip.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestBoard {
    static Board board;

    @BeforeEach
    public void init() {
        board = new Board();
    }

    @Test
    public void testGetColor() {
        assertEquals(Color.NEUTRAL, board.getColor(0,0));
    }

    @Test
    public void testGetColorBlack() {
        board.setColor(0,0, Color.BLACK);
        assertEquals(Color.BLACK, board.getColor(0,0));
    }
    @Test
    public void testGetColorWhite() {
        board.setColor(0,0, Color.WHITE);
        assertEquals(Color.WHITE, board.getColor(0,0));
    }

    @Test
    public void testGetAllColors() {
        for (int i = 0; i < board.getBoardSize(); i++) {
            for (int j = 0; j < board.getBoardSize(); j++) {
                assertEquals(Color.NEUTRAL, board.getColor(i,j));
            }
        }
    }

    @Test
    public void testGetWhiteChip() {
        board.setColor(0,0, Color.WHITE);
        assertEquals(Color.WHITE, board.getColor(0,0));
    }

    @Test
    public void testGetBlackChip() {
        board.setColor(0,0, Color.BLACK);
        assertEquals(Color.BLACK, board.getColor(0,0));
    }
}
