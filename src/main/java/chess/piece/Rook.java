package chess.piece;

import chess.board.Board;
import chess.misc.Position;
import chess.piece.basepiece.Piece;
import chess.piece.basepiece.PieceColor;
import chess.piece.basepiece.PieceType;

import java.util.HashSet;
import java.util.Set;

public class Rook extends Piece {
    public Rook (PieceColor color) {
        super(PieceType.ROOK, color, color == PieceColor.WHITE ? "♖" : "♜", 5);
    }

    @Override
    public Set<Position> getPossiblePositions (Board board, Position position) {
        Set<Position> list = new HashSet<>();

        list.addAll(handleUp(board, position));
        list.addAll(handleDown(board, position));
        list.addAll(handleLeft(board, position));
        list.addAll(handleRight(board, position));

        return list;
    }

    private Set<Position> handleUp (Board board, Position position) {
        Set<Position> positions = new HashSet<>();

        for (Position pos = position.offsetY(1); pos.getY() < board.getDimY(); pos = pos.offsetY(1, false)) {
            Piece piece = board.getPieceInSquare(pos);

            if (piece.getColor() == color) {
                break;
            } else if (piece.getColor() == color.invert()) {
                positions.add(pos);
                break;
            } else {
                positions.add(pos);
            }
        }

        return positions;
    }

    private Set<Position> handleDown (Board board, Position position) {
        Set<Position> positions = new HashSet<>();

        for (Position pos = position.offsetY(-1); pos.getY() >= 0; pos = pos.offsetY(-1, false)) {
            System.out.println(pos);
            Piece piece = board.getPieceInSquare(pos);

            if (piece.getColor() == color) {
                break;
            } else if (piece.getColor() == color.invert()) {
                positions.add(pos);
                break;
            } else {
                positions.add(pos);
            }
        }

        return positions;
    }

    private Set<Position> handleLeft (Board board, Position position) {
        Set<Position> positions = new HashSet<>();

        for (Position pos = position.offsetX(1); pos.getX() < board.getDimX(); pos = pos.offsetX(1, false)) {
            Piece piece = board.getPieceInSquare(pos);

            if (piece.getColor() == color) {
                break;
            } else if (piece.getColor() == color.invert()) {
                positions.add(pos);
                break;
            } else {
                positions.add(pos);
            }
        }

        return positions;
    }

    private Set<Position> handleRight (Board board, Position position) {
        Set<Position> positions = new HashSet<>();

        for (Position pos = position.offsetX(-1); pos.getX() >= 0; pos = pos.offsetX(-1, false)) {
            Piece piece = board.getPieceInSquare(pos);

            if (piece.getColor() == color) {
                break;
            } else if (piece.getColor() == color.invert()) {
                positions.add(pos);
                break;
            } else {
                positions.add(pos);
            }
        }

        return positions;
    }
}
