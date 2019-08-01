package chess.move;

import chess.board.Board;
import chess.misc.Position;
import chess.piece.CastlingKing;
import chess.piece.CastlingRook;
import chess.piece.King;
import chess.piece.Rook;
import chess.piece.basepiece.Piece;

public class CastlingKingMove extends NormalMove {
    public CastlingKingMove (Position origin, Position destination, Board board) {
        super(origin, destination, board);
    }

    @Override
    public void makeMove (Piece[][] buffer) {
        super.makeMove(buffer);
        buffer[getDestination().getY()][getDestination().getX()] = new King(color);
    }

    @Override
    public void unmakeMove (Piece[][] buffer) {
        super.unmakeMove(buffer);
        buffer[getOrigin().getY()][getOrigin().getX()] = new CastlingKing(color);
    }
}
