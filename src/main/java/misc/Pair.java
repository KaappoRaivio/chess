package misc;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

public class Pair<K, V> implements Serializable {
    private final K first;
    private final V second;

    public Pair (K first, V second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return getFirst().equals(pair.getFirst()) &&
                getSecond().equals(pair.getSecond());
    }

    @Override
    public int hashCode () {
        return Objects.hash(getFirst(), getSecond());
    }

    @Override
    public String toString () {
        return "{" +
                first + ", " +
                "}";
    }

    public K getFirst () {
        return Optional.ofNullable(first).orElseThrow(() -> new NullPointerException("The first field is null!"));
    }

    public V getSecond () {
        return Optional.ofNullable(second).orElseThrow(() -> new NullPointerException("The second field is null!"));
    }
}
