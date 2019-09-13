package chess.piece.basepiece;

import java.io.Serializable;

public enum PieceType implements Serializable {
    NO_PIECE (-1),
    PAWN (0),
    KNIGHT (2),
    BISHOP (4),
    ROOK (6),
    QUEEN (8),
    KING (10);

    private int ordinal;

    PieceType (int ordinal) {
        this.ordinal = ordinal;
    }

    public int getOrdinal () {
        return ordinal;
    }
}
