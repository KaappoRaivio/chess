package chess.misc.exceptions;

public class StopException extends RuntimeException {
    public StopException(String message) {
        super(message);
    }

    public StopException() {
        super();
    }
}
