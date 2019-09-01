package chess.misc;

import java.util.Objects;

public class Quadruple<First, Second, Third, Fourth> {
    final private First first;
    final private Second second;
    final private Third third;
    final private Fourth fourth;

    public Quadruple (First first, Second second, Third third, Fourth fourth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
    }

    public Quadruple (Triple<First, Second, Third> rest, Fourth fourth) {
        this(rest.getFirst(), rest.getSecond(), rest.getThird(), fourth);
    }

    public Quadruple (First first, Triple<Second, Third, Fourth> rest) {
        this(first, rest.getFirst(), rest.getSecond(), rest.getThird());
    }

    public First getFirst() {
        return first;
    }

    public Second getSecond() {
        return second;
    }

    public Third getThird() {
        return third;
    }

    public Fourth getFourth() {
        return fourth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quadruple<?, ?, ?, ?> quadruple = (Quadruple<?, ?, ?, ?>) o;
        return first.equals(quadruple.first) &&
                second.equals(quadruple.second) &&
                third.equals(quadruple.third) &&
                fourth.equals(quadruple.fourth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second, third, fourth);
    }

    @Override
    public String toString() {
        return "Quadruple{" +
                "first=" + first +
                ", second=" + second +
                ", third=" + third +
                ", fourth=" + fourth +
                '}';
    }
}
