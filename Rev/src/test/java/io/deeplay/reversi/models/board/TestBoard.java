package io.deeplay.reversi.models.board;

import io.deeplay.reversi.models.chip.Chip;
import io.deeplay.reversi.models.chip.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestBoard {
    private Board board;

    @BeforeEach
    public void init() {
        board = new Board();
    }

    @Test
    public void testGetColor() {
        assertEquals(Color.NEUTRAL, board.getChipColor(0, 0));
    }

    @Test
    public void testGetColorBlack() {
        board.setChip(0, 0, Color.BLACK);
        assertEquals(Color.BLACK, board.getChipColor(0, 0));
    }

    @Test
    public void testGetColorWhite() {
        board.setChip(0, 0, Color.WHITE);
        assertEquals(Color.WHITE, board.getChipColor(0, 0));
    }

    @Test
    public void testGetAllColors() {
        for (int i = 0; i < board.getBoardSize(); i++) {
            for (int j = 0; j < board.getBoardSize(); j++) {
                assertEquals(Color.NEUTRAL, board.getChipColor(i, j));
            }
        }
    }

    @Test
    public void testGetWhiteChip() {
        final Chip chip = new Chip(Color.WHITE);
        board.setChip(0, 0, Color.WHITE);
        assertEquals(chip.getColor(), board.getChipColor(0, 0));
    }

    @Test
    public void testGetBlackChip() {
        final Chip chip = new Chip(Color.BLACK);
        board.setChip(0, 0, Color.BLACK);
        assertEquals(chip.getColor(), board.getChipColor(0, 0));
    }
}
