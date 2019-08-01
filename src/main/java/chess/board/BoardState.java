package chess.board;

import chess.misc.Position;
import chess.move.Move;

import java.io.Serializable;

public class BoardState implements Serializable {
    private Position whiteKingPosition;
    private Position blackKingPosition;

    private int movesSinceFiftyMoveReset;
    private Move lastMove;

    public BoardState (Position whiteKingPosition, Position blackKingPosition, int movesSinceFiftyMoveReset, Move lastMove) {
        this.whiteKingPosition = whiteKingPosition;
        this.blackKingPosition = blackKingPosition;
        this.movesSinceFiftyMoveReset = movesSinceFiftyMoveReset;
        this.lastMove = lastMove;
    }

    public BoardState (BoardState other) {
        this(other.whiteKingPosition, other.blackKingPosition, other.movesSinceFiftyMoveReset, other.lastMove);
    }

    public Position getWhiteKingPosition () {
        return whiteKingPosition;
    }

    public Position getBlackKingPosition () {
        return blackKingPosition;
    }

    public int getMovesSinceFiftyMoveReset () {
        return movesSinceFiftyMoveReset;
    }

    public Move getLastMove () {
        return lastMove;
    }

    public void setWhiteKingPosition (Position whiteKingPosition) {
        this.whiteKingPosition = whiteKingPosition;
    }

    public void setBlackKingPosition (Position blackKingPosition) {
        this.blackKingPosition = blackKingPosition;
    }

    public void setMovesSinceFiftyMoveReset (int movesSinceFiftyMoveReset) {
        this.movesSinceFiftyMoveReset = movesSinceFiftyMoveReset;
    }

    public void setLastMove (Move lastMove) {
        this.lastMove = lastMove;
    }

    @Override
    public String toString () {
        return "BoardState{" +
                "whiteKingPosition=" + whiteKingPosition +
                ", blackKingPosition=" + blackKingPosition +
                ", movesSinceFiftyMoveReset=" + movesSinceFiftyMoveReset +
                ", lastMove=" + lastMove +
                '}';
    }
}
