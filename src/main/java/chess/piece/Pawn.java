package chess.piece;

import chess.board.Board;
import chess.misc.Position;
import chess.piece.basepiece.Piece;
import chess.piece.basepiece.PieceColor;
import chess.piece.basepiece.PieceType;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {

    public Pawn (PieceColor color, Position position) {
        super(PieceType.PAWN, color, position);
    }

    @Override
    public List<Position> getPossiblePositions (Board board) {
        List<Position> positions = new ArrayList<>();

        if (board.isSquareEmpty(position.offsetY(getForwardDirection()))) {
            positions.add(position.offsetY(getForwardDirection()));

            if (!hasMoved && board.isSquareEmpty(position.offsetY(getForwardDirection() * 2))) {
                positions.add(position.offsetY(getForwardDirection() * 2));
            }
        }



        return positions;
    }

}
