import chess.board.Board;
import chess.board.BoardNotation;
import chess.move.Move;
import chess.piece.basepiece.PieceColor;
import players.Player;
import players.RandomAI;
import players.TreeAI;
import runner.CapableOfPlaying;
import runner.Runner;
import runner.UI;
import ui.TtyUI;

public class Main {
    public static void main (String[] args) {
//        Board board = Board.fromFile("/home/kaappo/git/chess/src/main/resources/boards/starting_position.txt", BoardNotation.DEFAULT_NOTATION);
        Board board = Board.fromFile("/home/kaappo/git/chess/src/main/resources/boards/pos6.txt", BoardNotation.DEFAULT_NOTATION);
//        board.getAllPossibleMoves(PieceColor.WHITE);
//        System.out.println(board.isCheckMate(PieceColor.WHITE));
//        Board board = Board.fromFile("/home/kaappo/git/chess/src/main/resources/boards/pos5.txt", BoardNotation.DEFAULT_NOTATION);
//        board.makeMove(new Move("e2e4", board));
//        board.undo(1);
//        Board board1 = board.deepCopy();
//        board.makeMove(new Move("e2e4", board));
//        board.undo(1);
//        board.makeMove(new Move("e2e4", board));
//        board.undo(1);
//        System.out.println(board1.equals(board));
//        board.executeMove(new Move("e2e4", board));
//        board.undo(1);
//        board.deepCopy();
//        System.out.println(board);
//        System.exit(0);
//        System.out.println(board.getAllPossibleMoves(PieceColor.WHITE));
//        System.out.println(board);

        /*
        P = 100
        N = 320
        B = 330
        R = 500
        Q = 900
        K = 20000
         */
        System.out.println(board.isCheck(PieceColor.BLACK));
        System.out.println(board.getStateHistory().getCurrentState());
//        System.exit(0);

        UI ui = new TtyUI();
        CapableOfPlaying[] players = {
                new Player(PieceColor.WHITE, "Kaappo", ui),
                new TreeAI(PieceColor.BLACK, "tree ai", ui, 1)
        };

        Runner runner = new Runner(board, players, ui);
        runner.play(PieceColor.WHITE);
    }
}
