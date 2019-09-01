package chess.move;

import chess.board.Board;
import chess.misc.Position;
import chess.misc.exceptions.ChessException;
import chess.piece.King;
import chess.piece.NoPiece;
import chess.piece.Rook;
import chess.piece.basepiece.Piece;
import chess.piece.basepiece.PieceColor;
import misc.Pair;

import java.util.Objects;

import static chess.piece.basepiece.PieceColor.WHITE;

public class CastlingMove implements Move {
    final private CastlingType castlingType;
    final private PieceColor color;
    final private Position kingPosition;
    final private Board board;

    private CastlingRookMove castlingRookMove;
    private CastlingKingMove castlingKingMove;


    public CastlingMove (CastlingType castlingType, PieceColor kingColor, Board board) {
        this.castlingType = castlingType;
        this.color = kingColor;
        this.kingPosition = color == WHITE ? board.getStateHistory().getCurrentState().getWhiteKingPosition() : board.getStateHistory().getCurrentState().getBlackKingPosition();
        this.board = board;

        prepareMoves();


    }

    private void prepareMoves () {
        final int kingDestinationOffsetX;
        final int rookDestinationOffsetX;
        final int rookPositionOffsetX;


        switch (castlingType) {
            case KING_SIDE:
                kingDestinationOffsetX = 2;
                rookDestinationOffsetX = 1;
                rookPositionOffsetX = 3;
                break;
            case QUEEN_SIDE:
                kingDestinationOffsetX = -2;
                rookDestinationOffsetX = -1;
                rookPositionOffsetX = -4;
                break;
            default:
                throw new RuntimeException("Shouldn't happen");
        }

        Position kingDestination = kingPosition.offsetX(kingDestinationOffsetX);

        Position rookOrigin = kingPosition.offsetX(rookPositionOffsetX);
        Position rookDestination = kingPosition.offsetX(rookDestinationOffsetX);

        castlingKingMove = new CastlingKingMove(kingPosition, kingDestination, board);
        castlingRookMove = new CastlingRookMove(rookOrigin, rookDestination, board);
    }

    @Override
    public void makeMove (Piece[][] buffer) {
        castlingKingMove.makeMove(buffer);
        castlingRookMove.makeMove(buffer);
    }

    @Override
    public void unmakeMove (Piece[][] buffer) {
        castlingKingMove.unmakeMove(buffer);
        castlingRookMove.unmakeMove(buffer);
    }

    @Override
    public boolean resetsFiftyMoveRule () {
        return false;
    }

    @Override
    public boolean affectsKingPosition () {
        return true;
    }

    @Override
    public Pair<PieceColor, Position> getNewKingPosition () {

        return new Pair<>(color, castlingKingMove.getDestination());
    }

    @Override
    public Position getOrigin () {
        return kingPosition;
    }

    @Override
    public PieceColor getColor () {
        return color;
    }

    @Override
    public boolean isEnpassantMove () {
        return false;
    }

    @Override
    public String toString () {
        switch (castlingType) {
            case QUEEN_SIDE:
                return "O-O-O";
            case KING_SIDE:
                return "O-O";
        }

        return "";
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof CastlingMove)) return false;
        CastlingMove that = (CastlingMove) o;
        return castlingType == that.castlingType &&
                getColor() == that.getColor() &&
                kingPosition.equals(that.kingPosition) &&
                board.equals(that.board) &&
                castlingRookMove.equals(that.castlingRookMove) &&
                castlingKingMove.equals(that.castlingKingMove);
    }

    @Override
    public int hashCode () {
        return Objects.hash(castlingType, getColor(), kingPosition, board, castlingRookMove, castlingKingMove);
    }
}
