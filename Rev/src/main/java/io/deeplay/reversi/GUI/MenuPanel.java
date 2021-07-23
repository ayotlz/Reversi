package io.deeplay.reversi.GUI;

import javax.swing.*;
import java.awt.*;

/**
 * Класс MenuPanel - класс для создания дополнительной панели, расширяет JPanel
 */
public class MenuPanel extends JPanel {

    /**
     * Конкструктор панели с итоговым счетом
     *
     * @param scoreBlack - количество чёрных фишек
     * @param scoreWhite - количество белых фишек
     */
    public MenuPanel(final int scoreBlack, final int scoreWhite) {
        final JLabel text1 = new JLabel("Игра окончена!");
        final JLabel text2 = new JLabel(String.format("Чёрные: %d", scoreBlack));
        final JLabel text3 = new JLabel(String.format("Белые: %d", scoreWhite));
        final JButton button = new JButton("Выход");
        button.addActionListener(evt -> System.exit(0));

        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 20;
        gbc.ipady = 20;

        add(text1, gbc);
        gbc.gridy++;
        add(text2, gbc);
        gbc.gridy++;
        add(text3, gbc);
        gbc.gridy++;
        add(button, gbc);
    }
}
