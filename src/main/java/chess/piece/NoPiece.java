package chess.piece;

import chess.board.Board;
import chess.misc.ChessException;
import chess.misc.Position;
import chess.move.Move;
import chess.piece.basepiece.Piece;
import chess.piece.basepiece.PieceColor;
import chess.piece.basepiece.PieceType;

import java.util.Collections;
import java.util.Set;

public class NoPiece extends Piece {
    public NoPiece () {
        super(PieceType.NO_PIECE, PieceColor.NO_COLOR, ".", 0);
    }

    @Override
    public Set<Position> getPossiblePositions (Board board, Position position) {
        return Collections.emptySet();
    }

    @Override
    public void onMoved (Move move, Board board) {
        throw new ChessException("NoPiece cannot be moved from " + move.getOrigin() + " to " + move.getDestination() + "!");
    }

    @Override
    public boolean equals (Object o) {
        try {
            return o.getClass().equals(getClass());
        } catch (NullPointerException e) {
            return false;
        }
    }

    @Override
    public Piece deepCopy () {
        return this; // no need to deepcopy
    }
}
