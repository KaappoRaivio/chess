package players.alphabetaai;

import chess.board.Board;
import chess.misc.Triple;
import chess.move.Move;
import misc.Pair;

import java.util.List;

public interface AI {
    Pair<List<Triple<Board, Move, Double>>, Integer> callback(List<Move> movesToTest, Board board);
}
