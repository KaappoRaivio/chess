package chess.piece;

import chess.board.Board;
import chess.misc.ChessException;
import chess.misc.Position;
import chess.piece.basepiece.Piece;
import chess.piece.basepiece.PieceColor;
import chess.piece.basepiece.PieceType;

import java.util.LinkedList;
import java.util.List;

public class Pawn extends Piece {
    private int amountOfMovesSinceLastMoving; //  Used for en passant

    public Pawn (PieceColor color) {
        super(PieceType.PAWN, color, color == PieceColor.WHITE ? "♙" : "♟");

        amountOfMovesSinceLastMoving = -1;
    }

    @Override
    public List<Position> getPossiblePositions (Board board, Position position) {
        List<Position> positions = new LinkedList<>();

        positions.addAll(handleStraightAhead(board, position));
        positions.addAll(handleCapture(board, position));
        positions.addAll(handleEnPassant(board, position));

        return positions;
    }

    private List<Position> handleStraightAhead (Board board, Position position) {
        List<Position> positions = new LinkedList<>();
        try {
            if (board.isSquareEmpty(position.offsetY(getForwardDirection()))) {
                positions.add(position.offsetY(getForwardDirection()));

                try {
                    if (!hasMoved() && board.isSquareEmpty(position.offsetY(getForwardDirection() * 2))) {
                        positions.add(position.offsetY(getForwardDirection() * 2));
                    }
                } catch (ChessException ignored) {}
            }
        } catch (ChessException ignored) {}

        return positions;
    }

    private List<Position> handleCapture (Board board, Position position) {
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

    private List<Position> handleEnPassant (Board board, Position position) {
        List<Position> positions = new LinkedList<>();

        try {
            Piece piece = board.getPieceInSquare(position.offsetX(1));
            if (piece instanceof Pawn && ((Pawn) piece).isEligibleForEnPassant()) {
                if (board.isSquareEmpty(position.offset(1, getForwardDirection()))) {
                    positions.add(position.offset(1, getForwardDirection()));
                }
            }
        } catch (ChessException ignored) {}

        try {
            Piece piece = board.getPieceInSquare(position.offsetX(-1));
            if (piece instanceof Pawn && ((Pawn) piece).isEligibleForEnPassant()) {
                if (board.isSquareEmpty(position.offset(-1, getForwardDirection()))) {
                    positions.add(position.offset(-1, getForwardDirection()));
                }
            }
        } catch (ChessException ignored) {}

        return positions;
    }

    @Override
    public void onMoved (Position oldPosition, Position newPosition) {
        super.onMoved(oldPosition, newPosition);

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

    @Override
    public void setAmountOfMovesSinceLastMoving (int amountOfMovesSinceLastMoving) {
        this.amountOfMovesSinceLastMoving = amountOfMovesSinceLastMoving;
    }

    @Override
    public Piece deepCopy () {
        Pawn another = (Pawn) super.deepCopy();
        another.amountOfMovesSinceLastMoving = amountOfMovesSinceLastMoving;
        return another;
    }
}
