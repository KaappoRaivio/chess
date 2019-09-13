package runner;

import chess.board.Board;
import chess.move.Move;
import chess.piece.basepiece.PieceColor;

public interface UI {
    void updateValues (Board board, PieceColor turn, int moveCount);
    Move getMove (PieceColor color);

    void commit ();
    void close ();
}
