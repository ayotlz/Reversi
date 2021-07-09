package io.deeplay.reversi.exceptions;

public class ReversiException extends Exception {
    public ReversiException(ReversiErrorCode errorCode) {
        super(errorCode.getErrorString());
    }
}
