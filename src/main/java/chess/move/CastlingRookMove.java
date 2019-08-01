package chess.move;

import chess.board.Board;
import chess.misc.Position;
import chess.misc.exceptions.ChessException;
import chess.piece.CastlingRook;
import chess.piece.Rook;
import chess.piece.basepiece.Piece;

public class CastlingRookMove extends NormalMove {
    public CastlingRookMove (Position origin, Position destination, Board board) {
        super(origin, destination, board);

        if (!(board.getPieceInSquare(origin) instanceof CastlingRook)) {
            throw new ChessException("CastlingRookMove can't be used for piece type " + board.getPieceInSquare(origin) + "!");
        }
    }

    @Override
    public void makeMove (Piece[][] buffer) {
        super.makeMove(buffer);

        buffer[getDestination().getY()][getDestination().getX()] = new Rook(color);
    }

    @Override
    public void unmakeMove (Piece[][] buffer) {
        super.unmakeMove(buffer);

        buffer[getOrigin().getY()][getOrigin().getX()] = new CastlingRook(color);
    }
}
