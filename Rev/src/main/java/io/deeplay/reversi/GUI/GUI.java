package io.deeplay.reversi.GUI;

import io.deeplay.reversi.models.board.Board;
import io.deeplay.reversi.models.board.Cell;
import io.deeplay.reversi.models.chip.Color;

import javax.swing.*;
import java.awt.*;

/**
 * Класс GUI - класс графического интерфейса пользователя, расширяет JFrame
 */
public class GUI extends JFrame {

    private final String WHITE = "./src/main/resources/White.png";
    private final String BLACK = "./src/main/resources/Black.png";
    private final String ICON = "./src/main/resources/icon.png";

    /**
     * Поле GUI, которое хранит двумерный массив кнопок
     */
    private final JButton[][] buttons;

    /**
     * Конкструктор - создание оконного приложения
     */
    public GUI() {

        Board board = new Board();
        buttons = new JButton[board.getBoardSize()][board.getBoardSize()];

        createButtons();
        drawActiveBoard(board);
        setLayout(new GridLayout(8, 8, 3, 3));
        setTitle("Reversi");
        ImageIcon image = new ImageIcon(ICON);
        setIconImage(image.getImage());
        setSize(700, 700);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Функция добавляет кнопки в оконное приложение
     */
    public final void createButtons() {
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setContentAreaFilled(false);
                add(buttons[i][j]);
            }
        }
    }

    /**
     * Функция устанавливает фишки с board в оконное приложение
     *
     * @param board - доска, которую нужно отобразить с помошью кнопок
     */
    public final void drawActiveBoard(final Board board) {
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                if (board.getChipColor(i, j) == Color.WHITE) {
                    drawDisabledButtons(buttons[i][j], WHITE);
                } else if (board.getChipColor(i, j) == Color.BLACK) {
                    drawDisabledButtons(buttons[i][j], BLACK);
                } else {
                    buttons[i][j].setEnabled(true);
                }
            }
        }
        pushTheButton();
    }

    /**
     * Функция добавляет кнопке изображение и делает ее не активной
     *
     * @param button -  кнопка
     * @param color - адрес изображения
     */
    private void drawDisabledButtons(final JButton button, final String color){
        button.setIcon(new ImageIcon(color));
        button.setEnabled(false);
        button.setDisabledIcon(new ImageIcon(color));
    }

    /**
     * Функция закрепляет слушателя за каждой активной кнопкой, после нажатия кнопка становится неактивной
     */
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

    /**
     * Функция находит клетку, в которую можно походить и было совершено нажатие кнопки
     *
     * @param board - доска
     * @return возвращает Cell, если не нашел такую клетку, то возвращает null
     */
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

    /**
     * Функция создаёт оконное приложение с результатами игры
     */
    public final void winLoseWindow(final int scoreBlack, final int scoreWhite) {
        final JFrame window = new JFrame();
        window.setLayout(new BorderLayout());
        window.add(new MenuPanel(scoreBlack, scoreWhite));
        window.pack();
        ImageIcon image = new ImageIcon(ICON);
        window.setIconImage(image.getImage());
        window.setSize(300, 100);
        window.setLocationRelativeTo(null);
        window.setResizable(false);
        window.setVisible(true);
    }
}