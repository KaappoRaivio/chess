package chess.piece.basepiece;

import chess.misc.ChessException;

import java.io.Serializable;

public enum PieceColor implements Serializable {
    BLACK, WHITE, NO_COLOR;

    public PieceColor invert () {
        switch (this) {
            case BLACK:
                return WHITE;
            case WHITE:
                return BLACK;
            default:
//                throw new ChessException("Cannot invert NO_COLOR!");
                return NO_COLOR;
        }
    }
}
