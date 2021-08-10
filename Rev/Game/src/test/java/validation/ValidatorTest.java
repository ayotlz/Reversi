package validation;

import exceptions.ReversiException;
import models.board.Board;
import models.board.Cell;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ValidatorTest {
    @Test
    public void testCorrectCell() throws ReversiException {
        assertThrows(ReversiException.class, () -> Validator.isCellCorrect(new Cell(-1, -1), 8));
        assertThrows(ReversiException.class, () -> Validator.isCellCorrect(new Cell(9, 0), 8));
        assertThrows(ReversiException.class, () -> Validator.isCellCorrect(new Cell(0, 88), 8));
        assertThrows(ReversiException.class, () -> Validator.isCellCorrect(null, 8));

        Validator.isCellCorrect(new Cell(1, 1), 8);
        Validator.isCellCorrect(new Cell(8, 8), 10);
    }

    @Test
    public void testCorrectBoard() throws ReversiException {
        assertThrows(ReversiException.class, () -> Validator.isBoardCorrect(null));
        Validator.isBoardCorrect(new Board());
    }
}
