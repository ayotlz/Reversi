package io.deeplay.reversi;

import io.deeplay.reversi.models.board.Board;
import io.deeplay.reversi.models.chip.Chip;
import io.deeplay.reversi.models.chip.Color;
import io.deeplay.reversi.exceptions.ReversiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestBoard {
    static Board board;

    @BeforeEach
    public void init() {
        board = new Board();
    }

    @Test
    public void testGetColorException() throws ReversiException {
        assertThrows(ReversiException.class, () -> {
            board.getColor(-2,4);
        });
    }

    @Test
    public void testGetColor() throws ReversiException {
        assertEquals(Color.NEUTRAL, board.getColor(0,0));
    }

    @Test
    public void testGetColorBlack() throws ReversiException {
        board.getArray()[0][0].setColor(Color.BLACK);
        assertEquals(Color.BLACK, board.getColor(0,0));
    }
    @Test
    public void testGetColorWhite() throws ReversiException {
        board.getArray()[0][0].setColor(Color.WHITE);
        assertEquals(Color.WHITE, board.getColor(0,0));
    }

    @Test
    public void testGetAllColors() throws ReversiException {
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                assertEquals(Color.NEUTRAL, board.getColor(i,j));
            }
        }
    }

    @Test
    public void testGetWhiteChip() throws ReversiException {
        Chip chip = new Chip(Color.WHITE);
        board.getArray()[0][0].setColor(Color.WHITE);
        assertEquals(chip, board.getChip(0,0));
    }

    @Test
    public void testGetBlackChip() throws ReversiException {
        Chip chip = new Chip(Color.BLACK);
        board.getArray()[0][0].setColor(Color.BLACK);
        assertEquals(chip, board.getChip(0,0));
    }
}
