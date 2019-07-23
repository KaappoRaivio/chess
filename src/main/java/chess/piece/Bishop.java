package chess.piece;

import chess.board.Board;
import chess.misc.Position;
import chess.piece.basepiece.Piece;
import chess.piece.basepiece.PieceColor;
import chess.piece.basepiece.PieceType;

import java.util.HashSet;
import java.util.Set;

public class Bishop extends Piece {
    public Bishop (PieceColor color) {
        super(PieceType.BISHOP, color, color == PieceColor.WHITE ? "♗" : "♝", 3);
    }

    @Override
    public Set<Position> getPossiblePositions (Board board, Position position) {
        Set<Position> positions = new HashSet<>();

        positions.addAll(handleUpRight(board, position));
        positions.addAll(handleUpLeft(board, position));
        positions.addAll(handleDownLeft(board, position));
        positions.addAll(handleDownRight(board, position));

        return positions;
    }

    private Set<Position> handleUpRight (Board board, Position position) {
        Set<Position> positions = new HashSet<>();
        int checkX = position.getX() + 1;
        int checkY = position.getY() + 1;

        while (checkX < board.getDimX() && checkY < board.getDimY()) {
            Position newPosition = new Position(checkX, checkY);
            Piece currentPiece = board.getPieceInSquare(newPosition);

            if (currentPiece.getColor() == color) {
                return positions;
            } else if (currentPiece.getColor() == color.invert()) {
                positions.add(newPosition);
                return positions;
            } else {
                positions.add(newPosition);
            }
            checkX += 1;
            checkY += 1;
        }

        return positions;
    }

    private Set<Position> handleUpLeft (Board board, Position position) {
        Set<Position> positions = new HashSet<>();
        int checkX = position.getX() - 1;
        int checkY = position.getY() + 1;

        while (checkX >= 0 && checkY < board.getDimY()) {
            Position newPosition = new Position(checkX, checkY);
            Piece currentPiece = board.getPieceInSquare(newPosition);

            if (currentPiece.getColor() == color) {
                return positions;
            } else if (currentPiece.getColor() == color.invert()) {
                positions.add(newPosition);
                return positions;
            } else {
                positions.add(newPosition);
            }
            checkX -= 1;
            checkY += 1;
        }

        return positions;
    }

    private Set<Position> handleDownLeft (Board board, Position position) {
        Set<Position> positions = new HashSet<>();
        int checkX = position.getX() - 1;
        int checkY = position.getY() - 1;

        while (checkX >= 0 && checkY >= 0) {
            Position newPosition = new Position(checkX, checkY);
            Piece currentPiece = board.getPieceInSquare(newPosition);

            if (currentPiece.getColor() == color) {
                return positions;
            } else if (currentPiece.getColor() == color.invert()) {
                positions.add(newPosition);
                return positions;
            } else {
                positions.add(newPosition);
            }
            checkX -= 1;
            checkY -= 1;
        }

        return positions;
    }

    private Set<Position> handleDownRight (Board board, Position position) {
        Set<Position> positions = new HashSet<>();
        int checkX = position.getX() + 1;
        int checkY = position.getY() - 1;

        while (checkX < board.getDimX() && checkY >= 0) {
            Position newPosition = new Position(checkX, checkY);
            Piece currentPiece = board.getPieceInSquare(newPosition);

            if (currentPiece.getColor() == color) {
                return positions;
            } else if (currentPiece.getColor() == color.invert()) {
                positions.add(newPosition);
                return positions;
            } else {
                positions.add(newPosition);
            }
            checkX += 1;
            checkY -= 1;
        }

        return positions;
    }
}
