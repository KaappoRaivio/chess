package chess.piece;

import chess.board.Board;
import chess.misc.exceptions.ChessException;
import chess.misc.Position;
import chess.move.Move;
import chess.move.NormalMove;
import chess.piece.basepiece.Piece;
import chess.piece.basepiece.PieceColor;
import chess.piece.basepiece.PieceType;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class Knight extends Piece {
    public static final Position[] offsets = {
            new Position(1, 2),
            new Position(2, 1),
            new Position(2, -1, false),
            new Position(1, -2, false),
            new Position(-1, -2, false),
            new Position(-2, -1, false),
            new Position(-2, 1, false),
            new Position(-1, 2, false),
    };


    public Knight (PieceColor color) {
        super(PieceType.KNIGHT, color, color == PieceColor.WHITE ? "♘" : "♞", 3);
    }

    @Override
    public Set<Move> getPossibleMoves (Board board, Position position, Move lastMove) {
        return Arrays.stream(offsets).map(item -> {
            try {
                return new NormalMove(position, position.offset(item), board);
            } catch (ChessException e) {
                return new NormalMove(position, position, board);  // Mark all positions outside the board temporarily...
            }
        })
                .filter(item -> !new NormalMove(position, position, board).equals(item))  // ... and remove them here
                .filter(item -> board.getPieceInSquare(item.getDestination()).getColor() != color)
                .collect(Collectors.toSet());
    }
}
