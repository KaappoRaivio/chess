package chess.piece;

import chess.board.Board;
import chess.misc.exceptions.ChessException;
import chess.misc.Position;
import chess.move.EnPassantMove;
import chess.move.Move;
import chess.move.NormalMove;
import chess.piece.basepiece.Piece;
import chess.piece.basepiece.PieceColor;
import chess.piece.basepiece.PieceType;

import java.util.LinkedHashSet;
import java.util.Set;

public class Pawn extends Piece {
    public Pawn (PieceColor color) {
        super(PieceType.PAWN, color, color == PieceColor.WHITE ? "♙" : "♟", 100);
    }

    @Override
    public Set<Move> getPossibleMoves (Board board, Position position, Move lastMove) {
        Set<Move> moves = new LinkedHashSet<>();

        moves.addAll(handleStraightAhead(board, position));
        moves.addAll(handleCapture(board, position));
        moves.addAll(handleEnPassant(board, position, lastMove));

        return moves;
    }

    @Override
    protected int[][] getPieceSquareTable () {
        return new int[][]{
                {0,  0,  0,  0,  0,  0,  0,  0},
                {5, 10, 10,-20,-20, 10, 10,  5},
                {5, -5,-10,  0,  0,-10, -5,  5},
                {0,  0,  0, 20, 20,  0,  0,  0},
                {5,  5, 10, 25, 25, 10,  5,  5},
                {10, 10, 20, 30, 30, 20, 10, 10},
                {50, 50, 50, 50, 50, 50, 50, 50},
                {0,  0,  0,  0,  0,  0,  0,  0}
        };
    }

    private Set<Move> handleStraightAhead (Board board, Position position) {
        Set<Move> moves = new LinkedHashSet<>();
        try {
            if (board.isSquareEmpty(position.offsetY(getForwardDirection()))) {
                moves.add(new NormalMove(position, position.offsetY(getForwardDirection()), board));

                if (!hasMoved(position) && board.isSquareEmpty(position.offsetY(getForwardDirection() * 2))) {
                    moves.add(new NormalMove(position, position.offsetY(getForwardDirection() * 2), board));
                }

            }
        } catch (ChessException ignored) {}

        return moves;
    }

    private boolean hasMoved (Position position) {
        return color == PieceColor.WHITE ? position.getY() != 1 : position.getY() != 6;
    }

    private Set<Move> handleCapture (Board board, Position position) {
        Set<Move> moves = new LinkedHashSet<>();

        try {
            if (board.getPieceInSquare(position.offset(1, getForwardDirection())).getColor() == color.invert()) {
                moves.add(new NormalMove(position, position.offset(1, getForwardDirection()), board));
            }
        } catch (ChessException ignored) {}

        try {
            if (board.getPieceInSquare(position.offset(-1, getForwardDirection())).getColor() == color.invert()) {
                moves.add(new NormalMove(position, position.offset(-1, getForwardDirection()), board));
            }
        } catch (ChessException ignored) {}

        return moves;
    }

    private Set<Move> handleEnPassant (Board board, Position position, Move lastMove) {
        Set<Move> moves = new LinkedHashSet<>();

        try {
            Piece piece = board.getPieceInSquare(position.offsetX(1));
            if (piece instanceof Pawn && isEnPassantLegal(lastMove) && piece.getColor() == color.invert()) {
                if (board.isSquareEmpty(position.offset(1, getForwardDirection()))) {
                    moves.add(new EnPassantMove(position, position.offset(1, getForwardDirection()), board));
                }
            }
        } catch (ChessException ignored) {}

        try {
            Piece piece = board.getPieceInSquare(position.offsetX(-1));
            if (piece instanceof Pawn && isEnPassantLegal(lastMove) && piece.getColor() == color.invert()) {
                if (board.isSquareEmpty(position.offset(-1, getForwardDirection()))) {
                    moves.add(new EnPassantMove(position, position.offset(-1, getForwardDirection()), board));
                }
            }
        } catch (ChessException ignored) {}

        return moves;
    }

    private static boolean isEnPassantLegal (Move lastMove) {
        if (lastMove instanceof NormalMove) {
            NormalMove move = (NormalMove) lastMove;
            return Math.abs(move.getOrigin().getY() - move.getDestination().getY()) == 2;

        } else {
            return false;
        }
    }

    @Override
    public int getIndex (Board board, Position position, Move lastMove) {
        if (handleEnPassant(board, position, lastMove).size() > 0) {
            return getColor() == PieceColor.WHITE ? 12 : 13;
        } else {
            return super.getIndex(board, position, lastMove);
        }
    }
}
