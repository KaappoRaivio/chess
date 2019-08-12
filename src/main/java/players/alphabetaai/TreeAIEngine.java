package players.alphabetaai;

import chess.board.Board;
import chess.misc.Position;
import chess.misc.Quadruple;
import chess.misc.Triple;
import chess.move.Move;
import chess.piece.Bishop;
import chess.piece.basepiece.Piece;
import chess.piece.basepiece.PieceColor;
import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class TreeAIEngine implements AI {
    private int depth;
    volatile private ConcurrentLinkedHashMap<Long, TranspositionTableEntry> transpositionTable;
    private int tableHits = 0;
    private int searchedPositions = 0;

    TreeAIEngine (int depth, PieceColor color, ConcurrentLinkedHashMap<Long, TranspositionTableEntry> transpositionTable) {
        this.depth = depth;

        this.transpositionTable = transpositionTable;
    }
    private double searchAndEvaluate(Board board) {
        return negamax(board, depth, -1e10, 1e10, board.getTurn());
    }

    private double negamax (Board board, int depth, double alpha, double beta, PieceColor turn) {
        searchedPositions += 1;
        double alphaOrig = alpha;

//        if (hasValidEntry(board)) {
//            var entry = getEntry(board);
//            if (entry.getDepth() >= depth) {
//                switch (entry.getBound()) {
//                    case EXACT:
//                        tableHits += 1;
//
//                        return entry.getValue();
//                    case LOWER:
//                        tableHits += 1;
//                        alpha = max(alpha, entry.getValue());
//                        break;
//                    case UPPER:
//                        tableHits += 1;
//                        beta = min(beta, entry.getValue());
//                        break;
//                }
//
//                if (alpha >= beta) {
//
//                    return entry.getValue();
//                }
//            }
//        }
//


        if (board.isDraw(turn)) {
            // 0.0 for draw
            return 0.0;
        } else if (board.isCheckMate(turn)) {
            // Checkmates closer to the origin position are valued more
            if (board.getPieceInSquare(Position.fromString("h3")).equals(new Bishop(PieceColor.BLACK))) {
                System.out.println("Breakpoint!");
            }

            System.out.println("Checkmate for " + turn);
            System.out.println(board);
            System.out.println(board.getMoveHistoryPretty());
            System.out.println((depth + 1) * (turn == PieceColor.WHITE ? -1e10 : 1e10) + ", " + (turn == PieceColor.WHITE));



            return (depth + 1) * (turn == PieceColor.WHITE ? -1e10 : 1e10);
        } else if (depth <= 0) {
//            System.out.println("Evaluating board!");
//            System.out.println(board);
//            System.out.println(evaluateCurrentPosition(board) * turn.getValue());
            return turn.getValue() * evaluateCurrentPosition(board);
        }

        double value = -1e10;

        for (Move move : board.getAllPossibleMoves(turn)) {
            board.makeMove(move);

            double result = negamax(board, depth - 1, -beta, -alpha, turn.invert());
            if (Math.abs(result) > Math.abs(value)) {
                value = -result;
            }
//            value = max(value, -result);
            alpha = max(value, alpha);

            board.unMakeMove(1);

//            if (Math.abs(alpha) >= Math.abs(beta)) {
//                break;
//            }
        }

//        Bound bound;
//        if (value <= alphaOrig) {
//            bound = Bound.UPPER;
//        } else if (value >= beta) {
//            bound = Bound.LOWER;
//        } else {
//            bound = Bound.EXACT;
//        }
//        transpositionTableStore(board, new TranspositionTableEntry(depth, bound, value));


        return value;
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

                sum += piece.getColor() == PieceColor.WHITE ? piece.getValue(position) : -piece.getValue(position);
            }
        }

        return sum;
    }

    @Override
    public List<Quadruple<Board, Move, Double, Integer>> callback (List<Move> movesToTest, Board board) {
        searchedPositions = 0;
        List<Triple<Board, Move, Double>> result = new ArrayList<>();

        for (Move move : movesToTest) {
            board.makeMove(move);
            if (Move.parseMove("G4h3", PieceColor.BLACK, board).equals(move)) {
                System.out.println("moiasd");
            }

            result.add(new Triple<>(board.deepCopy(), move, searchAndEvaluate(board)));
            board.unMakeMove(1);
        }
        System.out.println(result);

        return result
                .stream()
                .map(triple -> new Quadruple<>(triple.getFirst(), triple.getSecond(), triple.getThird(), searchedPositions))
                .collect(Collectors.toList());
    }

    int getTableHits() {
        return tableHits;
    }
}
