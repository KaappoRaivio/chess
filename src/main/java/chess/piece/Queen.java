package chess.piece;

import chess.board.Board;
import chess.misc.Position;
import chess.piece.basepiece.Piece;
import chess.piece.basepiece.PieceColor;
import chess.piece.basepiece.PieceType;

import java.util.List;

public class Queen extends Piece {
    public Queen (PieceColor color) {
        super(PieceType.QUEEN, color, color == PieceColor.WHITE ? "♕" : "♛");
    }

    @Override
    public List<Position> getPossiblePositions (Board board, Position position) {
        return null;
    }
}
