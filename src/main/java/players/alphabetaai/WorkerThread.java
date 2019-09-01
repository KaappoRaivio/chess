package players.alphabetaai;

import chess.board.Board;
import chess.misc.Quadruple;
import chess.move.Move;

import java.util.List;

public class WorkerThread extends Thread {
    private static int count = 0;

    private List<Move> movesToTest;
    private Board board;
    volatile private AI callback;
    private List<Quadruple<Board, Move, Double, Integer>> result;

    WorkerThread(List<Move> movesToTest, Board board, AI callback) {
        super("User-generated worker thread " + ++count);

        this.movesToTest = movesToTest;
        this.board = board;
        this.callback = callback;
    }

    @Override
    public void run() {
        result = callback.callback(movesToTest, board);
    }

    List<Quadruple<Board, Move, Double, Integer>> getResult () {
        System.out.println("Table hits for " + getName() + ": " + ((TreeAIEngine) callback).getTableHits());
        return result;
    }
}
