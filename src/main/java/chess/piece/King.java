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
        this(color, color == PieceColor.WHITE ? "♔" : "♚");
    }

    King (PieceColor color, String symbol) {
        super(PieceType.KING, color, symbol, 4);
    }

    @Override
    public Set<Move> getPossibleMoves (Board board, Position position, Move lastMove) {
        return Arrays.stream(offsets).map(item -> {
            try {
                return new NormalMove(position, position.offset(item), board);
            } catch (ChessException e) {
                return new NormalMove(position, position, board); // Mark all positions that are outside the board temporarily...
            }
        })
                .filter(item -> !new NormalMove(position, position, board).equals(item)) // ... and remove them here
                .filter(item -> board.getPieceInSquare(item.getDestination()).getColor() != color)
                .collect(Collectors.toSet());
    }


    private boolean canCastle (Board board, Position position) {
        return this instanceof CastlingKing;
    }

    @Override
    public int getIndex (Board board, Position position, Move lastMove) {
        if (canCastle(board, position)) {
            return getColor() == PieceColor.WHITE ? 14 : 15;
        }
        return super.getIndex(board, position, lastMove);
    }
}
