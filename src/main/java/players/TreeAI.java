package players;

import chess.board.Board;
import chess.misc.Position;
import chess.move.Move;
import chess.piece.basepiece.Piece;
import chess.piece.basepiece.PieceColor;
import runner.UI;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static chess.piece.basepiece.PieceColor.WHITE;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class TreeAI extends Player {
    private int depth;

    public TreeAI (PieceColor color, String name, UI ui, int depth) {
        super(color, name, ui);
        this.depth = depth;
    }

    @Override
    public Move getMove () {

        Move bestMove = null;
        double bestScore = -1e11;

        for (Move move : board.getAllPossibleMoves(color)) {
            board.makeMove(move);
            double score = evaluateMove(board);
            if (score > bestScore) {
                bestMove = move;
            }
            board.unMakeMove(1);
        }

        return Optional.ofNullable(bestMove).orElseThrow();

    }

    private Map<Move, Board> movesAndPositions = new HashMap<>();

    private double evaluateMove (Board position) {
        return decisionTree(board, depth, color.invert(), -1e10, 1e10, null);
    }

    private double decisionTree (Board node, int depth, PieceColor turn, double alpha, double beta, Move initialMove) {
        if (depth <= 0) {
            movesAndPositions.put(initialMove, node.deepCopy());
        } else if (node.isCheckMate(turn)) {
            movesAndPositions.put(initialMove, node.deepCopy());
            return -1e10 + evaluatePosition(node, turn);
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
                sum += piece.getColor() == turn ? piece.getValue() : -piece.getValue();
            }
        }

        return sum;
    }
}
