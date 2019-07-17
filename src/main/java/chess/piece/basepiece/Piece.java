package chess.piece.basepiece;

import chess.board.Board;
import chess.misc.ChessException;
import chess.misc.Position;

import java.util.List;
import java.util.Objects;

abstract public class Piece {
    protected PieceType type;
    protected PieceColor color;
    protected Position position;
    protected boolean hasMoved;

    public Piece (PieceType type, PieceColor color, Position position) {
        if (color == PieceColor.NO_COLOR ^ type != PieceType.NO_PIECE) {
            throw new ChessException("The combination " + type + " with color " + color + " is not valid. NO_PIECE and NO_COLOR must be used together!");
        }

        this.type = type;
        this.color = color;
        this.position = position;
    }


    abstract public List<Position> getPossiblePositions (Board board);

    public void onMoved (Position newPosition) {
        if (!newPosition.equals(position)) {
            hasMoved = true;
            position = newPosition;
        }
    }

    public PieceType getType () {
        return type;
    }

    public PieceColor getColor () {
        return color;
    }

    public Position getPosition () {
        return position;
    }

    public int getForwardDirection () {
        switch (color) {
            case WHITE:
                return 1;
            case BLACK:
                return -1;
            default:
                throw new ChessException("Not applicable!");
        }
    }

    @Override
    public String toString () {
        return "Piece{" +
                "type=" + type +
                ", color=" + color +
                ", position=" + position +
                '}';
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Piece piece = (Piece) o;
        return type == piece.type &&
                color == piece.color &&
                position.equals(piece.position);
    }


    @Override
    public int hashCode () {
        return Objects.hash(type, color, position);
    }

}
