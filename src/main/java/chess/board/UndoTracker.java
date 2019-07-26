package chess.board;

import chess.misc.Counter;
import chess.misc.exceptions.ChessException;
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


    void undoOneLevel (Piece[][] buffer) {
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

        Piece originPiece = buffer[origin.getY()][origin.getX()];
        Piece destinationPiece = buffer[destination.getY()][destination.getX()];

        destinationPiece.setHasMoved(lastMove.hasDestinationPieceMovedBefore());

        originPiece.setHasMoved(lastMove.hasOriginPieceMovedBefore());

        if (isCastling(buffer, lastMove)) {
            moveRookBack(buffer, lastMove);
        }

        for (int y = 0; y < buffer.length; y++) {
            for (int x = 0; x < buffer[y].length; x++) {
                buffer[y][x].undoLastMove();
            }
        }
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

    private void moveRookBack (Piece[][] buffer, Move move) {
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
