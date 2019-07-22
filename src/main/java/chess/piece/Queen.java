package chess.piece;

import chess.board.Board;
import chess.misc.Position;
import chess.piece.basepiece.Piece;
import chess.piece.basepiece.PieceColor;
import chess.piece.basepiece.PieceType;

import java.util.Set;

public class Queen extends Piece {
    public Queen (PieceColor color) {
        super(PieceType.QUEEN, color, color == PieceColor.WHITE ? "♕" : "♛");
    }

    @Override
    public Set<Position> getPossiblePositions (Board board, Position position) {
        Set<Position> moves = new Rook(color).getPossiblePositions(board, position);
        moves.addAll(new Bishop(color).getPossiblePositions(board, position));

        return moves;
    }
}
