package io.deeplay.reversi.models.board;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.deeplay.reversi.models.chip.Chip;
import io.deeplay.reversi.models.chip.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс Board - класс доски
 */
public class Board {
    /**
     * Поле доски, которое хранит двумерный массив фишек
     */
    @JsonProperty
    private final Chip[][] board;

    public Chip[][] getBoard() {
        return board;
    }

    public Color getNextPlayer() {
        return nextPlayer;
    }

    public void setNextPlayer() {
        this.nextPlayer = nextPlayer.reverseColor();
    }

    @JsonProperty
    private Color nextPlayer;

    /**
     * Поле размера доски
     */
    private final int boardSize = 8;

    /**
     * Конкструктор - создание доски размера boardSize с нейтральными фишками по умолчанию
     */
    public Board() {
        this.board = new Chip[boardSize][boardSize];

        for (final Chip[] chips : this.board) {
            for (int j = 0; j < this.board.length; j++) {
                chips[j] = new Chip(Color.NEUTRAL);
            }
        }
        nextPlayer = Color.BLACK;
    }

    public Board(final Board b) {
        board = new Chip[boardSize][boardSize];
        for (int i = 0; i < b.board.length; i++) {
            System.arraycopy(b.board[i], 0, board[i], 0, b.board[i].length);
        }
    }

    /**
     * Конкструктор копирования доски
     *
     * @param board - исходная доска
     */
    @JsonCreator
    public Board(@JsonProperty("board") final Chip[][] board, @JsonProperty("nextPlayer") final Color nextPlayer) {
        this.board = new Chip[boardSize][boardSize];

        for (int i = 0; i < board.length; i++) {
            System.arraycopy(board[i], 0, this.board[i], 0, board.length);
        }
        this.nextPlayer = nextPlayer;
    }

    public Board(final Board board) {
        this.board = new Chip[board.getBoardSize()][board.getBoardSize()];

        for (int i = 0; i < board.getBoardSize(); i++) {
            for (int j = 0; j < board.getBoardSize(); j++) {
                this.board[i][j] = new Chip(board.getChipColor(i,j));
            }
        }
        nextPlayer = board.nextPlayer;
    }

    /**
     * Функция возвращает цвет на клетке с координатами x и y
     *
     * @param x - координата x
     * @param y - координата y
     * @return возвращает Color
     */
    public final Color getChipColor(final int x, final int y) {
        return board[x][y].getColor();
    }

    /**
     * Функция возвращает размер доски
     *
     * @return возвращает boardSize
     */
    public final int getBoardSize() {
        return boardSize;
    }

    /**
     * Функция устанавливает цвет фишки в клетке
     *
     * @param x     - координата x
     * @param y     - координата x
     * @param color - цвет фишки
     */
    public final void setChip(final int x, final int y, final Color color) {
        board[x][y] = new Chip(color);
    }

    /**
     * Функция переворота фишки по координатам
     *
     * @param x - координата x
     * @param y - координата y
     */
    public final void reverseChip(final int x, final int y) {
        setChip(x, y, board[x][y].getColor().reverseColor());
    }

    /**
     * Функция ищет клетки, в которые можно походить
     *
     * @param turnOrder - цвет, который совершает ход в данный момент
     * @return возвращает Map: ключ - клетка, куда можно походить; значение - список клеток, фишки в которых будут
     * перевёрнуты, в случае хода в клетку из ключа
     */
    public final Map<Cell, List<Cell>> getScoreMap(final Color turnOrder) {
        final List<Cell> chipsOfOpponent = findOpponentsChips(turnOrder);
        final Map<Cell, List<Cell>> mapNeighborhood = findNeighborhood(chipsOfOpponent);
        final Map<Cell, List<Cell>> scoreMap = new HashMap<>();
        for (final Map.Entry<Cell, List<Cell>> entry : mapNeighborhood.entrySet()) {
            for (Cell cell : entry.getValue()) {
                scoreMap.put(cell, new ArrayList<>());
            }
        }
        for (final Map.Entry<Cell, List<Cell>> entry : mapNeighborhood.entrySet()) {
            for (Cell cell : entry.getValue()) {
                scoreMap.get(cell).addAll(getListOfFlipCells(cell, entry.getKey(), turnOrder));
            }
        }
        final List<Cell> listToDelete = new ArrayList<>();
        for (final Map.Entry<Cell, List<Cell>> entry : scoreMap.entrySet()) {
            if (scoreMap.get(entry.getKey()).size() == 0) {
                listToDelete.add(entry.getKey());
            }
        }
        for (final Cell cell : listToDelete) {
            scoreMap.remove(cell);
        }
        return scoreMap;
    }

    /**
     * Функция поиска всех фишек противника
     *
     * @param turnOrder - цвет, который совершает ход в данный момент
     * @return возвращает список клеток, в которых расположены фишки противника
     */
    private List<Cell> findOpponentsChips(final Color turnOrder) {
        final ArrayList<Cell> listOfWhiteOrBlackChips = new ArrayList<>();
        final Color findColor = turnOrder.reverseColor();
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (findColor == board[i][j].getColor()) {
                    listOfWhiteOrBlackChips.add(new Cell(i, j));
                }
            }
        }
        return listOfWhiteOrBlackChips;
    }

    /**
     * Функция поиска пустых клеток вокруг фишек противника
     *
     * @param listOfOpponentsChips - список клеток, в которых расположены фишки противника
     * @return возвращает Map: ключ - клетка противника; значение - список пустых клеток вокруг
     */
    private Map<Cell, List<Cell>> findNeighborhood(final List<Cell> listOfOpponentsChips) {
        final Map<Cell, List<Cell>> neighborhood = new HashMap<>();
        for (final Cell listOfWhiteOrBlackChip : listOfOpponentsChips) {
            final List<Cell> tempList = new ArrayList<>();
            for (int j = -1; j < 2; j++) {
                for (int k = -1; k < 2; k++) {
                    if (j + listOfWhiteOrBlackChip.getX() >= 0 && j + listOfWhiteOrBlackChip.getX() < boardSize &&
                            k + listOfWhiteOrBlackChip.getY() >= 0 && k + listOfWhiteOrBlackChip.getY() < boardSize) {
                        final Chip chip = board[j + listOfWhiteOrBlackChip.getX()][k + listOfWhiteOrBlackChip.getY()];
                        if (chip.getColor() == Color.NEUTRAL) {
                            final Cell tempCell = new Cell(j + listOfWhiteOrBlackChip.getX(),
                                    k + listOfWhiteOrBlackChip.getY());
                            tempList.add(tempCell);
                        }
                    }
                }
            }
            neighborhood.put(listOfWhiteOrBlackChip, tempList);
        }
        return neighborhood;
    }

    /**
     * Функция поиска всех клеток, которые будут перевёрнуты в одном напралении
     *
     * @param neighbourCell - клетка, в сторону которой будет искаться возможность сделать ход
     * @param mainCell      - клетка, из которой ищется возможность сделать ход
     * @param turnOrder     - цвет, который совершает ход в данный момент
     * @return возвращается список клеток одного конкретного направления, которые могут быть перевёрнуты
     */
    private List<Cell> getListOfFlipCells(final Cell neighbourCell, final Cell mainCell, final Color turnOrder) {
        if (neighbourCell.equals(mainCell)) {
            return new ArrayList<>();
        }
        final int differenceX = mainCell.getX() - neighbourCell.getX();
        final int differenceY = mainCell.getY() - neighbourCell.getY();
        final List<Cell> cells = new ArrayList<>();

        int neighbourX = neighbourCell.getX();
        int neighbourY = neighbourCell.getY();

        while (true) {
            neighbourX += differenceX;
            neighbourY += differenceY;
            if (neighbourX >= boardSize || neighbourX < 0 || neighbourY >= boardSize || neighbourY < 0) {
                return new ArrayList<>();
            }
            if (getChipColor(neighbourX, neighbourY) == Color.NEUTRAL) {
                return new ArrayList<>();
            }
            if (getChipColor(neighbourX, neighbourY) == turnOrder.reverseColor()) {
                cells.add(new Cell(neighbourX, neighbourY));
            }
            if (getChipColor(neighbourX, neighbourY) == turnOrder) {
                return cells;
            }
        }
    }

    @Override
    public String toString() {
        final StringBuilder b = new StringBuilder();
        for (int i = 0; i < boardSize; i++) {
            b.append(BoardColor.PURPLE.getColor()).append(" ").append(i).append(" ").append(
                    BoardColor.RESET.getColor());
        }
        b.append("\n");

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j].getColor() == Color.BLACK) {
                    b.append(BoardColor.RED.getColor()).append(" B ").append(BoardColor.RESET.getColor());
                }
                if (board[i][j].getColor() == Color.WHITE) {
                    b.append(BoardColor.YELLOW.getColor()).append(" W ").append(BoardColor.RESET.getColor());
                }
                if (board[i][j].getColor() == Color.NEUTRAL) {
                    b.append(BoardColor.CYAN.getColor()).append(" . ").append(BoardColor.RESET.getColor());
                }
            }
            b.append(BoardColor.PURPLE.getColor()).append(" ").append(i).append("\n").append(
                    BoardColor.RESET.getColor());
        }
        return b.toString();
    }
}
