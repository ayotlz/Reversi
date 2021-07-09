package io.deeplay.reversi.models.chip;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestChip {
    @Test
    public void testReverse() {
        Chip chip = new Chip(Color.BLACK);
        assertEquals(chip.getColor().reverseColor(), Color.WHITE);
    }

    @Test
    public void testWrongReverse() {
        Chip chip = new Chip(Color.NEUTRAL);
        assertEquals(chip.getColor().reverseColor(), Color.NEUTRAL);
    }

    @Test
    public void testSetColor() {
        Chip chip = new Chip(Color.WHITE);
        chip.setColor(Color.NEUTRAL);
        assertEquals(chip.getColor(), Color.NEUTRAL);
    }
}
