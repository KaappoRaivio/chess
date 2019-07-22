package chess.piece;

import chess.board.Board;
import chess.misc.ChessException;
import chess.misc.Position;
import chess.piece.basepiece.Piece;
import chess.piece.basepiece.PieceColor;
import chess.piece.basepiece.PieceType;

import java.util.Arrays;
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
        super(PieceType.KING, color, color == PieceColor.WHITE ? "♔" : "♚");
    }

    @Override
    public Set<Position> getPossiblePositions (Board board, Position position) {
        return Arrays.stream(offsets).map(item -> {
            try {
                return position.offset(item);
            } catch (ChessException e) {
                return position; // Mark all positions outside the board temporarily...
            }
        })
                .filter(item -> !position.equals(item)) // ... and remove them here
                .filter(item -> board.getPieceInSquare(item).getColor() != color)
                .collect(Collectors.toSet());
    }
}
