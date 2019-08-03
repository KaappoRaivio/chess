package chess.board;

import chess.board.Board;
import chess.misc.exceptions.ChessException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

// https://stackoverflow.com/a/28254483

public class RepetitionTracker implements Serializable {
    private final Map<Integer, Integer> counts = new HashMap<>();
    private boolean draw = false;

    public void add (Board value) {
        counts.merge(value.hashCode(), 1, Integer::sum);
        if (count(value) == 3) {
            draw = true;
//            throw new DrawException();
        }

    }

    public void subtract (Board value) {
        if (!counts.containsKey(value.hashCode())) {
            throw new ChessException("Position \n" + value + " not known!" + value.hashCode());
        }
        counts.merge(value.hashCode(), -1, Integer::sum);
        if (count(value) < 3) {
            draw = false;
        }
    }

    private int count (Board value) {
        return counts.getOrDefault(value.hashCode(), 0);
    }

    public boolean isDraw () {
        return draw;
    }

    @Override
    public String toString () {
        return counts.toString();
    }
}