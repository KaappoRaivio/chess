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
        Board board = Board.fromFile("/home/kaappo/git/chess/src/main/resources/boards/pos5.txt", BoardNotation.DEFAULT_NOTATION);
        System.out.println(board);
        board.makeMove(new Move("d2d4", board));
        board.undo(1);
        board.makeMove(new Move("d2d4", board));
//        board.undo(1);
//        board.makeMove(new Move("d2d4", board));
//        board.makeMove(new Move("c8c7", board));
        System.out.println(board);
        System.out.println(board.getAllPossibleMoves(PieceColor.BLACK));
//        System.out.println(board.isCheck(PieceColor.BLACK));
//        System.out.println(board.getAllPossibleMoves(PieceColor.BLACK));
//        board.makeMove(new Move("E1F1", board));
//        System.out.println(board);
//        System.out.println(board.getWhiteKingPos() + ", " + board.getBlackKingPos());
//        board.undo(1);
//        System.out.println(board);
//        System.out.println(board.getWhiteKingPos() + ", " + board.getBlackKingPos());


//        Board board = Board.fromFile("/home/kaappo/git/chess/src/main/resources/boards/starting_position.txt", BoardNotation.DEFAULT_NOTATION);

//        UI ui = new TtyUI();
//        CapableOfPlaying[] players = {
//                new Player(PieceColor.WHITE, "Kaappo", ui),
//                new RandomAI(PieceColor.BLACK, "random ai", ui)
//        };
//
//        Runner runner = new Runner(board, players, ui);
//        runner.play(PieceColor.WHITE);
    }
}
