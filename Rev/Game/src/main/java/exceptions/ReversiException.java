package exceptions;

public class ReversiException extends Exception {
    public ReversiException(final ReversiErrorCode errorCode) {
        super(errorCode.getErrorString());
    }
}
