package io.deeplay.reversi.GUI;

import io.deeplay.reversi.models.board.Board;
import io.deeplay.reversi.models.chip.Color;

import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame {

    private final Board board = new Board();

    private final JButton[][] buttons = new JButton[board.getBoardSize()][board.getBoardSize()];

    public final JButton[][] getButtons() {
        return buttons;
    }

    public GUI() {

        createButtons();
        drawActiveBoard(board);
        setLayout(new GridLayout(8, 8, 3, 3));
        setTitle("Reversi");
        ImageIcon image = new ImageIcon("./src/main/resources/icon.png");
        setIconImage(image.getImage());
        setSize(700, 700);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public final void createButtons() {
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setContentAreaFilled(false);
                add(buttons[i][j]);
            }
        }
    }

    public final void drawActiveBoard(final Board board) {
        final String white = "./src/main/resources/White.png";
        final String black = "./src/main/resources/Black.png";
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                if (board.getChipColor(i, j) == Color.WHITE) {
                    buttons[i][j].setIcon(new ImageIcon(white));
                    buttons[i][j].setEnabled(false);
                    buttons[i][j].setDisabledIcon(new ImageIcon(white));
                }
                if (board.getChipColor(i, j) == Color.BLACK) {
                    buttons[i][j].setIcon(new ImageIcon(black));
                    buttons[i][j].setEnabled(false);
                    buttons[i][j].setDisabledIcon(new ImageIcon(black));
                }
            }
        }
        pushTheButton();
    }

    public final void pushTheButton() {
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                if (buttons[i][j].isEnabled()) {
                    int finalI = i;
                    int finalJ = j;
                    //                    ПОДПРАВИТЬ ЭТО!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    buttons[i][j].addActionListener(e -> buttons[finalI][finalJ].setEnabled(false));
                }
            }
        }
    }

    public static void main(String[] args) {
        new GUI();
    }
}