package players.alphabetaai;

import chess.board.Board;
import chess.misc.Quadruple;
import chess.misc.Span;
import chess.misc.Splitter;
import chess.move.Move;
import chess.piece.basepiece.PieceColor;
import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import players.Player;
import runner.UI;

import java.util.*;

public class TreeAIInterface extends Player {
    private static final int sizeLimit = 1000000;

    private static final boolean debug = false;
    private static final int amountOfCPUs = Runtime.getRuntime().availableProcessors();
    private final int depth;


    private ConcurrentLinkedHashMap<Long, TranspositionTableEntry> transpositionTable;


    public TreeAIInterface(PieceColor color, String name, UI ui, int depth) {
        super(color, name, ui);
        this.depth = depth;

        transpositionTable = new ConcurrentLinkedHashMap.Builder<Long, TranspositionTableEntry>().maximumWeightedCapacity(sizeLimit).build();
    }

    @Override
    public Move getMove() {
        List<Move> allPossibleMoves = new ArrayList<>(board.getAllPossibleMoves(color));

        List<WorkerThread> threads = new ArrayList<>();


        for (Span span : Splitter.splitListInto(allPossibleMoves, amountOfCPUs)) {
            WorkerThread thread = new WorkerThread(allPossibleMoves.subList(span.getStart(), span.getEnd()), board.deepCopy(), new TreeAIEngine(depth, color, transpositionTable));
            thread.start();
            threads.add(thread);
        }

        long start = System.currentTimeMillis();

        for (WorkerThread thread : threads) {
            try {
                thread.join();
                System.out.println("Joined " + thread.getName() + "!");
                if (debug) {
                    thread.getResult().forEach(line -> System.out.println(thread.getName() + ": " + line));
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        Set<Quadruple<Board, Move, Double, Integer>> results = new HashSet<>();

        for (WorkerThread thread : threads) {
            results.addAll(thread.getResult());
        }

        long end = System.currentTimeMillis();
        double duration = (end - start) / 1000.0; // seconds


        int positionsExamined = results
                .stream()
                .mapToInt(Quadruple::getFourth)  // The fourth item is the amount of positions tested for each of the moves in the set, so we sum those up to get the total.
                .reduce((x, y) -> x + y)
                .orElseThrow();

        System.out.println("Took " + duration + " seconds, evaluated " + positionsExamined + " positions in total, " + positionsExamined / duration + " positions per second.");

        return results
                .stream()
                .max(Comparator.comparingDouble(Quadruple::getThird))
                .orElseThrow()
                .getSecond();

    }
}