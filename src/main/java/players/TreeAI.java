package players;

import chess.board.Board;
import chess.misc.Position;
import chess.misc.Triple;
import chess.move.Move;
import chess.piece.basepiece.Piece;
import chess.piece.basepiece.PieceColor;
import runner.Spectator;
import runner.UI;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.*;

public class TreeAI extends Player implements Spectator {
    private static final boolean debug = false;


    private int depth;

    public TreeAI (PieceColor color, String name, UI ui, int depth) {
        super(color, name, ui);
        this.depth = depth;
    }

    @Override
    public Move getMove () {

        Set<Move> allPossibleMoves = board.getAllPossibleMoves(color);


        long start = System.currentTimeMillis();

        List<Triple<Board, Move, Double>> movesAndScores = allPossibleMoves
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

        return movesAndScores
                .stream()
                .max(Comparator.comparingDouble(Triple::getThird))
                .orElseThrow()
                .getSecond();

    }


    private double searchAndEvaluate(Board board, Move initialMove, PieceColor initialTurn) {
        return decisionTree(board, depth, initialTurn == color, -1e10, 1e10, initialMove, 0);
    }

    private static final int hardlimit = 6;

    private double decisionTree (Board board, int depth, boolean maximizingPlayer, double alpha, double beta, Move initialMove, int realdepth) {
        PieceColor currentPlayerColor = maximizingPlayer ? color : color.invert();
        var moves = board.getAllPossibleMoves(currentPlayerColor);

        if (board.isDrawNoStalemateCheck(currentPlayerColor) ||  moves.size() == 0) {
            if (board.isCheck(currentPlayerColor)) {
                System.out.println("Checkmate for " + currentPlayerColor);
                System.out.println(board);
                System.out.println(board.getMoveHistoryPretty());

                // Checkmates closer to the origin position are valued more
                return depth * (maximizingPlayer ? -1e10 : 1e10);

            } else {
                // Draw
                return 0;
            }
        }
//        System.out.println(maximizingPlayer);
        if (depth <= 0) {
            if (board.isCheck(currentPlayerColor) && realdepth < hardlimit) {
                // If it's check, go two further
//                System.out.println("going further!");
                depth += 3;
            } else {
//                System.out.println("not going further!");
                return evaluateCurrentPosition(board);
            }
        }



        if (maximizingPlayer) {
            double value = -1e10;

            for (Move move : moves) {
                board.makeMove(move);

                value = max(value, decisionTree(board, depth - 1, false, alpha, beta, initialMove, realdepth + 1));
                alpha = max(value, alpha);

                board.unMakeMove(1);

                if (alpha >= beta) {
                    break;
                }
            }

            return value;

        } else {
            double value = 1e10;

            for (Move move : moves) {
                board.makeMove(move);

                value = min(value, decisionTree(board, depth - 1, true, alpha, beta, initialMove, realdepth + 1));
                beta  = min(value, beta);

                board.unMakeMove(1);

                if (alpha >= beta) {
                    break;
                }
            }

            return value;
        }

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

    private Triple<Board, Move, Double> apply (Triple<Board, Move, Double> triple) {
        triple.getFirst().makeMove(triple.getSecond());

        Triple<Board, Move, Double> result = new Triple<>(triple.getFirst().deepCopy(),
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
    }
}
