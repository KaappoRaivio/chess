package chess.misc;

import chess.misc.exceptions.ChessException;
import chess.misc.exceptions.DrawException;

import java.util.HashMap;
import java.util.Map;

// https://stackoverflow.com/a/28254483

public class Counter<T> {
    private final Map<T, Integer> counts = new HashMap<>();

    public void add (T value) {
        counts.merge(value, 1, Integer::sum);
        if (count(value) == 4) {
            System.out.println(this);
            throw new DrawException();
        }
    }

    public void subtract (T value) {
        if (!counts.containsKey(value)) {
            throw new ChessException("Position " + value + " not known!");
        }
        counts.merge(value, -1, Integer::sum);
    }

    private int count (T value) {
        return counts.getOrDefault(value, 0);
    }

    @Override
    public String toString () {
        return counts.toString();
    }
}