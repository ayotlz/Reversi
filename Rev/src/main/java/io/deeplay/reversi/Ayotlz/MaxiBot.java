package io.deeplay.reversi.Ayotlz;

import io.deeplay.reversi.models.board.Board;
import io.deeplay.reversi.models.board.Cell;
import io.deeplay.reversi.models.chip.Color;
import io.deeplay.reversi.player.Player;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MaxiBot extends Player {
    public MaxiBot(final Color color) {
        super(color);
    }

    @Override
    public Cell getAnswer(Board board) throws IOException {
        final Map<Cell, List<Cell>> scoreMap = board.getScoreMap(getPlayerColor());
        int maxValue = 0;
        Cell answer = null;

        Map<Cell, List<Cell>> copyMap = new HashMap<>(scoreMap);
        Set<Cell> cellSet = copyMap.keySet();
        deleteAboutCorner(cellSet);

        if (cellSet.size() > 0) {
            scoreMap.keySet().removeIf(k -> !cellSet.contains(k));
        }

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

    public void deleteAboutCorner(Set<Cell> cellSet) {
        cellSet.removeIf(this::isCornerCell);
    }

    public boolean isCornerCell(Cell cell) {
        return cell.getX() == 1 && cell.getY() == 0
                || cell.getX() == 0 && cell.getY() == 1
                || cell.getX() == 1 && cell.getY() == 1
                || cell.getX() == 0 && cell.getY() == 6
                || cell.getX() == 1 && cell.getY() == 6
                || cell.getX() == 1 && cell.getY() == 7
                || cell.getX() == 6 && cell.getY() == 0
                || cell.getX() == 6 && cell.getY() == 1
                || cell.getX() == 7 && cell.getY() == 1
                || cell.getX() == 6 && cell.getY() == 6
                || cell.getX() == 6 && cell.getY() == 7
                || cell.getX() == 7 && cell.getY() == 6;
    }
}
