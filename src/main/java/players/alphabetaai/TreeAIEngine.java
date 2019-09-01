package players.alphabetaai;

import chess.board.Board;
import chess.misc.Position;
import chess.misc.Quadruple;
import chess.move.Move;
import chess.piece.basepiece.Piece;
import chess.piece.basepiece.PieceColor;
import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class TreeAIEngine implements AI {
    private volatile double globalAlpha = -1e10;
    private volatile double globalBeta  = +1e10;


    private int depth;
    private final PieceColor color;
    volatile private ConcurrentLinkedHashMap<Long, TranspositionTableEntry> transpositionTable;
    private int tableHits = 0;
    private int searchedPositions = 0;

    TreeAIEngine (int depth, PieceColor color, ConcurrentLinkedHashMap<Long, TranspositionTableEntry> transpositionTable) {
        this.depth = depth;
        this.color = color;

        this.transpositionTable = transpositionTable;

    }
    private double searchAndEvaluate(Board board) {
//        System.out.println(board.getTurn());
        return minimax(board, depth, board.getTurn() == color, -1e10, +1e10);
    }

    private double minimax (Board board, int depth, boolean maximizingPlayer, double alpha, double beta) {
        ++searchedPositions;
        PieceColor turn = maximizingPlayer ? color : color.invert();

        if (depth <= 0 || board.isEndOfGame(turn)) {
            double value = evaluateCurrentPosition(board);
            transpositionTableStore(board, new TranspositionTableEntry(depth, Bound.EXACT, value));
            return value;
        }

        double alphaOrig = alpha;
        double  betaOrig = beta;

        if (hasValidEntry(board)) {
            var entry = getEntry(board);
            if (entry.getDepth() >= depth) {
                switch (entry.getBound()) {
                    case EXACT:
                        tableHits += 1;
                        return entry.getValue();
                    case LOWER:
                        alpha = max(alpha, entry.getValue());
                        break;
                    case UPPER:
                        beta = min(beta, entry.getValue());
                        break;
                }

                if (alpha >= beta) {
                    tableHits += 1;
                    return entry.getValue();
                }
            }
        }


        double value;
        if (maximizingPlayer) {
            value = -1e10;

            for (Move move : board.getAllPossibleMoves(turn)) {
                board.makeMove(move);

                value = max(value, minimax(board, depth - 1, false, alpha, beta));
                alpha = max(value, alpha);

                board.unMakeMove(1);

                if (alpha >= beta) {
                    break;
                }
            }
        } else {
            value = +1e10;

            for (Move move : board.getAllPossibleMoves(turn)) {
                board.makeMove(move);

                value = min(value, minimax(board, depth - 1, true, alpha, beta));
                 beta = min(value, beta);

                board.unMakeMove(1);

                if (alpha >= beta) {
                    break;
                }
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
        return value;
    }


    private void transpositionTableStore (Board board, TranspositionTableEntry entry) {
        transpositionTable.put(board.customHashCode(), entry);
    }

    private boolean hasValidEntry (Board board) {
        return transpositionTable.get(board.customHashCode()) != null;
    }

    private TranspositionTableEntry getEntry (Board board) {
        return Optional.ofNullable(transpositionTable.get(board.customHashCode())).orElseThrow();
    }

    private double evaluateCurrentPosition(Board board) {
        if (board.isDraw(board.getTurn())) {
            return 0.0;
        } else if (board.isCheckMate(color)) {
            System.out.println("Checkmate for self: " + board);
            return -1e10;
        } else if (board.isCheckMate(color.invert())) {
            System.out.println("Checkmate for opponent: " + board);
            return 1e10;
        }

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
        searchedPositions = 0;
        tableHits = 0;
        List<Quadruple<Board, Move, Double, Integer>> result = new ArrayList<>();

        for (Move move : movesToTest) {
            board.makeMove(move);

            searchedPositions = 0;
            double value = searchAndEvaluate(board);
            result.add(new Quadruple<>(board.deepCopy(), move, value, searchedPositions));

            board.unMakeMove(1);
        }
        System.out.println(result);

        return result;
    }

    int getTableHits() {
        return tableHits;
    }
}
