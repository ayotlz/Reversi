package models.board;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CellTest {
    final Cell cell = new Cell(0, 0);

    @Test
    void getX() {
        assertEquals(0, cell.getX());
    }

    @Test
    void getY() {
        assertEquals(0, cell.getY());
    }
}
