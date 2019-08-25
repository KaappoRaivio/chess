package players.alphabetaai;

import chess.board.Board;
import chess.misc.Quadruple;
import chess.misc.Span;
import chess.misc.Splitter;
import chess.misc.Triple;
import chess.move.Move;
import chess.piece.basepiece.PieceColor;
import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import players.Player;
import runner.UI;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TreeAIInterface extends Player {
    private static final int sizeLimit = 1000000;

    private static final boolean debug = false;
//    private static final int amountOfCPUs = 1;
    private static final int amountOfCPUs = Runtime.getRuntime().availableProcessors();
    private final int depth;


    volatile private ConcurrentLinkedHashMap<Long, TranspositionTableEntry> transpositionTable;


    public TreeAIInterface(PieceColor color, String name, UI ui, int depth) {
        super(color, name, ui);
        this.depth = depth;

        transpositionTable = new ConcurrentLinkedHashMap.Builder<Long, TranspositionTableEntry>().maximumWeightedCapacity(sizeLimit).build();
    }

    @Override
    public Move getMove() {
        List<Move> allPossibleMoves = new ArrayList<>(board.getAllPossibleMoves(color));
//        List<Move> allPossibleMoves = Collections.singletonList(Move.parseMove("g4h3", PieceColor.BLACK, board));

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
                    thread.getResult().getFirst().forEach(line -> System.out.println(thread.getName() + ": " + line));
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        Set<Triple<Board, Move, Double>> results = new HashSet<>();

        int positionsExamined = 0;
        for (WorkerThread thread : threads) {
            positionsExamined += thread.getResult().getSecond();
            results.addAll(thread.getResult().getFirst());
        }

        long end = System.currentTimeMillis();
        double duration = (end - start) / 1000.0; // seconds

        System.out.println("Took " + duration + " seconds, evaluated " + positionsExamined + " positions in total, " + positionsExamined / duration + " positions per second.");

        Triple<Board, Move, Double> result = results
                .stream()
                .max(Comparator.comparingDouble(Triple::getThird))
                .orElseThrow();

        System.out.println(transpositionTable.size());
        System.out.println(transpositionTable.keySet());
        return result.getSecond();
//        if (color == PieceColor.WHITE) {
//        } else {
//            result = results
//                    .stream()
//                    .min(Comparator.comparingDouble(Quadruple::getThird))
//                    .orElseThrow();
//        }
//
//        System.out.println(result.getThird());
//
//        return result
//                .getSecond();

    }
}
