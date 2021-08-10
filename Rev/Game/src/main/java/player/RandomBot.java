package player;

import models.board.Board;
import models.board.Cell;
import models.chip.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Класс RandomBot - класс случайного игрока расширяюшего Player
 */
public class RandomBot extends Player {
    public RandomBot(final Color color) {
        super(color);
        setName("RandomBot");
    }

    /**
     * Функция, которая запращивает у бота ответ
     *
     * @param board - доска
     * @return возвращается ответ от игрока
     */
    @Override
    public final Cell getAnswer(final Board board) {
        final Map<Cell, List<Cell>> scoreMap = board.getScoreMap(getPlayerColor());

        final Random random = new Random();
        final List<Cell> keys = new ArrayList<>(scoreMap.keySet());
        return keys.get(random.nextInt(keys.size()));
    }
}
