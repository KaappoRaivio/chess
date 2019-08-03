package players;

import chess.board.Board;
import chess.misc.Position;
import chess.misc.Triple;
import chess.move.Move;
import chess.piece.basepiece.Piece;
import chess.piece.basepiece.PieceColor;
import misc.Pair;
import runner.UI;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.*;

public class TreeAI extends Player {
    private int depth;
    final private double small;

    public TreeAI (PieceColor color, String name, UI ui, int depth) {
        super(color, name, ui);
        this.depth = depth;

        small = color == PieceColor.WHITE ? -1e10 : 1e10;
    }

    @Override
    public Move getMove () {

        Set<Move> allPossibleMoves = board.getAllPossibleMoves(color);

        List<Triple<Board, Move, Double>> movesAndScores = allPossibleMoves
                .stream()
                .map(move -> new Triple<>(board.deepCopy(), move, 0.0))
                .collect(Collectors.toList())
                .parallelStream()
                .map(this::apply)  // The heavy lifting
                .collect(Collectors.toList());

        System.out.println(movesAndScores);

        return movesAndScores
                .stream()
                .max(Comparator.comparingDouble(Triple::getThird))
                .orElseThrow()
                .getSecond();

    }

    private Map<Move, Board> movesAndPositions = new HashMap<>();

    private double evaluateMove (Board position, Move initialMove) {
        return decisionTree(board, depth, color.invert(), -1e10, 1e10, initialMove);
    }

    private double decisionTree (Board node, int depth, PieceColor turn, double alpha, double beta, Move initialMove) {
        if (depth <= 0) {
//            movesAndPositions.put(initialMove, node.deepCopy());
            return evaluatePosition(node, turn);
        } else if (node.isCheckMate(turn)) {
            System.out.println("Checkmate");
            System.out.println(node);
//            movesAndPositions.put(initialMove, node.deepCopy());
            return small + evaluatePosition(node, turn);
        }

        if (turn == color) {
            double value = -1e10;

            for (Move move : node.getAllPossibleMoves(turn)) {
                node.makeMove(move);

                value = max(value, decisionTree(node, depth - 1, turn.invert(), alpha, beta, initialMove));
                alpha = max(alpha, value);

                node.unMakeMove(1);

                if (alpha >= beta) {
                    break;
                }
            }

            return value;
        } else {
            double value = 1e10;

            for (Move move : node.getAllPossibleMoves(turn)) {
                node.makeMove(move);

                value = min(value, decisionTree(node, depth - 1, turn.invert(), alpha, beta, initialMove));
                beta = min(beta, value);

                node.unMakeMove(1);

                if (alpha >= beta) {
                    break;
                }
            }
            return value;
        }

    }

    private double evaluatePosition (Board position, PieceColor turn) {
        double sum = 0;
        
        for (int y = 0; y < position.getDimY(); y++) {
            for (int x = 0; x < position.getDimX(); x++) {
                Piece piece = board.getPieceInSquare(new Position(x, y));
                sum += piece.getColor() == color ? piece.getValue(new Position(x, y)) : -piece.getValue(new Position(x, y));
            }
        }

        return sum;
    }

    private Triple<Board, Move, Double> apply (Triple<Board, Move, Double> triple) {
        triple.getFirst().makeMove(triple.getSecond());
        Triple<Board, Move, Double> result = new Triple<>(triple.getFirst(), triple.getSecond(), evaluateMove(triple.getFirst(), triple.getSecond()));
        triple.getFirst().unMakeMove(1);
        return result;
    }
}
