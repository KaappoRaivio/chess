package chess.piece;

import chess.board.Board;
import chess.misc.Position;
import chess.piece.basepiece.Piece;
import chess.piece.basepiece.PieceColor;
import chess.piece.basepiece.PieceType;

import java.util.LinkedList;
import java.util.List;

public class Rook extends Piece {
    public Rook (PieceColor color, Position position) {
        super(PieceType.ROOK, color, position);
    }

    @Override
    public List<Position> getPossiblePositions (Board board) {
        List<Position> list = new LinkedList<>();

        list.addAll(handleUp(board));
        list.addAll(handleDown(board));
        list.addAll(handleLeft(board));
        list.addAll(handleRight(board));

        return list;
    }

    public List<Position> handleUp (Board board) {
        List<Position> positions = new LinkedList<>();

        for (Position pos = position; pos.getY() < board.getDimY(); pos = pos.offsetY(1)) {
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

    public List<Position> handleDown (Board board) {
        List<Position> positions = new LinkedList<>();

        for (Position pos = position; pos.getY() >= 0; pos = pos.offsetY(-1)) {
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

    public List<Position> handleLeft (Board board) {
        List<Position> positions = new LinkedList<>();

        for (Position pos = position; pos.getX() < board.getDimX(); pos = pos.offsetX(1)) {
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

    public List<Position> handleRight (Board board) {
        List<Position> positions = new LinkedList<>();

        for (Position pos = position; pos.getX() >= 0; pos = pos.offsetX(-1)) {
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
