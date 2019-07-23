import chess.board.Board;
import chess.board.BoardNotation;
import chess.misc.ChessException;
import chess.misc.Position;
import chess.move.Move;

public class Main {
    public static void main (String[] args) {
        Board board = Board.fromFile("/home/kaappo/git/chess/src/main/resources/boards/pos3.txt", BoardNotation.DEFAULT_NOTATION);
//        Board board = Board.fromFile("/home/kaappo/git/chess/src/main/resources/boards/starting_position.txt", BoardNotation.DEFAULT_NOTATION);
        System.out.println(board);
        System.out.println(board.isMoveLegal(new Move("C5E5", board)));
//        System.out.println(board.isSquareUnderThreat(Position.fromString("E1")));
//        board.makeMove(new Move("E1C1", board));
//        System.out.println(board);
//        board.undo(1);
//        System.out.println(board);

//        board.getAllPossibleMoves().forEach(move -> {
//            try {
//                board.makeMove(move);
//                board.getAllPossibleMoves().forEach(move1 -> {
//                    try {
//                        board.makeMove(move1);
//                        System.out.println("\n" + board);
//                        board.undo(1);
//                    } catch (ChessException e) {
//                        System.out.println("=== === ERROR === ===");
//                        System.out.println(board);
//                        System.out.println("=== === DUMP === ===");
//                        throw e;
//
//                    }
//                });
//
//                board.undo(1);
//            } catch (ChessException e) {
//                System.out.println("=== === ERROR === ===");
//                System.out.println(board);
//                System.out.println("=== === DUMP === ===");
//                throw e;
//
//            }
//        });

//        System.out.println(board.isSquareUnderThreat(Position.fromString("A8")));


//        board.makeMove(new Move("C2C3", board));
//        board.makeMove(new Move("G1F3", board));
//        board.makeMove(new Move("B8A6", board));
//        System.out.println(board);
//        board.undo(2);
//        System.out.println(board);

    }
}
