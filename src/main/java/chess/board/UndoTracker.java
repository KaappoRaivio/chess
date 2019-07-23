package chess.board;

import chess.misc.ChessException;
import chess.misc.Position;
import chess.move.Move;
import chess.piece.NoPiece;
import chess.piece.basepiece.Piece;
import chess.piece.basepiece.PieceType;

import java.io.Serializable;
import java.util.*;

public class UndoTracker implements Serializable {
    private Deque<Move> moves = new LinkedList<>();

    public void addMove (Move move) {
        moves.push(move);
    }


    private void undoOneLevel (Piece[][] buffer) {
        Move lastMove;
        try {
            lastMove = moves.pop();
        } catch (NoSuchElementException e) {
            throw new ChessException("UndoTracker is empty!");
        }

        Position origin = lastMove.getOrigin();
        Position destination = lastMove.getDestination();
        if (buffer[origin.getY()][origin.getX()].getType() != PieceType.NO_PIECE) {
            throw new ChessException("Square " + origin + " should be empty instead of " + buffer[origin.getY()][origin.getX()].getType() + "! " + lastMove);
        }

        buffer[origin.getY()][origin.getX()] = buffer[destination.getY()][destination.getX()];
        buffer[destination.getY()][destination.getX()] = lastMove.getPieceUnderDestination();

        buffer[origin.getY()][origin.getX()].setHasMoved(lastMove.hasOriginPieceMovedBefore());
        buffer[destination.getY()][destination.getX()].setHasMoved(lastMove.hasDestinationPieceMovedBefore());

        boolean castling = isCastling(buffer, lastMove);
        moveRookBackIfWasCastling(buffer, lastMove, castling);


    }

    void undo (Piece[][] buffer, int times) {
        for (int i = 0; i < times; i++) {
            undoOneLevel(buffer);
        }
    }

    public UndoTracker deepCopy () {
        Queue<Move> tempBuffer = new LinkedList<>();
        while (!moves.isEmpty()) {
            tempBuffer.add(moves.remove());
        }

        UndoTracker another = new UndoTracker();
        while (!tempBuffer.isEmpty()) {
            another.moves.add(tempBuffer.peek());
            moves.add(tempBuffer.remove());
        }

        return another;
    }

    private boolean isCastling (Piece[][] buffer, Move move) {
        return buffer[move.getOrigin().getY()][move.getOrigin().getX()].getType() == PieceType.KING && Math.abs(move.getOrigin().getX() - move.getDestination().getX()) == 2;
    }

    private int getCastlingDirection (Move move) {
        return Integer.compare(move.getDestination().getX() - move.getOrigin().getX(), 0);
    }

    private void moveRookBackIfWasCastling (Piece[][] buffer, Move move, boolean castling) {
        if (castling) {
            int castlingDirection = getCastlingDirection(move);

            Position oldRookPos;
            Position newRookPos;

            if (castlingDirection > 0 ) {
                oldRookPos = move.getOrigin().offsetX(1);
                newRookPos = move.getOrigin().offsetX(3);
            } else if (castlingDirection < 0) {
                oldRookPos = move.getOrigin().offsetX(-1);
                newRookPos = move.getOrigin().offsetX(-4);
            } else {
                throw new ChessException("Not a castling! (" + move + ")");
            }

            buffer[newRookPos.getY()][newRookPos.getX()] = buffer[oldRookPos.getY()][oldRookPos.getX()];
            buffer[oldRookPos.getY()][oldRookPos.getX()] = new NoPiece();
            buffer[newRookPos.getY()][newRookPos.getX()].setHasMoved(false);
        }
    }
}
