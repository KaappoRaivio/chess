package players;

public class TranspositionTableEntry {
    private int depth;
    private Bound bound;
    private Double value;

    public TranspositionTableEntry(int depth, Bound bound, Double value) {
        this.depth = depth;
        this.bound = bound;
        this.value = value;
    }

    public int getDepth() {
        return depth;
    }

    public Bound getBound() {
        return bound;
    }

    public Double getValue() {
        return value;
    }
}
