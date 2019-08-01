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
        super(PieceType.QUEEN, color, color == PieceColor.WHITE ? "♕" : "♛", 9);
    }

    @Override
    public Set<Move> getPossibleMoves (Board board, Position position, Move lastMove) {
        Set<Move> moves = new Rook(color).getPossibleMoves(board, position, lastMove);
        moves.addAll(new Bishop(color).getPossibleMoves(board, position, lastMove));

        return moves;
    }
}
