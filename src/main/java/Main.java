import chess.board.Board;
import chess.board.BoardNotation;
import chess.misc.exceptions.StopException;
import chess.move.Move;
import chess.piece.basepiece.PieceColor;
import misc.Saver;
import players.Player;
import players.alphabetaai.TreeAIInterface;
import runner.CapableOfPlaying;
import runner.Runner;
import runner.UI;
import ui.TtyUI;

import java.util.Arrays;
import java.util.Collections;

public class Main {
    public static void main (String[] args) {
//        Board board = Board.fromFile("/home/kaappo/git/chess/src/main/resources/boards/starting_position.txt", BoardNotation.DEFAULT_NOTATION);
        Board board = Saver.fromFile("/home/kaappo/git/chess/src/main/resources/serialized_boards/1567347710692.out", Board.class);
//        board.makeMove(Move.parseMove("e2e4", PieceColor.WHITE, board));
//        System.out.println(board.toFen());
//        System.exit(0);
//        Board board = Board.fromFile("/home/kaappo/git/chess/src/main/resources/boards/pos11.txt", BoardNotation.DEFAULT_NOTATION);
//        System.out.println(board.isCheckMate(PieceColor.WHITE));
//        System.exit(0);
//        Board board = Board.fromFile("/home/kaappo/git/chess/src/main/resources/boards/1565344171878.out");
//        board.makeMove(Move.parseMove("d1b1", PieceColor.WHITE, board) );
//        board.unMakeMove(1);
//        System.out.println(board.getAllPossibleMoves(PieceColor.WHITE));
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
                new TreeAIInterface(PieceColor.BLACK, "tree ai", ui, 1),
        };

        Runner runner = new Runner(board, players, ui, Collections.emptyList());
        try {
            runner.play(board.getTurn());
        } catch (StopException e) {
            Saver.save(runner.getBoard(), "/home/kaappo/git/chess/src/main/resources/serialized_boards/" + System.currentTimeMillis() + ".out", false);
            runner.getBoard().saveHumanReadable("/home/kaappo/git/chess/src/main/resources/boards/" + System.currentTimeMillis() + ".out");
        } finally {
            Saver.save(runner.getBoard(), "/home/kaappo/git/chess/src/main/resources/serialized_boards/" + System.currentTimeMillis() + ".out", false);
            runner.getBoard().saveHumanReadable("/home/kaappo/git/chess/src/main/resources/boards/" + System.currentTimeMillis() + ".out");
        }
    }
}
