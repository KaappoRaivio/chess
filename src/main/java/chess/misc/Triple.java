package chess.misc;

import java.util.Objects;

public class Triple<F, S, T> {
    private F first;
    private S second;
    private T third;

    public Triple (F first, S second, T third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public F getFirst () {
        return first;
    }

    public S getSecond () {
        return second;
    }

    public T getThird () {
        return third;
    }

    @Override
    public String toString () {
        return "Triple{" +
                "first=" + first +
                ", second=" + second +
                ", third=" + third +
                '}';
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof Triple)) return false;
        Triple<?, ?, ?> triple = (Triple<?, ?, ?>) o;
        return getFirst().equals(triple.getFirst()) &&
                getSecond().equals(triple.getSecond()) &&
                getThird().equals(triple.getThird());
    }

    @Override
    public int hashCode () {
        return Objects.hash(getFirst(), getSecond(), getThird());
    }
}
