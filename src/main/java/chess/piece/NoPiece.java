package chess.piece;

import chess.board.Board;
import chess.misc.ChessException;
import chess.misc.Position;
import chess.piece.basepiece.Piece;
import chess.piece.basepiece.PieceColor;
import chess.piece.basepiece.PieceType;

import java.util.Collections;
import java.util.List;

public class NoPiece extends Piece {
    public NoPiece () {
        super(PieceType.NO_PIECE, PieceColor.NO_COLOR, ".");
    }

    @Override
    public List<Position> getPossiblePositions (Board board, Position position) {
        return Collections.emptyList();
    }

    @Override
    public void onMoved (Position oldPosition, Position newPosition) {
        throw new ChessException("NoPiece cannot be moved from " + oldPosition + " to " + newPosition + "!");
    }

    @Override
    public boolean equals (Object o) {
        return o.getClass() == getClass();
    }

    @Override
    public int hashCode () {
        return super.hashCode();
    }
}
