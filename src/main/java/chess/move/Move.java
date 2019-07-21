package chess.move;

import chess.board.Board;
import chess.misc.Position;
import chess.piece.basepiece.Piece;

public class Move {
    private final Position origin;
    private final Position destination;

    private final Piece pieceUnderDestination;

    private boolean hasOriginPieceMovedBefore;
    private boolean hasDestinationPieceMovedBefore;

    public Move (String move, Board board) {
        this(Position.fromString(move.substring(0, 2)), Position.fromString(move.substring(2)), board);
    }

    public Move (Position origin, Position destination, Board board) {
        this(origin, destination, board.getPieceInSquare(destination), board.getPieceInSquare(origin).hasMoved(), board.getPieceInSquare(origin).hasMoved());
    }

    private Move (Position origin, Position destination, Piece pieceUnderDestination, boolean hasOriginPieceMovedBefore, boolean hasDestinationPieceMovedBefore) {
        this.origin = origin;
        this.destination = destination;
        this.pieceUnderDestination = pieceUnderDestination;
        this.hasOriginPieceMovedBefore = hasOriginPieceMovedBefore;
        this.hasDestinationPieceMovedBefore = hasDestinationPieceMovedBefore;
    }

    public Position getOrigin () {
        return origin;
    }

    public Position getDestination () {
        return destination;
    }

    public Piece getPieceUnderDestination () {
        return pieceUnderDestination;
    }

    public boolean isHasOriginPieceMovedBefore () {
        return hasOriginPieceMovedBefore;
    }

    public boolean isHasDestinationPieceMovedBefore () {
        return hasDestinationPieceMovedBefore;
    }
}
