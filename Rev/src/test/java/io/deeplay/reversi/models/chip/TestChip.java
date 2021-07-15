package io.deeplay.reversi.models.chip;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestChip {
    @Test
    public void testReverse() {
        final Chip chip = new Chip(Color.BLACK);
        assertEquals(chip.getColor().reverseColor(), Color.WHITE);
    }

    @Test
    public void testWrongReverse() {
        final Chip chip = new Chip(Color.NEUTRAL);
        assertEquals(chip.getColor().reverseColor(), Color.NEUTRAL);
    }
}
