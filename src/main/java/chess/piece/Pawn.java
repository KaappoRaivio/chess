package chess.piece;

import chess.board.Board;
import chess.misc.ChessException;
import chess.misc.Position;
import chess.move.Move;
import chess.piece.basepiece.Piece;
import chess.piece.basepiece.PieceColor;
import chess.piece.basepiece.PieceType;

import java.util.HashSet;
import java.util.Set;

public class Pawn extends Piece {
    private int amountOfMovesSinceLastMoving; //  Used for en passant

    public Pawn (PieceColor color) {
        super(PieceType.PAWN, color, color == PieceColor.WHITE ? "♙" : "♟");

        amountOfMovesSinceLastMoving = -1;
    }

    @Override
    public Set<Position> getPossiblePositions (Board board, Position position) {
        Set<Position> positions = new HashSet<>();

        positions.addAll(handleStraightAhead(board, position));
        positions.addAll(handleCapture(board, position));
        positions.addAll(handleEnPassant(board, position));

        return positions;
    }

    private Set<Position> handleStraightAhead (Board board, Position position) {
        Set<Position> positions = new HashSet<>();
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

    private Set<Position> handleCapture (Board board, Position position) {
        Set<Position> positions = new HashSet<>();

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

    private Set<Position> handleEnPassant (Board board, Position position) {
        Set<Position> positions = new HashSet<>();

        try {
            Piece piece = board.getPieceInSquare(position.offsetX(1));
            if (piece instanceof Pawn && ((Pawn) piece).isEligibleForEnPassant() && piece.getColor() == color.invert()) {
                if (board.isSquareEmpty(position.offset(1, getForwardDirection()))) {
                    positions.add(position.offset(1, getForwardDirection()));
                }
            }
        } catch (ChessException ignored) {}

        try {
            Piece piece = board.getPieceInSquare(position.offsetX(-1));
            if (piece instanceof Pawn && ((Pawn) piece).isEligibleForEnPassant() && piece.getColor() == color.invert()) {
                if (board.isSquareEmpty(position.offset(-1, getForwardDirection()))) {
                    positions.add(position.offset(-1, getForwardDirection()));
                }
            }
        } catch (ChessException ignored) {}

        return positions;
    }

    @Override
    public void onMoved (Move move, Board board) {
        super.onMoved(move, board);

        amountOfMovesSinceLastMoving = 0;
    }

    @Override
    public void onAnotherPieceMoved (Move move, Board board) {
        super.onAnotherPieceMoved(move, board);
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
