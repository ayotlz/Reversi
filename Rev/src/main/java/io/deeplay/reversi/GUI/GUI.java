package io.deeplay.reversi.GUI;

import io.deeplay.reversi.models.board.Board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import io.deeplay.reversi.models.chip.Color;

public class GUI extends JFrame {

    public GUI() {
        setLayout(new FlowLayout());

        Board board = new Board();
        for (int i = 0; i < board.getBoardSize(); i++) {
            for (int j = 0; j < board.getBoardSize(); j++) {
                board.setChip(i, j, Color.BLACK);
            }
        }
        board.setChip(1, 1, Color.WHITE);
        drawBoard(board);

        setLayout(new GridLayout(8, 8, 3, 3));
        setTitle("Reversi");
        ImageIcon image = new ImageIcon("C:/Users/KIRILL-NOTEBOOK/IdeaProjects/Reversi/Rev/src/main/resources/icon.png");
        setIconImage(image.getImage());
        setSize(700, 700);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    private void drawBoard(final Board board) {
        final String white = "C:/Users/KIRILL-NOTEBOOK/IdeaProjects/Reversi/Rev/src/main/resources/White.png";
        final String black = "C:/Users/KIRILL-NOTEBOOK/IdeaProjects/Reversi/Rev/src/main/resources/Black.png";
        JButton[][] buttons = new JButton[board.getBoardSize()][board.getBoardSize()];
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setFocusPainted(false);
                buttons[i][j].setContentAreaFilled(false);
                System.out.println(board.getChipColor(i,j));
                if (board.getChipColor(i,j) == Color.WHITE){
                    buttons[i][j].setIcon(new ImageIcon(white));
                    buttons[i][j].setEnabled(false);
                }
                if (board.getChipColor(i,j) == Color.BLACK){
                    buttons[i][j].setIcon(new ImageIcon(black));
                    buttons[i][j].setEnabled(false);
                }
                add(buttons[i][j]);
                int finalI = i;
                int finalJ = j;
                buttons[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        buttons[finalI][finalJ].setEnabled(false);
                    }
                });
            }
        }
    }

    public static void main(String[] args) {
        new GUI();
    }
}