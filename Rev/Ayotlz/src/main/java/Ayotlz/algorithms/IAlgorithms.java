package Ayotlz.algorithms;

import models.board.Board;
import models.board.Cell;
import models.chip.Color;

public interface IAlgorithms {
    Cell getCell(final Board board, final Color playerColor);
}
