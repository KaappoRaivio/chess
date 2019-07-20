import chess.board.Board;
import chess.board.BoardNotation;

public class Main {
    public static void main (String[] args) {
        Board board = Board.fromFile("/home/kaappo/git/chess/src/main/resources/boards/starting_position.txt", BoardNotation.DEFAULT_NOTATION);
        System.out.println(board);
    }
}
