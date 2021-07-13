package io.deeplay.reversi.bot;

import io.deeplay.reversi.models.board.Board;
import io.deeplay.reversi.models.board.Cell;
import io.deeplay.reversi.models.chip.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class RandomBot extends Player {
    public RandomBot(Color color) {
        super(color);
    }

    @Override
    public Cell getAnswer(Board board) {
        final Map<Cell, List<Cell>> scoreMap = board.getScoreMap(getPlayerColor());

        Random random = new Random();
        List<Cell> keys = new ArrayList<>(scoreMap.keySet());
        Cell randomCell = keys.get(random.nextInt(keys.size()));

        return randomCell;
//        int maxScore = 0;
//        Cell cell = null;
//
//        for (Map.Entry<Cell, List<Cell>> entry : scoreMap.entrySet()) {
//            if (scoreMap.get(entry.getKey()).size() > maxScore) {
//                maxScore++;
//                cell = entry.getKey();
//            }
//        }
//
//        return cell;
    }
}
