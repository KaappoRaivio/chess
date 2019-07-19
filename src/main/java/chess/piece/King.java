package chess.piece;

import chess.board.Board;
import chess.misc.ChessException;
import chess.misc.Position;
import chess.piece.basepiece.Piece;
import chess.piece.basepiece.PieceColor;
import chess.piece.basepiece.PieceType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class King extends Piece {
    public static final Position[] offsets = {
            new Position (1, 1),
            new Position (1, 0),
            new Position (1, -1),
            new Position (0, -1),
            new Position (-1, -1),
            new Position (-1, 0),
            new Position (-1, 1),
            new Position (0, 1),
    };

    public King (PieceColor color, Position position) {
        super(PieceType.KING, color, position);
    }

    @Override
    public List<Position> getPossiblePositions (Board board) {
        return Arrays.stream(offsets).map(item -> {
            try {
                return position.offset(item);
            } catch (ChessException e) {
                return position;
            }
        })
                .filter(item -> !position.equals(item))
                .filter(item -> board.getPieceInSquare(item).getColor() != color)
                .collect(Collectors.toList());
    }
}
