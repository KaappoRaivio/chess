package chess.move;

import chess.board.Board;
import chess.misc.Position;
import chess.piece.NoPiece;
import chess.piece.Pawn;
import chess.piece.basepiece.Piece;

public class EnPassantMove extends NormalMove {
    public EnPassantMove (Position origin, Position destination, Board board) {
        super(origin, destination, board);
    }

    @Override
    public void makeMove (Piece[][] buffer) {
        super.makeMove(buffer);

        int offsetX = getDestination().getX() - getOrigin().getX();
        Position opponentPawnPosition = getOrigin().offsetX(offsetX);

        buffer[opponentPawnPosition.getY()][opponentPawnPosition.getX()] = new NoPiece();
    }

    @Override
    public void unmakeMove (Piece[][] buffer) {
        super.unmakeMove(buffer);

        int offsetX = getDestination().getX() - getOrigin().getX();
        Position opponentPawnPosition = getOrigin().offsetX(offsetX);

        buffer[opponentPawnPosition.getY()][opponentPawnPosition.getX()] = new Pawn(color.invert());
    }
}
