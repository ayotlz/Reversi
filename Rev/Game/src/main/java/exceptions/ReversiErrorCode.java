package exceptions;

public enum ReversiErrorCode {
    OUT_OF_BOARD("Cell is not correct"),
    CELL_IS_NULL("Cell is null"),
    BOARD_IS_NULL("Board is null"),
    ODD_SIZE_BOARD("Board's size is odd number"),
    INCORRECT_PLACE_FOR_CHIP("The chip cannot be placed in this cell");

    private final String errorString;

    ReversiErrorCode(final String errorString) {
        this.errorString = errorString;
    }

    public final String getErrorString() {
        return errorString;
    }
}
