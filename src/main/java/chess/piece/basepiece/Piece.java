package chess.piece.basepiece;

import chess.board.Board;
import chess.misc.ChessException;
import chess.misc.Position;

import java.util.List;
import java.util.Objects;

abstract public class Piece {
    protected final PieceType type;
    protected final PieceColor color;
    private boolean hasMoved;
    private String symbol;

    public Piece (PieceType type, PieceColor color, String symbol) {
        if (color == PieceColor.NO_COLOR ^ type == PieceType.NO_PIECE) {
            throw new ChessException("The combination " + type + " with color " + color + " is not valid. NO_PIECE and NO_COLOR must be used together!");
        }

        this.type = type;
        this.color = color;
        this.symbol = symbol;
    }


    abstract public List<Position> getPossiblePositions (Board board, Position position);

    public void onMoved (Position oldPosition, Position newPosition) {
        setHasMoved(true);
    }

    public void onAnotherPieceMoved () {}

    public PieceType getType () {
        return type;
    }

    public PieceColor getColor () {
        return color;
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
        return symbol;
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Piece piece = (Piece) o;
        return type == piece.type &&
                color == piece.color;
    }


    @Override
    public int hashCode () {
        return Objects.hash(type, color);
    }

    public boolean isHasMoved () {
        return hasMoved;
    }

    public void setHasMoved (boolean hasMoved) {
        this.hasMoved = hasMoved;
    }
}
