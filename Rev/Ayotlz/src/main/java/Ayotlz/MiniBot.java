package Ayotlz;

import models.board.Board;
import models.board.Cell;
import models.chip.Color;
import player.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class MiniBot extends Player {
    public MiniBot(final Color color) {
        super(color);
        setName("AyotlzMiniBot");
    }

    @Override
    public final Cell getAnswer(final Board board) {
        final Map<Cell, List<Cell>> scoreMap = board.getScoreMap(getPlayerColor());
        int minValue = Integer.MAX_VALUE;
        Cell answer = null;

        final Map<Cell, List<Cell>> copyMap = new HashMap<>(scoreMap);
        final Set<Cell> cellSet = copyMap.keySet();
        deleteAboutCorner(cellSet);

        if (cellSet.size() > 0) {
            scoreMap.keySet().removeIf(k -> !cellSet.contains(k));
        }

        for (final Map.Entry<Cell, List<Cell>> entry : scoreMap.entrySet()) {
            if (entry.getKey().equals(new Cell(0, 0)) ||
                    entry.getKey().equals(new Cell(0, board.getBoardSize() - 1)) ||
                    entry.getKey().equals(new Cell(board.getBoardSize() - 1, 0)) ||
                    entry.getKey().equals(new Cell(board.getBoardSize() - 1, board.getBoardSize() - 1))) {
                return entry.getKey();
            }
            if (entry.getValue().size() < minValue) {
                minValue = entry.getValue().size();
                answer = entry.getKey();
            }
        }
        return answer;
    }

    public final void deleteAboutCorner(final Set<Cell> cellSet) {
        cellSet.removeIf(this::isCornerCell);
    }

    public final boolean isCornerCell(final Cell cell) {
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
