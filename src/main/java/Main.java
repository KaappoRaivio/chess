import chess.board.Board;
import chess.board.BoardNotation;
import chess.misc.exceptions.StopException;
import chess.piece.basepiece.PieceColor;
import misc.Saver;
import players.Player;
import players.TreeAI;
import runner.CapableOfPlaying;
import runner.Runner;
import runner.UI;
import ui.TtyUI;

import java.util.Arrays;

public class Main {
    public static void main (String[] args) {
        Board board = Board.fromFile("/home/kaappo/git/chess/src/main/resources/boards/starting_position.txt", BoardNotation.DEFAULT_NOTATION);
//        Board board = Board.fromFile("/home/kaappo/git/chess/src/main/resources/boards/pos11.txt", BoardNotation.DEFAULT_NOTATION);
//        System.out.println(board.isCheckMate(PieceColor.WHITE));
//        System.out.println(board.getAllPossibleMoves(PieceColor.WHITE));
//        System.exit(0);
//        Board board = Saver.fromFile("/home/kaappo/git/chess/src/main/resources/serialized_boards/1564943212850.out", Board.class);
        //f8d6
//        Board board = Saver.fromFile("/home/kaappo/git/chess/src/main/resources/serialized_boards/1564926519867.out", Board.class);
//        board.saveHumanReadable("/home/kaappo/git/chess/src/main/resources/boards/pos9.txt");
//        System.out.println(board);
//        System.exit(0);
//        board.makeMove(Move.parseMove("f8d6", PieceColor.BLACK, board));
//        System.out.println(board.isCheck(PieceColor.BLACK));
//        System.out.println(board);
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
                new Player(PieceColor.WHITE, "AskoChess", ui),
                new TreeAI(PieceColor.BLACK, "tree ai", ui, 2),
        };

        Runner runner = new Runner(board, players, ui, Arrays.asList(new TreeAI(PieceColor.WHITE, "white spectator", ui, 10), new TreeAI(PieceColor.BLACK, "white spectator", ui, 2)));
        try {
            runner.play(PieceColor.WHITE);
        } catch (StopException e) {
            Saver.save(runner.getBoard(), "/home/kaappo/git/chess/src/main/resources/serialized_boards/" + System.currentTimeMillis() + ".out", false);
            runner.getBoard().saveHumanReadable("/home/kaappo/git/chess/src/main/resources/boards/" + System.currentTimeMillis() + ".out");
        } finally {
            Saver.save(runner.getBoard(), "/home/kaappo/git/chess/src/main/resources/serialized_boards/" + System.currentTimeMillis() + ".out", false);
            runner.getBoard().saveHumanReadable("/home/kaappo/git/chess/src/main/resources/boards/" + System.currentTimeMillis() + ".out");
        }
    }
}
