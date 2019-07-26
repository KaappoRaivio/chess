package chess.misc.exceptions;

public class CheckmateException extends GameEndException {
    public CheckmateException () {
        super("Checkmate!");
    }
}
