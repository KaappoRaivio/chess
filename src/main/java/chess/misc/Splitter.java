package chess.misc;

import java.util.ArrayList;
import java.util.List;

public class Splitter {
    public static <T> List<Span> splitListInto (List<T> list, int amountOfChunks) {
        int surfaceArea = list.size();


        int[] lengths = new int[amountOfChunks];
        int remainder = surfaceArea % amountOfChunks;

        for (int i = 0; i < amountOfChunks; i++) {
            lengths[i] = surfaceArea / amountOfChunks;
        }

        for (int i = 0; i < remainder; i++) {
            lengths[i] += 1;
        }

        List<Span> spans = new ArrayList<>();

        int x = 0;

        for (int length : lengths) {
            spans.add(new Span(x, x + length));
            x += length;
        }

        return spans;
    }
}
