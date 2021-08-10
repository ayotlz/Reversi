package models.board;

import models.chip.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BoardTest {
    private Board board;

    @BeforeEach
    void init() {
        board = new Board();
    }

    @Test
    void testGetChipColorBlack() {
        board.setChip(0, 0, Color.BLACK);
        assertEquals(Color.BLACK, board.getChipColor(0, 0));
    }

    @Test
    void testGetChipColorWhite() {
        board.setChip(0, 0, Color.WHITE);
        assertEquals(Color.WHITE, board.getChipColor(0, 0));
    }

    @Test
    void testGetAllColors() {
        for (int i = 0; i < board.getBoardSize(); i++) {
            for (int j = 0; j < board.getBoardSize(); j++) {
                assertEquals(Color.NEUTRAL, board.getChipColor(i, j));
            }
        }
    }

    @Test
    void testGetBoardSize() {
        assertEquals(8, board.getBoardSize());
    }

    @Test
    void testReverseChip() {
        board.setChip(0, 0, Color.WHITE);
        board.setChip(0, 1, Color.BLACK);
        board.reverseChip(0, 0);
        board.reverseChip(0, 1);
        board.reverseChip(0, 2);
        assertEquals(Color.BLACK, board.getChipColor(0, 0));
        assertEquals(Color.WHITE, board.getChipColor(0, 1));
        assertEquals(Color.NEUTRAL, board.getChipColor(0, 2));
    }

    @Test
    void testGetScoreMap() {
        board.setChip(0, 0, Color.BLACK);
        final Cell cell1 = new Cell(0, 1);
        board.setChip(0, 1, Color.WHITE);

        final Cell cell2 = new Cell(0, 2);

        final List<Cell> chipsOfOpponent = new ArrayList<>();
        chipsOfOpponent.add(cell1);

        final Map<Cell, List<Cell>> scoreBlackMap = new HashMap<>();
        final Map<Cell, List<Cell>> scoreWhiteMap = new HashMap<>();
        scoreBlackMap.put(cell2, chipsOfOpponent);

        assertEquals(scoreBlackMap, board.getScoreMap(Color.BLACK));
        assertEquals(scoreWhiteMap, board.getScoreMap(Color.WHITE));
    }
}
