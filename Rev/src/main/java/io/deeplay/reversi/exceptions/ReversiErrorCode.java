package io.deeplay.reversi.exceptions;

public enum ReversiErrorCode {
    OUT_OF_BOARD("Cell is not correct"),
    CELL_IS_NULL("Cell is null"),
    BOARD_IS_NULL("Board is null"),
    COLOR_IS_NULL("Color is null"),
    ODD_SIZE_BOARD("Board's size is odd number"),
    CELLS_ARE_EQUALS("NeighbourCell and MainCell are equals"),
    CELL_IS_INCORRECT("Cell has incorrect fields"),
    INCORRECT_PLACE_FOR_CHIP("The chip cannot be placed in this cell");

    private final String errorString;

    ReversiErrorCode(String errorString) {
        this.errorString = errorString;
    }

    public String getErrorString() {
        return errorString;
    }
}
