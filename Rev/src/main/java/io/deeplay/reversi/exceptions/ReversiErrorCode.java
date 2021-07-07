package io.deeplay.reversi.exceptions;

public enum ReversiErrorCode {
    COLOR_IS_NULL("Color is null"),
    ODD_SIZE_BOARD("Board's size is odd number"),
    CELLS_ARE_EQUALS("NeighbourCell and MainCell are equals");

    private final String errorString;

    private ReversiErrorCode(String errorString) {
        this.errorString = errorString;
    }

    public String getErrorString() {
        return errorString;
    }
}
