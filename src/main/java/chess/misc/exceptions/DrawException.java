package chess.misc.exceptions;

public class DrawException extends GameEndException {
    public DrawException () {
        super("Draw!");
    }
}
