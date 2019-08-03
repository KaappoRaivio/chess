import chess.board.Board;
import chess.board.BoardNotation;
import chess.misc.exceptions.StopException;
import chess.move.Move;
import chess.piece.basepiece.PieceColor;
import misc.Saver;
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
        Board board = Board.fromFile("/home/kaappo/git/chess/src/main/resources/boards/pos8.txt", BoardNotation.DEFAULT_NOTATION);
//        board.makeMove(Move.parseMove("e2e4", PieceColor.WHITE, board));
//        board.unMakeMove(1);

        /*
        P = 100
        N = 320
        B = 330
        R = 500
        Q = 900
        K = 20000
         */
//        System.out.println(board.isCheck(PieceColor.BLACK));
//        System.out.println(board.getStateHistory().getCurrentState());
//        System.exit(0);
        UI ui = new TtyUI();
        CapableOfPlaying[] players = {
                new Player(PieceColor.WHITE, "Kaappo", ui),
                new TreeAI(PieceColor.BLACK, "tree ai", ui, 2)
        };

        Runner runner = new Runner(board, players, ui);
        try {
            runner.play(PieceColor.BLACK);
        } catch (StopException e) {
            new Saver<>().save(runner.getBoard(), "/home/kaappo/git/chess/src/main/resources/serialized_boards/" + System.currentTimeMillis() + ".out", false);
        }
    }
}
