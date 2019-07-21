package chess.move;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class MoveHistory {
    private Queue<Move> moves = new LinkedList<>();

    public void addMove (Move move) {
        moves.add(move);
    }
}
