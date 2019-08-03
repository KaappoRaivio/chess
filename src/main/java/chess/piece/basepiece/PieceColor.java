package chess.piece.basepiece;

import chess.misc.exceptions.ChessException;

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
//                throw new RuntimeException("Cannot invert NO_COLOR!");
//                System.out.println("Inverting no_color!");
                return NO_COLOR;
        }
    }
}
