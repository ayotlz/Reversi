package Kirill.bots;

import Kirill.utilityFunctions.IFunction;
import Kirill.utilityFunctions.SimpleScoreFunction;
import models.board.Board;
import models.board.Cell;
import models.chip.Color;
import player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс OneSimulationBot - класс игрока расширяюшего Player
 */
public final class OneSimulationBot extends Player {

    private IFunction utilityFunction = new SimpleScoreFunction();

    public OneSimulationBot(final Color color) {
        super(color);
        setName("KirillMiniMaxBot " + utilityFunction.toString());
    }

    public OneSimulationBot(final Color color, final IFunction function) {
        super(color);
        this.utilityFunction = function;
        setName("KirillMiniMaxBot " + utilityFunction.toString());
    }

    /**
     * Функция, которая запращивает у бота ответ
     *
     * @param board - доска
     * @return возвращается ответ от игрока
     */
    @Override
    public final Cell getAnswer(final Board board) {

        final Map<Cell, Double> utilityMap = new HashMap<>(getUtilityMap(board));

        Map.Entry<Cell, Double> entry = utilityMap.entrySet().iterator().next();
        Cell tempCell = entry.getKey();
        double tempDouble = entry.getValue();

        final Cell answerCell;

        for (final Cell cell : utilityMap.keySet()) {
            if (utilityMap.get(cell) > tempDouble) {
                tempDouble = utilityMap.get(cell);
                tempCell = cell;
            }
        }
        answerCell = tempCell;

        return answerCell;
    }

    // Мапа содержащая ячейки и полезность
    private Map<Cell, Double> getUtilityMap(final Board board) {

        //Клетки в которые можно походить и список которые перевернутся
        final Map<Cell, List<Cell>> scoreMap = board.getScoreMap(getPlayerColor());

        //Клетки в которые можно походить
        final List<Cell> keys = new ArrayList<>(scoreMap.keySet());
        final Map<Cell, Double> utility = new HashMap<>();

        for (final Cell key : keys) {
            final Board copyBoard = new Board(board);
            copyBoard.setChip(key.getX(), key.getY(), getPlayerColor());
            for (final Cell cell : scoreMap.get(key)) {
                copyBoard.reverseChip(cell.getX(), cell.getY());
            }
            utility.put(key, getWinScore(copyBoard));
        }
        return utility;
    }

    private double getWinScore(final Board board) {
        return utilityFunction.getScore(board, getPlayerColor()) - utilityFunction.getScore(board, getPlayerColor().reverseColor());
    }
}
