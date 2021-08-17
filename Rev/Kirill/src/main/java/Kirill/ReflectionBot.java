package Kirill;

import models.board.Board;
import models.board.Cell;
import models.chip.Color;
import player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс ReflectionBot - класс игрока расширяюшего Player, работающего по принципу на каждом ходу
 * захватывать наибольшее количество дисков
 */
public final class ReflectionBot extends Player {

    public ReflectionBot(final Color color) {
        super(color);
        setName("KirillReflectionMaxBot");
    }

    /**
     * Функция, которая запращивает у бота ответ
     *
     * @param board - доска
     * @return возвращается ответ от игрока
     */
    @Override
    public final Cell getAnswer(final Board board) {

        final Map<Cell, Integer> utilityMap = new HashMap<>(getUtilityMap(board));

        final Cell answerCell;

        Cell tempCell = null;

        int tempInt = 0;

        for (final Cell cell : utilityMap.keySet()) {
            if (utilityMap.get(cell) > tempInt) {
                tempInt = utilityMap.get(cell);
                tempCell = cell;
            }
        }
        answerCell = tempCell;

        return answerCell;
    }

    private Map<Cell, Integer> getUtilityMap(final Board board) {
        //Клетки в которые можно походить и список которые перевернутся
        final Map<Cell, List<Cell>> scoreMap = board.getScoreMap(getPlayerColor());

        //Клетки в которые можно походить
        final List<Cell> keys = new ArrayList<>(scoreMap.keySet());

        final Map<Cell, Integer> utility = new HashMap<>();

        for (int i = 0; i < scoreMap.keySet().size(); i++) {
            utility.put(keys.get(i), scoreMap.get(keys.get(i)).size());
        }
        return utility;
    }
}
