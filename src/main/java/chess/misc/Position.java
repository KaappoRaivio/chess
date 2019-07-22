package chess.misc;

import misc.Pair;

import java.util.regex.Pattern;

public class Position extends Pair<Integer, Integer> {
    private static final Pattern stringPattern = Pattern.compile("^[a-hA-H]\\d$");

    public Position (int x, int y) {
        this(x, y, true);
    }

    public Position (int x, int y, boolean check) {
        super(check ? limit(0, x, 8) : x, check ? limit(0, y, 8) : y);
    }

    public static Position fromString (String position) {
        if (! stringPattern.matcher(position).matches()) {
            throw new ChessException("String " + position + " is not a valid notation for a board position!");
        }

        int x = position.toUpperCase().charAt(0) - 65; //  65 for converting from ASCII to int
        int y = Integer.parseInt(String.valueOf(position.charAt(1))) - 1;

        return new Position(x, y);
    }

    public Position offset (int x, int y) {
        return offset(x, y, true);
    }

    public Position offset (int x, int y, boolean check) {
        return offsetX(x, check).offsetY(y, check);
    }

    public Position offset (Position position) {
        return offset(position, true);
    }

    public Position offset (Position position, boolean check) {
        return offset(position.getX(), position.getY(), check);
    }

    public Position offsetX (int x) {
        return offsetX(x, true);
    }

    public Position offsetX (int x, boolean check) {
        return new Position(getX() + x, getY(), check);
    }

    public Position offsetY (int y) {
        return offsetY(y, true);
    }

    public Position offsetY (int y, boolean check) {
        return new Position(getX(), getY() + y, check);
    }

    public int getX () {
        return super.getFirst();
    }

    public int getY () {
        return super.getSecond();
    }

    private static int limit (int lower, int value, int higher) {
        if (value < lower) {
            throw new ChessException("Value " + value + " is not within its bounds! (too low)");
        } else if (value >= higher) {
            throw new ChessException("Value " + value + " is not within its bounds! (too high)");
        } else {
            return value;
        }
    }

    @Override
    public String toString () {
        char xString = (char) (getX() + 65);
        return xString + "" + (getY() + 1);
    }
}
