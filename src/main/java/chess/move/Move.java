package chess.move;

import chess.board.Board;
import chess.misc.Position;
import chess.piece.basepiece.Piece;

import java.io.Serializable;
import java.util.Objects;

public class Move implements Serializable {

    private final Position origin;
    private final Position destination;

    private final Piece pieceUnderDestination;

    private final boolean hasOriginPieceMovedBefore;
    private final boolean hasDestinationPieceMovedBefore;


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

    public boolean hasOriginPieceMovedBefore () {
        return hasOriginPieceMovedBefore;
    }

    public boolean hasDestinationPieceMovedBefore () {
        return hasDestinationPieceMovedBefore;
    }

    public Move deepCopy () {
        return this;
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return hasOriginPieceMovedBefore == move.hasOriginPieceMovedBefore &&
                hasDestinationPieceMovedBefore == move.hasDestinationPieceMovedBefore &&
                getOrigin().equals(move.getOrigin()) &&
                getDestination().equals(move.getDestination()) &&
                getPieceUnderDestination().equals(move.getPieceUnderDestination());
    }

    @Override
    public int hashCode () {
        return Objects.hash(getOrigin(), getDestination(), getPieceUnderDestination(), hasOriginPieceMovedBefore, hasDestinationPieceMovedBefore);
    }

    @Override
    public String toString () {
        return origin + "" + destination;
    }

}
