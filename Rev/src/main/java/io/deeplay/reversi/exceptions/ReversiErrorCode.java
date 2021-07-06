package io.deeplay.reversi.exceptions;

public enum ReversiErrorCode {
    COLOR_IS_NULL("Color is null"),
    ODD_SIZE_BOARD("Board's size is odd number");

    private final String errorString;

    private ReversiErrorCode(String errorString) {
        this.errorString = errorString;
    }

    public String getErrorString() {
        return errorString;
    }
}
