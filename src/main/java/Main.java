import chess.board.Board;
import chess.board.BoardNotation;
import chess.misc.ChessException;
import chess.misc.Position;
import chess.move.Move;
import chess.piece.basepiece.PieceColor;
import misc.Saver;
import players.Player;
import players.RandomAI;
import runner.CapableOfPlaying;
import runner.Runner;
import runner.UI;
import ui.TtyUI;

public class Main {
    public static void main (String[] args) {
        Board board = Board.fromFile("/home/kaappo/git/chess/src/main/resources/boards/starting_position.txt", BoardNotation.DEFAULT_NOTATION);

        UI ui = new TtyUI();
        CapableOfPlaying[] players = {
                new Player(PieceColor.WHITE, "Kaappo", ui),
                new RandomAI(PieceColor.BLACK, "random ai", ui)
        };

        Runner runner = new Runner(board, players, ui);
        runner.play();
    }
}
