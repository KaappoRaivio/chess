package ui;

import chess.board.Board;
import chess.misc.ChessException;
import chess.move.Move;
import chess.piece.basepiece.PieceColor;
import misc.OrdinalConverter;
import runner.UI;

import java.util.Arrays;
import java.util.Scanner;

public class TtyUI implements UI {
    private Board board;
    private PieceColor turn;
    private int moveCount;

    @Override
    public void updateValues (Board board, PieceColor turn, int moveCount) {
        this.board = board.deepCopy();
        this.turn = turn;
        this.moveCount = moveCount;
    }

    @Override
    public Move getMove (PieceColor color) {
        while (true) {
            try {
                System.out.print("Your move:> ");
                String response = new Scanner(System.in).nextLine();
                return new Move(response, board);
            } catch (ChessException ignored) { }
        }
    }

    @Override
    public void commit () {
        System.out.println(turn + "'s move, " + OrdinalConverter.toOrdinal(moveCount / 2) + " move:");
        System.out.println(board.isCheck(turn) + ", " + board.isCheck(turn.invert()));
        if (board.isCheck(turn)) {
            System.out.println("Check!");
        }
        Arrays.stream(board.toString().split("\n")).forEach(line -> System.out.println("\t" + line));
    }

    @Override
    public void close () {

    }
}
