package players.alphabetaai;

import chess.board.Board;
import chess.misc.Position;
import chess.misc.Quadruple;
import chess.move.Move;
import chess.piece.basepiece.Piece;
import chess.piece.basepiece.PieceColor;
import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import misc.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class TreeAIEngine implements AI {
    private int depth;
    volatile private ConcurrentLinkedHashMap<Long, TranspositionTableEntry> transpositionTable;
    private PieceColor color;
    private int tableHits = 0;

    TreeAIEngine (int depth, PieceColor color, ConcurrentLinkedHashMap<Long, TranspositionTableEntry> transpositionTable) {
        this.depth = depth;
        this.color = color;

        this.transpositionTable = transpositionTable;
    }
    private Pair<Double, Integer> searchAndEvaluate(Board board, PieceColor initialTurn) {
        return decisionTree(board, depth, initialTurn == color, -1e10, 1e10, 0);
    }

    private Pair<Double, Integer> decisionTree(Board board, int depth, boolean maximizingPlayer, double alpha, double beta, int positionsExamined) {
        double alphaOrig = alpha;
        double  betaOrig = beta;

        if (hasValidEntry(board)) {
            var entry = getEntry(board);
            if (entry.getDepth() >= depth) {
                switch (entry.getBound()) {
                    case EXACT:
//                            System.out.println("Exact transposition table hit directly!");
                        tableHits += 1;
                        return new Pair<>(entry.getValue(), positionsExamined);
                    case LOWER:
//                            System.out.println("Lower transposition table hit!");
                        tableHits += 1;
                        alpha = max(alpha, entry.getValue());
                        break;
                    case UPPER:
                        tableHits += 1;
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
                System.out.println((depth + 1) * (maximizingPlayer ? 1e10 : -1e10));

                // Checkmates closer to the origin position are valued more
                return new Pair<>((depth + 1) * (maximizingPlayer ? 1e10 : -1e10), positionsExamined);

            } else {
                // Draw
                return new Pair<>(0.0, 0);
            }
        }
        if (depth <= 0) {
//            if (board.isCheck(currentPlayerColor) && realdepth < hardlimit()) {
//                // If it's check, go two further
//                depth += 3;
//            } else {
                return new Pair<>(evaluateCurrentPosition(board), positionsExamined);
//            }
        }

        if (maximizingPlayer) {
            double value = -1e10;

            for (Move move : moves) {
                board.makeMove(move);

                Pair<Double, Integer> result = decisionTree(board, depth - 1, false, alpha, beta, positionsExamined);
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
            transpositionTableStore(board, new TranspositionTableEntry(depth, bound, value));

            return new Pair<>(value, positionsExamined);

        } else {
            double value = 1e10;

            for (Move move : moves) {
                board.makeMove(move);

                Pair<Double, Integer> result = decisionTree(board, depth - 1, true, alpha, beta, positionsExamined);
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
            transpositionTableStore(board, new TranspositionTableEntry(depth, bound, value));

            return new Pair<>(value, positionsExamined);
        }

    }

    private void transpositionTableStore (Board board, TranspositionTableEntry entry) {
        transpositionTable.put(board.customHashCode(), entry);
    }

    private boolean hasValidEntry (Board board) {
        return transpositionTable.containsKey(board.customHashCode());
    }

    private TranspositionTableEntry getEntry (Board board) {
        return Optional.ofNullable(transpositionTable.get(board.customHashCode())).orElseThrow();
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

        return sum;
    }

    @Override
    public List<Quadruple<Board, Move, Double, Integer>> callback (List<Move> movesToTest, Board board) {
        List<Quadruple<Board, Move, Double, Integer>> result = new ArrayList<>();

        for (Move move : movesToTest) {
            board.makeMove(move);
            var pair = searchAndEvaluate(board, color.invert());
            result.add(new Quadruple<>(board.deepCopy(), move, pair.getFirst(), pair.getSecond()));
            board.unMakeMove(1);
        }

        return result;
    }

    int getTableHits() {
        return tableHits;
    }
}
