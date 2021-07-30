package io.deeplay.reversi.player;

import io.deeplay.reversi.models.board.Board;
import io.deeplay.reversi.models.board.Cell;
import io.deeplay.reversi.models.chip.Color;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class MaxiBot extends Player{
    public MaxiBot(final Color color) {
        super(color);
    }

    @Override
    public Cell getAnswer(Board board) throws IOException {
        final Map<Cell, List<Cell>> scoreMap = board.getScoreMap(getPlayerColor());
        int maxValue = 0;
        Cell answer = null;
        for (Map.Entry<Cell, List<Cell>> entry : scoreMap.entrySet()) {
            if (entry.getKey().equals(new Cell(0, 0)) ||
                    entry.getKey().equals(new Cell(0, board.getBoardSize() - 1)) ||
                    entry.getKey().equals(new Cell(board.getBoardSize() - 1, 0)) ||
                    entry.getKey().equals(new Cell(board.getBoardSize() - 1, board.getBoardSize() - 1))) {
                return entry.getKey();
            }
            if (entry.getValue().size() > maxValue) {
                maxValue = entry.getValue().size();
                answer = entry.getKey();
            }
        }
        return answer;
    }
}
