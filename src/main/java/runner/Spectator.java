package runner;

import chess.board.Board;
import chess.piece.basepiece.PieceColor;

public interface Spectator {
    void spectate (Board board, int moveCount, PieceColor turn);
}
