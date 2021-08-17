package exceptions;

public final class ReversiException extends Exception {
    public ReversiException(final ReversiErrorCode errorCode) {
        super(errorCode.getErrorString());
    }
}
