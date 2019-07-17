package chess.piece;

import chess.board.Board;
import chess.misc.ChessException;
import chess.misc.Position;
import chess.piece.basepiece.Piece;
import chess.piece.basepiece.PieceColor;
import chess.piece.basepiece.PieceType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Pawn extends Piece {
    private int amountOfMovesSinceLastMoving; //  Used for en passant

    public Pawn (PieceColor color, Position position) {
        super(PieceType.PAWN, color, position);

        amountOfMovesSinceLastMoving = -1;
    }

    @Override
    public List<Position> getPossiblePositions (Board board) {
        List<Position> positions = new LinkedList<>();

        positions.addAll(handleStraightAhead(board));
        positions.addAll(handleCapture(board));
        positions.addAll(handleEnPassant(board));

        return positions;
    }

    private List<Position> handleStraightAhead (Board board) {
        List<Position> positions = new LinkedList<>();
        try {
            if (board.isSquareEmpty(position.offsetY(getForwardDirection()))) {
                positions.add(position.offsetY(getForwardDirection()));

                try {
                    if (!hasMoved && board.isSquareEmpty(position.offsetY(getForwardDirection() * 2))) {
                        positions.add(position.offsetY(getForwardDirection() * 2));
                    }
                } catch (ChessException ignored) {}
            }
        } catch (ChessException ignored) {}

        return positions;
    }

    private List<Position> handleCapture (Board board) {
        List<Position> positions = new LinkedList<>();

        try {
            if (board.getPieceInSquare(position.offset(1, getForwardDirection())).getColor() == color.invert()) {
                positions.add(position.offset(1, getForwardDirection()));
            }
        } catch (ChessException ignored) {}

        try {
            if (board.getPieceInSquare(position.offset(-1, getForwardDirection())).getColor() == color.invert()) {
                positions.add(position.offset(-1, getForwardDirection()));
            }
        } catch (ChessException ignored) {}

        return positions;
    }

    private List<Position> handleEnPassant (Board board) {
        List<Position> positions = new LinkedList<>();

        try {
            Piece piece = board.getPieceInSquare(position.offsetX(1));
            if (piece instanceof Pawn && ((Pawn) piece).isEligibleForEnPassant()) {
                if (board.isSquareEmpty(position.offset(1, getForwardDirection()))) {
                    positions.add(position.offset(1, getForwardDirection()));
                }
            }
        } catch (ChessException e) {}

        try {
            Piece piece = board.getPieceInSquare(position.offsetX(-1));
            if (piece instanceof Pawn && ((Pawn) piece).isEligibleForEnPassant()) {
                if (board.isSquareEmpty(position.offset(-1, getForwardDirection()))) {
                    positions.add(position.offset(-1, getForwardDirection()));
                }
            }
        } catch (ChessException e) {}

        return positions;
    }

    @Override
    public void onMoved (Position newPosition) {
        super.onMoved(newPosition);

        amountOfMovesSinceLastMoving = 0;
    }

    @Override
    public void onAnotherPieceMoved () {
        super.onAnotherPieceMoved();
        amountOfMovesSinceLastMoving += 1;
    }

    private boolean isEligibleForEnPassant () {
        return amountOfMovesSinceLastMoving == 0;
    }
}
