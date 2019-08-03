package chess.piece;

import chess.board.Board;
import chess.misc.Position;
import chess.move.Move;
import chess.piece.basepiece.Piece;
import chess.piece.basepiece.PieceColor;
import chess.piece.basepiece.PieceType;

import java.util.Set;

public class Queen extends Piece {
    public Queen (PieceColor color) {
        super(PieceType.QUEEN, color, color == PieceColor.WHITE ? "♕" : "♛", 900);
    }

    @Override
    public Set<Move> getPossibleMoves (Board board, Position position, Move lastMove) {
        Set<Move> moves = new Rook(color).getPossibleMoves(board, position, lastMove);
        moves.addAll(new Bishop(color).getPossibleMoves(board, position, lastMove));

        return moves;
    }

    @Override
    protected int[][] getPieceSquareTable () {
        return new int[][]{
                {-20,-10,-10, -5, -5,-10,-10,-20},
                {-10,  0,  5,  0,  0,  0,  0,-10},
                {-10,  5,  5,  5,  5,  5,  0,-10},
                {0,  0,  5,  5,  5,  5,  0, -5},
                {-5,  0,  5,  5,  5,  5,  0, -5},
                {-10,  0,  5,  5,  5,  5,  0,-10},
                {-10,  0,  0,  0,  0,  0,  0,-10},
                {-20,-10,-10, -5, -5,-10,-10,-20},
        };
    }
}
