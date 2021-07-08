package io.deeplay.reversi.exceptions;

public enum ReversiErrorCode {
    OUT_OF_BOARD("Cell is not correct"),
    COLOR_IS_NULL("Color is null"),
    ODD_SIZE_BOARD("Board's size is odd number"),
    CELLS_ARE_EQUALS("NeighbourCell and MainCell are equals"),
    CELL_IS_INCORRECT("Cell has incorrect fields");

    private final String errorString;

    private ReversiErrorCode(String errorString) {
        this.errorString = errorString;
    }

    public String getErrorString() {
        return errorString;
    }
}
