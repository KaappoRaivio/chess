package chess.piece;

import chess.board.Board;
import chess.misc.Position;
import chess.piece.basepiece.Piece;
import chess.piece.basepiece.PieceColor;
import chess.piece.basepiece.PieceType;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {
    public Bishop (PieceColor color) {
        super(PieceType.BISHOP, color, color == PieceColor.WHITE ? "♗" : "♝");
    }

    @Override
    public List<Position> getPossiblePositions (Board board, Position position) {
        List<Position> positions = new ArrayList<>();

        positions.addAll(handleUpRight(board, position));
        positions.addAll(handleUpLeft(board, position));
        positions.addAll(handleDownLeft(board, position));
        positions.addAll(handleDownRight(board, position));

        return positions;
    }

    private List<Position> handleUpRight (Board board, Position position) {
        List<Position> positions = new ArrayList<>();
        int checkX = position.getX();
        int checkY = position.getY();

        while (checkX < board.getDimX() && checkY < board.getDimY()) {
            Position newPosition = position.offset(checkX, checkY);
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

    private List<Position> handleUpLeft (Board board, Position position) {
        List<Position> positions = new ArrayList<>();
        int checkX = position.getX();
        int checkY = position.getY();

        while (checkX >= 0 && checkY < board.getDimY()) {
            Position newPosition = position.offset(checkX, checkY);
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

    private List<Position> handleDownLeft (Board board, Position position) {
        List<Position> positions = new ArrayList<>();
        int checkX = position.getX();
        int checkY = position.getY();

        while (checkX >= 0 && checkY >= 0) {
            Position newPosition = position.offset(checkX, checkY);
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

    private List<Position> handleDownRight (Board board, Position position) {
        List<Position> positions = new ArrayList<>();
        int checkX = position.getX();
        int checkY = position.getY();

        while (checkX < board.getDimX() && checkY >= 0) {
            Position newPosition = position.offset(checkX, checkY);
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
