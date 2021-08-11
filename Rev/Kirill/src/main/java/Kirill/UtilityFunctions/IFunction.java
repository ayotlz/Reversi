package Kirill.UtilityFunctions;

import models.board.Board;
import models.chip.Color;

public interface IFunction {
     double getScore(final Board board, final Color color);
}
