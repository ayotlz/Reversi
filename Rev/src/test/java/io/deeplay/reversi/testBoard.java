package io.deeplay.reversi;

import io.deeplay.reversi.chip.Chip;
import org.junit.Test;

public class testBoard {
    @Test
    public void test1() {
        Board board = new Board();
        for (Chip[] chips : board.getBoard()) {
            for (Chip chip : chips) {
                System.out.println(chip.getColor());
            }
        }
    }
}
