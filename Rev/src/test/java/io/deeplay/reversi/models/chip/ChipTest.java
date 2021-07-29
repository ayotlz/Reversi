package io.deeplay.reversi.models.chip;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ChipTest {

    final Chip chip = new Chip(Color.NEUTRAL);
    final Chip chipWhite = new Chip(Color.WHITE);
    final Chip chipBlack = new Chip(Color.BLACK);

    @Test
    void testGetColor() {
        assertEquals(Color.NEUTRAL, chip.getColor());
        assertEquals(Color.WHITE, chipWhite.getColor());
        assertEquals(Color.BLACK, chipBlack.getColor());
    }

    @Test
    void testReverseColor() {
        assertEquals(chipWhite.getColor().reverseColor(), Color.BLACK);
        assertEquals(chipBlack.getColor().reverseColor(), Color.WHITE);
        assertEquals(chip.getColor().reverseColor(), Color.NEUTRAL);
    }
}
