import chess.board.Board;
import chess.board.BoardNotation;
import chess.move.Move;
import chess.piece.basepiece.PieceColor;

public class Main {
    public static void main (String[] args) {
        Board board = Board.fromFile("/home/kaappo/git/chess/src/main/resources/boards/starting_position.txt", BoardNotation.DEFAULT_NOTATION);

        System.out.println(board);
        for (int i = 0; i < 4; i++) {
            System.out.println(i);
            board.makeMove(new Move("g1f3", board));
            System.out.println(board.isDraw());
            board.makeMove(new Move("b8c6", board));
            System.out.println(board.isDraw());

            board.makeMove(new Move("f3g1", board));
            System.out.println(board.isDraw());
            board.makeMove(new Move("c6b8", board));
            System.out.println(board.isDraw());
        }
//        board.undo(1);
//        board.makeMove(new Move("d2d4", board));
//        board.makeMove(new Move("c8c7", board));
        System.out.println(board);
        System.out.println(board.getAllPossibleMoves(PieceColor.WHITE));
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
