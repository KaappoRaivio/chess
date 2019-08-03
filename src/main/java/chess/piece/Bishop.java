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

public class Bishop extends Piece {
    public Bishop (PieceColor color) {
        super(PieceType.BISHOP, color, color == PieceColor.WHITE ? "♗" : "♝", 330);
    }

    @Override
    public Set<Move> getPossibleMoves (Board board, Position position, Move lastMove) {
        Set<Move> mvoes = new LinkedHashSet<>();

        mvoes.addAll(handleUpRight(board, position));
        mvoes.addAll(handleUpLeft(board, position));
        mvoes.addAll(handleDownLeft(board, position));
        mvoes.addAll(handleDownRight(board, position));

        return mvoes;
    }

    @Override
    protected int[][] getPieceSquareTable () {
        return new int[][]{
                {-20,-10,-10,-10,-10,-10,-10,-20},
                {-10,  5,  0,  0,  0,  0,  5,-10},
                {-10, 10, 10, 10, 10, 10, 10,-10},
                {-10,  0, 10, 10, 10, 10,  0,-10},
                {-10,  5,  5, 10, 10,  5,  5,-10},
                {-10,  0,  5, 10, 10,  5,  0,-10},
                {-10,  0,  0,  0,  0,  0,  0,-10},
                {-20,-10,-10,-10,-10,-10,-10,-20}
        };
    }

    private Set<Move> handleUpRight (Board board, Position position) {
        Set<Move> moves = new LinkedHashSet<>();
        int checkX = position.getX() + 1;
        int checkY = position.getY() + 1;

        while (checkX < board.getDimX() && checkY < board.getDimY()) {
            Position newPosition = new Position(checkX, checkY);
            Piece currentPiece = board.getPieceInSquare(newPosition);

            if (currentPiece.getColor() == color) {
                return moves;
            } else if (currentPiece.getColor() == color.invert()) {
                moves.add(new NormalMove(position, newPosition, board));
                return moves;
            } else {
                moves.add(new NormalMove(position, newPosition, board));
            }
            checkX += 1;
            checkY += 1;
        }

        return moves;
    }

    private Set<Move> handleUpLeft (Board board, Position position) {
        Set<Move> moves = new LinkedHashSet<>();
        int checkX = position.getX() - 1;
        int checkY = position.getY() + 1;

        while (checkX >= 0 && checkY < board.getDimY()) {
            Position newPosition = new Position(checkX, checkY);
            Piece currentPiece = board.getPieceInSquare(newPosition);

            if (currentPiece.getColor() == color) {
                return moves;
            } else if (currentPiece.getColor() == color.invert()) {
                moves.add(new NormalMove(position, newPosition, board));
                return moves;
            } else {
                moves.add(new NormalMove(position, newPosition, board));
            }
            checkX -= 1;
            checkY += 1;
        }

        return moves;
    }

    private Set<Move> handleDownLeft (Board board, Position position) {
        Set<Move> moves = new LinkedHashSet<>();
        int checkX = position.getX() - 1;
        int checkY = position.getY() - 1;

        while (checkX >= 0 && checkY >= 0) {
            Position newPosition = new Position(checkX, checkY);
            Piece currentPiece = board.getPieceInSquare(newPosition);

            if (currentPiece.getColor() == color) {
                return moves;
            } else if (currentPiece.getColor() == color.invert()) {
                moves.add(new NormalMove(position, newPosition, board));
                return moves;
            } else {
                moves.add(new NormalMove(position, newPosition, board));
            }
            checkX -= 1;
            checkY -= 1;
        }

        return moves;
    }

    private Set<Move> handleDownRight (Board board, Position position) {
        Set<Move> moves = new LinkedHashSet<>();
        int checkX = position.getX() + 1;
        int checkY = position.getY() - 1;

        while (checkX < board.getDimX() && checkY >= 0) {
            Position newPosition = new Position(checkX, checkY);
            Piece currentPiece = board.getPieceInSquare(newPosition);

            if (currentPiece.getColor() == color) {
                return moves;
            } else if (currentPiece.getColor() == color.invert()) {
                moves.add(new NormalMove(position, newPosition, board));
                return moves;
            } else {
                moves.add(new NormalMove(position, newPosition, board));
            }
            checkX += 1;
            checkY -= 1;
        }

        return moves;
    }
}
