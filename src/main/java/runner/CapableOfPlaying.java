package runner;

import chess.board.Board;
import chess.move.Move;
import chess.piece.basepiece.PieceColor;

public interface CapableOfPlaying {
    Move getMove ();
    void updateValues (Board board, PieceColor turn, int moveCount);
    PieceColor getColor ();
}
