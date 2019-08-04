package chess.piece;

import chess.board.Board;
import chess.misc.Position;
import chess.move.Move;
import chess.move.NormalMove;
import chess.piece.basepiece.Piece;
import chess.piece.basepiece.PieceColor;
import chess.piece.basepiece.PieceType;

import java.util.LinkedHashSet;
import java.util.Set;

public class Rook extends Piece {
    public Rook (PieceColor color) {
        this(color, color == PieceColor.WHITE ? "♖" : "♜");
    }

    Rook (PieceColor color, String symbol) {
        super(PieceType.ROOK, color, symbol, 500);
    }

    @Override
    public Set<Move> getPossibleMoves (Board board, Position position, Move lastMove) {
        Set<Move> moves = new LinkedHashSet<>();

        moves.addAll(handleUp(board, position));
        moves.addAll(handleDown(board, position));
        moves.addAll(handleLeft(board, position));
        moves.addAll(handleRight(board, position));

        return moves;
    }

    @Override
    protected double[][] getPieceSquareTable () {
        return new double[][]{
                {-0.33, -0.33, -0.33, 0.33, 0.33, -0.33, -0.33, -0.33},
                {-1.0, -0.33, -0.33, -0.33, -0.33, -0.33, -0.33, -1.0},
                {-1.0, -0.33, -0.33, -0.33, -0.33, -0.33, -0.33, -1.0},
                {-1.0, -0.33, -0.33, -0.33, -0.33, -0.33, -0.33, -1.0},
                {-1.0, -0.33, -0.33, -0.33, -0.33, -0.33, -0.33, -1.0},
                {-1.0, -0.33, -0.33, -0.33, -0.33, -0.33, -0.33, -1.0},
                {0.33, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.33},
                {-0.33, -0.33, -0.33, -0.33, -0.33, -0.33, -0.33, -0.33},

        };
    }

    private Set<Move> handleUp (Board board, Position position) {
        Set<Move> moves = new LinkedHashSet<>();

        for (Position pos = position.offsetY(1, false); pos.getY() < board.getDimY(); pos = pos.offsetY(1, false)) {
            Piece piece = board.getPieceInSquare(pos);

            if (piece.getColor() == color) {
                break;
            } else if (piece.getColor() == color.invert()) {
                moves.add(new NormalMove(position, pos, board));
                break;
            } else {
                moves.add(new NormalMove(position, pos, board));
            }
        }

        return moves;
    }

    private Set<Move> handleDown (Board board, Position position) {
        Set<Move> moves = new LinkedHashSet<>();

        for (Position newPosition = position.offsetY(-1, false); newPosition.getY() >= 0; newPosition = newPosition.offsetY(-1, false)) {
            Piece piece = board.getPieceInSquare(newPosition);

            if (piece.getColor() == color) {
                break;
            } else if (piece.getColor() == color.invert()) {
                moves.add(new NormalMove(position, newPosition, board));
                break;
            } else {
                moves.add(new NormalMove(position, newPosition, board));
            }
        }

        return moves;
    }

    private Set<Move> handleLeft (Board board, Position position) {
        Set<Move> moves = new LinkedHashSet<>();

        for (Position newPosition = position.offsetX(1, false); newPosition.getX() < board.getDimX(); newPosition = newPosition.offsetX(1, false)) {
            Piece piece = board.getPieceInSquare(newPosition);

            if (piece.getColor() == color) {
                break;
            } else if (piece.getColor() == color.invert()) {
                moves.add(new NormalMove(position, newPosition, board));
                break;
            } else {
                moves.add(new NormalMove(position, newPosition, board));
            }
        }

        return moves;
    }

    private Set<Move> handleRight (Board board, Position position) {
        Set<Move> moves = new LinkedHashSet<>();

        for (Position newPosition = position.offsetX(-1, false); newPosition.getX() >= 0; newPosition = newPosition.offsetX(-1, false)) {
            Piece piece = board.getPieceInSquare(newPosition);

            if (piece.getColor() == color) {
                break;
            } else if (piece.getColor() == color.invert()) {
                moves.add(new NormalMove(position, newPosition, board));
                break;
            } else {
                moves.add(new NormalMove(position, newPosition, board));
            }
        }

        return moves;
    }
}
