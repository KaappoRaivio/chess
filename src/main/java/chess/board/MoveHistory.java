package chess.board;

import chess.misc.ChessException;
import chess.misc.Position;
import chess.move.Move;
import chess.piece.basepiece.Piece;
import chess.piece.basepiece.PieceType;

import java.util.*;

public class MoveHistory {
    private Queue<Move> moves = new LinkedList<>();

    public void addMove (Move move) {
        moves.add(move);
    }


    private void undoOneLevel (Piece[][] buffer) {
        Move lastMove;
        try {
            lastMove = moves.remove();
        } catch (NoSuchElementException e) {
            throw new ChessException("MoveHistory is empty!");
        }

        Position origin = lastMove.getOrigin();
        Position destination = lastMove.getDestination();
        if (buffer[origin.getY()][origin.getX()].getType() != PieceType.NO_PIECE) {
            throw new ChessException("Square " + origin + " should be empty instead of " + buffer[origin.getY()][origin.getX()].getType() + "!");
        }

        buffer[origin.getY()][origin.getX()] = buffer[destination.getY()][destination.getX()];
        buffer[destination.getY()][destination.getX()] = lastMove.getPieceUnderDestination();

    }

    void undo (Piece[][] buffer, int times) {
        for (int i = 0; i < times; i++) {
            undoOneLevel(buffer);
        }
    }

    public MoveHistory deepCopy () {
        Queue<Move> tempBuffer = new LinkedList<>();
        while (!moves.isEmpty()) {
            tempBuffer.add(moves.remove());
        }

        MoveHistory another = new MoveHistory();
        while (!tempBuffer.isEmpty()) {
            another.moves.add(tempBuffer.remove());
        }

        return another;
    }
}
