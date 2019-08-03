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
        this(color, color == PieceColor.WHITE ? "♔" : "♚", 400);
    }

    King (PieceColor color, String symbol, int value) {
        super(PieceType.KING, color, symbol, value);
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

    @Override
    protected int[][] getPieceSquareTable () {
        return new int[][]{
                {20, 30, 10,  0,  0, 10, 30, 20},
                {20, 20,  0,  0,  0,  0, 20, 20},
                {-10,-20,-20,-20,-20,-20,-20,-10},
                {-20,-30,-30,-40,-40,-30,-30,-20},
                {-30,-40,-40,-50,-50,-40,-40,-30},
                {-30,-40,-40,-50,-50,-40,-40,-30},
                {-30,-40,-40,-50,-50,-40,-40,-30},
                {-30,-40,-40,-50,-50,-40,-40,-30},
        };
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
