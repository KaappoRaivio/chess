package chess.piece.basepiece;

import chess.misc.ChessException;

public enum PieceColor {
    BLACK, WHITE, NO_COLOR;

    public PieceColor invert () {
        switch (this) {
            case BLACK:
                return WHITE;
            case WHITE:
                return BLACK;
            default:
                throw new ChessException("Cannot invert NO_COLOR!");
        }
    }
}
