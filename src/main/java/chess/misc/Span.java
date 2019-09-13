package chess.misc;

import chess.misc.exceptions.NotImplementedError;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class Span implements Iterable<Integer> {
    private int start;
    private int end;

    Span (int start, int end) {
        this.start = start;
        this.end = end;
    }

    private boolean isIn (int position) {
        return position >= start && position < end;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<>() {
            int current = start - 1;

            @Override
            public boolean hasNext() {
                return isIn(current + 1);
            }

            @Override
            public Integer next() {
                current += 1;
                return current;
            }
        };
    }

    @Override
    public void forEach(Consumer<? super Integer> action) {
        for (int a : this) {
            action.accept(a);
        }
    }

    @Override
    public Spliterator<Integer> spliterator() {
        throw new RuntimeException(new NotImplementedError());
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }
}
