package io.deeplay.reversi.GUI;

import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame {

    public GUI() {
        setLayout(new FlowLayout());

        JButton[] buttons = new JButton[64];
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton();
            add(buttons[i]);
            int finalI = i;
            buttons[i].addActionListener(e -> buttons[finalI].setEnabled(false));
        }

        buttons[27].setEnabled(false);
        buttons[28].setEnabled(false);
        buttons[35].setEnabled(false);
        buttons[36].setEnabled(false);



        // Constructor to setup GUI components and event handlers
        setLayout(new GridLayout(8, 8, 3, 3));
        // "super" Frame sets layout to 3x2 GridLayout, horizontal and vertical gaps of 3 pixels

        // The components are added from left-to-right, top-to-bottom

        setTitle("Reversi");
//        this.setIconImage(new ImageIcon(GUI.class.getResource("io/deeplay/reversi/GUI/icon.png")).getImage());
        setSize(700, 700);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public static void main(String[] args) {
        new GUI();  // Let the constructor do the job
    }
}