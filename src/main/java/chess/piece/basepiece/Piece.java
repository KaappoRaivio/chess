package chess.piece.basepiece;

import chess.board.Board;
import chess.misc.Position;
import chess.misc.exceptions.ChessException;
import chess.move.Move;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

abstract public class Piece implements Serializable {
    final private PieceType type;
    final protected PieceColor color;
    final private String symbol;
    final private int value;

    public Piece (PieceType type, PieceColor color, String symbol, int value) {
        if (color == PieceColor.NO_COLOR ^ type == PieceType.NO_PIECE) {
            throw new ChessException("The combination " + type + " with color " + color + " is not valid. NO_PIECE and NO_COLOR must be used together!");
        }

        this.type = type;
        this.color = color;
        this.symbol = symbol;
        this.value = value;
    }


    abstract public Set<Move> getPossibleMoves (Board board, Position position, Move lastMove);
    protected abstract int[][] getPieceSquareTable ();

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

    public int getIndex (Board board, Position position, Move lastMove) {
        return getColor() == PieceColor.WHITE ? getType().ordinal(): getType().ordinal() + 1;
    }

    public int getValue (Position position) {
        switch (color) {
            case NO_COLOR:
            case WHITE:
                return value * (getPieceSquareTable()[position.getY()][position.getX()]);
            case BLACK:
                return value * (getPieceSquareTable()[7 - position.getY()][position.getX()]);
            default:
                throw new ChessException("");

//        return value;
        }
    }

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
                throw new ChessException("Not applicable! " + toString());
        }
    }
}
