package players.alphabetaai;

import chess.board.Board;
import chess.misc.Quadruple;
import chess.move.Move;

import java.util.List;

public interface AI {
    List<Quadruple<Board, Move, Double, Integer>> callback(List<Move> movesToTest, Board board);
}
