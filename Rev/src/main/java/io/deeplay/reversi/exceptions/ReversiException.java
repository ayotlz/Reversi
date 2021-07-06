package io.deeplay.reversi.exceptions;

public class ReversiException extends Exception {
    private final ReversiErrorCode errorCode;

    public ReversiException(ReversiErrorCode errorCode) {
        super(errorCode.getErrorString());
        this.errorCode = errorCode;
    }

    public ReversiErrorCode getReversiErrorCode() {
        return errorCode;
    }
}
