package chess.board;

import chess.misc.Position;
import chess.move.Move;
import chess.piece.basepiece.PieceColor;

import java.io.Serializable;

public class BoardState implements Serializable {
    private Position whiteKingPosition;
    private Position blackKingPosition;

    private int movesSinceFiftyMoveReset;
    private Move lastMove;
    private PieceColor turn;


    private int moveCount;

    BoardState(Position whiteKingPosition, Position blackKingPosition, int movesSinceFiftyMoveReset, Move lastMove, PieceColor turn, int moveCount) {
        this.whiteKingPosition = whiteKingPosition;
        this.blackKingPosition = blackKingPosition;
        this.movesSinceFiftyMoveReset = movesSinceFiftyMoveReset;
        this.lastMove = lastMove;
        this.turn = turn;
        this.moveCount = moveCount;
    }

    BoardState (BoardState other) {
        this(other.whiteKingPosition, other.blackKingPosition, other.movesSinceFiftyMoveReset, other.lastMove, other.turn, other.moveCount);
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

    public PieceColor getTurn() {
        return turn;
    }

    public void setWhiteKingPosition (Position whiteKingPosition) {
        this.whiteKingPosition = whiteKingPosition;
    }

    public void setBlackKingPosition (Position blackKingPosition) {
        this.blackKingPosition = blackKingPosition;
    }
    public int getMoveCount() {
        return moveCount;
    }

    public void setMoveCount(int moveCount) {
        this.moveCount = moveCount;
    }

    public void setMovesSinceFiftyMoveReset (int movesSinceFiftyMoveReset) {
        this.movesSinceFiftyMoveReset = movesSinceFiftyMoveReset;
    }

    public void setTurn (PieceColor turn) {
        this.turn = turn;
    }

    public void setLastMove (Move lastMove) {
        this.lastMove = lastMove;
    }

    @Override
    public String toString() {
        return "BoardState{" +
                "whiteKingPosition=" + whiteKingPosition +
                ", blackKingPosition=" + blackKingPosition +
                ", movesSinceFiftyMoveReset=" + movesSinceFiftyMoveReset +
                ", lastMove=" + lastMove +
                ", turn=" + turn +
                ", moveCount=" + moveCount +
                '}';
    }
}
