package io.deeplay.reversi.Kirill.UtilityFunctions;

import io.deeplay.reversi.models.board.Board;
import io.deeplay.reversi.models.chip.Color;

public interface IFunctions {
     double getScore(final Board board, final Color color);
}
