package chess.piece.basepiece;

import chess.misc.exceptions.ChessException;

import java.io.Serializable;

public enum PieceColor implements Serializable {
    BLACK(-1), WHITE(1), NO_COLOR(0);

    final private int value;

    PieceColor (int value) {
        this.value = value;
    }

    public PieceColor invert () {
        switch (this) {
            case BLACK:
                return WHITE;
            case WHITE:
                return BLACK;
            default:
                return NO_COLOR;
        }
    }

    public int getValue () {
        return value;
    }
}
