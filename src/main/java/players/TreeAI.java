package players;

import chess.board.Board;
import chess.misc.Position;
import chess.misc.Triple;
import chess.move.Move;
import chess.piece.basepiece.Piece;
import chess.piece.basepiece.PieceColor;
import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import misc.Pair;
import runner.Spectator;
import runner.UI;

import java.lang.ref.WeakReference;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.*;

public class TreeAI extends Player implements Spectator {
    private static final boolean debug = false;
    private int depth;

    private ConcurrentLinkedHashMap<Long, TranspositionTableEntry> transpositonTable;
    private static final int sizeLimit = 1000000;

    public TreeAI (PieceColor color, String name, UI ui, int depth) {
        super(color, name, ui);
        this.depth = depth;
        ConcurrentLinkedHashMap.Builder<Long, TranspositionTableEntry> builder = new ConcurrentLinkedHashMap.Builder<>();
        builder.maximumWeightedCapacity(sizeLimit);
        transpositonTable = builder.build();
    }

    @Override
    public Move getMove () {

        Set<Move> allPossibleMoves = board.getAllPossibleMoves(color);


        long start = System.currentTimeMillis();

        List<Triple<Board, Move, Pair<Double, Integer>>> movesAndScores = allPossibleMoves
                .stream()
                .map(move -> new Triple<>(board.deepCopy(), move, 0.0))
                .collect(Collectors.toList())
                .parallelStream()
//                .stream()
                .map(this::apply)  // The heavy lifting
                .collect(Collectors.toList());

        long end = System.currentTimeMillis();
        System.out.println("Time elapsed: " + (end - start) + " ms");

        if (debug) {
            System.out.println(movesAndScores);
        }

        int positionsExamined = movesAndScores
                .stream()
                .mapToInt(boardMovePairTriple -> boardMovePairTriple.getThird().getSecond())
                .reduce((x, y) -> x + y)
                .orElseThrow();

        System.out.println("Examined " + positionsExamined + ", " + (Integer.valueOf(positionsExamined).doubleValue() / (end - start)) * 1000 + "per second");

        return movesAndScores
                .stream()
                .max(Comparator.comparingDouble(key -> key.getThird().getFirst()))
                .orElseThrow()
                .getSecond();

    }


    private Pair<Double, Integer> searchAndEvaluate(Board board, Move initialMove, PieceColor initialTurn) {
        return decisionTree(board, depth, initialTurn == color, -1e10, 1e10, 0, 0);
    }

    private int hardlimit () {
        return depth + 3;
    }

    private Pair<Double, Integer> decisionTree(Board board, int depth, boolean maximizingPlayer, double alpha, double beta, int realdepth, int positionsExamined) {
        double alphaOrig = alpha;
        double  betaOrig = beta;

        if (hasValidEntry(board)) {
            var entry = getEntry(board);
            if (entry.getDepth() >= (hardlimit() - realdepth)) {
                switch (entry.getBound()) {
                    case EXACT:
//                            System.out.println("Exact transposition table hit directly!");
                        return new Pair<>(entry.getValue(), positionsExamined);
                    case LOWER:
//                            System.out.println("Lower transposition table hit!");
                        alpha = max(alpha, entry.getValue());
                        break;
                    case UPPER:
//                            System.out.println("Upper transposition table hit!");
                        beta = min(beta, entry.getValue());
                        break;
                }

                if (alpha >= beta) {
//                    System.out.println("Exact transposition table hit!");
                    return new Pair<>(entry.getValue(), positionsExamined);
                }
            }
        }

        PieceColor currentPlayerColor = maximizingPlayer ? color : color.invert();
        var moves = board.getAllPossibleMoves(currentPlayerColor);
        positionsExamined += 1;

        if (board.isDrawNoStalemateCheck(currentPlayerColor) ||  moves.size() == 0) {
            if (board.isCheck(currentPlayerColor)) {
                System.out.println("Checkmate for " + currentPlayerColor);
                System.out.println(board);
                System.out.println(board.getMoveHistoryPretty());
                System.out.println((hardlimit() - realdepth + 1) * (maximizingPlayer ? 1e10 : -1e10));

                // Checkmates closer to the origin position are valued more
                return new Pair<>((hardlimit() - realdepth + 1) * (maximizingPlayer ? 1e10 : -1e10), positionsExamined);

            } else {
                // Draw
                return new Pair<>(0.0, 0);
            }
        }
        if (depth <= 0) {
            if (board.isCheck(currentPlayerColor) && realdepth < hardlimit()) {
                // If it's check, go two further
                depth += 3;
            } else {
                return new Pair<>(evaluateCurrentPosition(board), positionsExamined);
            }
        }

        if (maximizingPlayer) {
            double value = -1e10;

            for (Move move : moves) {
                board.makeMove(move);

                Pair<Double, Integer> result = decisionTree(board, depth - 1, false, alpha, beta, realdepth + 1, positionsExamined);
                value = max(value, result.getFirst());
                alpha = max(value, alpha);

                positionsExamined = result.getSecond();

                board.unMakeMove(1);

                if (alpha >= beta) {
                    break;
                }
            }

            Bound bound;
            if (value >= alphaOrig) {
                bound = Bound.UPPER;
            } else if (value <= betaOrig) {
                bound = Bound.LOWER;
            } else {
                bound = Bound.EXACT;
            }
            transpositionTableStore(board, new TranspositionTableEntry((hardlimit() - realdepth), bound, value));

            return new Pair<>(value, positionsExamined);

        } else {
            double value = 1e10;

            for (Move move : moves) {
                board.makeMove(move);

                Pair<Double, Integer> result = decisionTree(board, depth - 1, true, alpha, beta, realdepth + 1, positionsExamined);
                value = min(value, result.getFirst());
                beta = min(value, beta);

                positionsExamined = result.getSecond();

                board.unMakeMove(1);

                if (alpha >= beta) {
                    break;
                }
            }

            Bound bound;
            if (value <= alphaOrig) {
                bound = Bound.UPPER;
            } else if (value >= betaOrig) {
                bound = Bound.LOWER;
            } else {
                bound = Bound.EXACT;
            }
            transpositionTableStore(board, new TranspositionTableEntry((hardlimit() - realdepth), bound, value));

            return new Pair<>(value, positionsExamined);
        }

    }

    private void transpositionTableStore (Board board, TranspositionTableEntry entry) {
        transpositonTable.put(board.customHashCode(), entry);
    }

    private boolean hasValidEntry (Board board) {
        return transpositonTable.containsKey(board.customHashCode());
    }

    private TranspositionTableEntry getEntry (Board board) {
        return Optional.ofNullable(transpositonTable.get(board.customHashCode())).orElseThrow();
    }

    private double evaluateCurrentPosition(Board board) {
        double sum = 0;
        
        for (int y = 0; y < board.getDimY(); y++) {
            for (int x = 0; x < board.getDimX(); x++) {
                Position position = new Position(x, y);
                Piece piece = board.getPieceInSquare(position);

                sum += piece.getColor() == color ? piece.getValue(position) : -piece.getValue(position);
            }
        }

//        return maximizingPlayer ? sum : -sum;
        return sum;
    }

    private Triple<Board, Move, Pair<Double, Integer>> apply (Triple<Board, Move, Double> triple) {
        triple.getFirst().makeMove(triple.getSecond());

        Triple<Board, Move, Pair<Double, Integer>> result = new Triple<>(triple.getFirst().deepCopy(),
                triple.getSecond(),
                searchAndEvaluate(triple.getFirst(),
                        triple.getSecond(),
                        turn.invert()
                )
        );

        triple.getFirst().unMakeMove(1);
        return result;
    }

    public static void main(String... args) {
        TreeAI treeAI = new TreeAI(PieceColor.BLACK, "asd", null, 2);

        Board board = Board.fromFile("/home/kaappo/git/chess/src/main/resources/boards/pos8.txt");
        System.out.println(board);
        treeAI.updateValues(board.deepCopy(), PieceColor.BLACK, 1);
        System.out.println(treeAI.evaluateCurrentPosition(board));
        System.out.println(treeAI.getMove());


//        System.out.println(treeAI.evaluateCurrentPosition(board, PieceColor.WHITE));
//        System.out.println(treeAI.evaluateCurrentPosition(board, PieceColor.BLACK));
    }

    @Override
    public void spectate(Board board, int moveCount, PieceColor turn) {
        System.out.println("\tPosition from " + color + "'s perspective: " + evaluateCurrentPosition(board));
//        System.out.println("\tSearch tree from " + color + "'s perspective: " + decisionTree(board, depth + 1, turn == color, -1e10, 1e10, 0, 0));

    }
}
