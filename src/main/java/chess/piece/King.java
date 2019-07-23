package chess.piece;

import chess.board.Board;
import chess.misc.ChessException;
import chess.misc.Position;
import chess.piece.basepiece.Piece;
import chess.piece.basepiece.PieceColor;
import chess.piece.basepiece.PieceType;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class King extends Piece {
    public static final Position[] offsets = {
            new Position (1, 1),
            new Position (1, 0),
            new Position (1, -1, false),
            new Position (0, -1, false),
            new Position (-1, -1, false),
            new Position (-1, 0, false),
            new Position (-1, 1, false),
            new Position (0, 1),
    };

    public King (PieceColor color) {
        super(PieceType.KING, color, color == PieceColor.WHITE ? "♔" : "♚", 4);
    }

    @Override
    public Set<Position> getPossiblePositions (Board board, Position position) {
        var moves = Arrays.stream(offsets).map(item -> {
            try {
                return position.offset(item);
            } catch (ChessException e) {
                return position; // Mark all positions that are outside the board temporarily...
            }
        })
                .filter(item -> !position.equals(item)) // ... and remove them here
                .filter(item -> board.getPieceInSquare(item).getColor() != color)
                .collect(Collectors.toSet());

        moves.addAll(handleKingSideCastling(board, position));
        moves.addAll(handleQueenSideCastling(board, position));

        return moves;
    }

    private Set<Position> handleKingSideCastling (Board board, Position position) {
        Piece supposedRook = board.getPieceInSquare(position.offsetX(3));
        if (hasMoved() || supposedRook.getType() != PieceType.ROOK || supposedRook.hasMoved()) {
            return Collections.emptySet();
        }

        if (board.isSquareUnderThreat(position) || board.isSquareUnderThreat(position.offsetX(1)) || board.isSquareUnderThreat(position.offsetX(2))) {
            return Collections.emptySet();
        }

        return Set.of(position.offsetX(2));
    }

    private Set<Position> handleQueenSideCastling (Board board, Position position) {
        Piece supposedRook = board.getPieceInSquare(position.offsetX(-4));
        if (hasMoved() || supposedRook.getType() != PieceType.ROOK || supposedRook.hasMoved()) {
            return Collections.emptySet();
        }

        if (board.isSquareUnderThreat(position) || board.isSquareUnderThreat(position.offsetX(-1)) || board.isSquareUnderThreat(position.offsetX(-2))) {
            return Collections.emptySet();
        }

        return Set.of(position.offsetX(-2));
    }

    private Position getStartingPosition (PieceColor color) {
        switch (color) {
            case WHITE:
                return Position.fromString("E1");
            case BLACK:
                return Position.fromString("E8");
            default:
                throw new ChessException("Shouldn't happen. If does, you have broken something!");
        }
    }
}
