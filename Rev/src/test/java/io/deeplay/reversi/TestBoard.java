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

//    @Test
//    public void testApply(){
//        JFrame frame = new JFrame("Reversi");
//        frame.setBounds(100, 100, 400, 400);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        String workingDir = System.getProperty("user.dir");
//        String iconFilename = workingDir + File.separator + "res" + File.separator + "Icon32.png";
//        ImageIcon icon = new ImageIcon(iconFilename);
//        frame.setIconImage(icon.getImage());
//        frame.setVisible(true);
//        frame.;
//
//        JLabel cell = new JLabel(new ImageIcon("MyImage"));
//    }
}
