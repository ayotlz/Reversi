package io.deeplay.reversi.GUI;

import io.deeplay.reversi.models.board.Board;
import io.deeplay.reversi.models.board.Cell;
import io.deeplay.reversi.models.chip.Color;

import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame {

    private final JButton[][] buttons;

    public GUI() {

        Board board = new Board();

        buttons = new JButton[board.getBoardSize()][board.getBoardSize()];

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
                } else if (board.getChipColor(i, j) == Color.BLACK) {
                    buttons[i][j].setIcon(new ImageIcon(black));
                    buttons[i][j].setEnabled(false);
                    buttons[i][j].setDisabledIcon(new ImageIcon(black));
                } else {
                    buttons[i][j].setEnabled(true);
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
                    buttons[i][j].addActionListener(e -> buttons[finalI][finalJ].setEnabled(false));
                }
            }
        }
    }

    public final Cell getAnswerCell(final Board board) {
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons.length; j++) {
                if (!buttons[i][j].isEnabled() && board.getChipColor(i, j).equals(Color.NEUTRAL)) {
                    return new Cell(i, j);
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
        new GUI();
    }
}