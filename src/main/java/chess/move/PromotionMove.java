package chess.move;

import chess.board.Board;
import chess.misc.Position;
import chess.misc.exceptions.ChessException;
import chess.piece.*;
import chess.piece.basepiece.Piece;
import chess.piece.basepiece.PieceType;

import java.util.Objects;

public class PromotionMove extends NormalMove {
    final private Piece promoted;

    public PromotionMove(Position origin, Position destination, Board board, PieceType promoted) {
        super(origin, destination, board);
        switch (promoted) {
            case QUEEN:
                this.promoted = new Queen(color);
                break;
            case ROOK:
                this.promoted = new Rook(color);
                break;
            case BISHOP:
                this.promoted = new Bishop(color);
                break;
            case KNIGHT:
                this.promoted = new Knight(color);
                break;
            default:
                throw new ChessException("Can't promote to " + promoted + "!");
        }
    }

    @Override
    public void makeMove(Piece[][] buffer) {
        super.makeMove(buffer);
        buffer[getDestination().getY()][getDestination().getX()] = promoted;
    }

    @Override
    public void unmakeMove(Piece[][] buffer) {
        super.unmakeMove(buffer);
        buffer[getOrigin().getY()][getOrigin().getX()] = new Pawn(color);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PromotionMove that = (PromotionMove) o;
        return promoted.equals(that.promoted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), promoted);
    }
}
