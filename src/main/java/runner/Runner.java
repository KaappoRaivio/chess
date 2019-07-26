package runner;

import chess.board.Board;
import chess.move.Move;
import chess.piece.basepiece.PieceColor;

import java.util.Scanner;

public class Runner {
    private Board board;
    private CapableOfPlaying[] players;
    private UI ui;
    private int moveCount;

    public Runner (Board board, CapableOfPlaying[] players, UI ui) {
        this.board = board;
        this.players = players;
        this.ui = ui;
        moveCount = 0;

    }

    public PieceColor play (PieceColor initialTurn) {
        PieceColor turn = initialTurn;
        int moveCount = initialTurn == PieceColor.WHITE ? 1 : 2;
        for (CapableOfPlaying capableOfPlaying : players) {
            capableOfPlaying.updateValues(board.deepCopy(), turn, moveCount);
        }
        ui.updateValues(board, turn, moveCount);

        while (true) {
            CapableOfPlaying currentPlayer = players[(moveCount + 1) % 2];
            if (board.isCheckMate(currentPlayer.getColor())) {
                System.out.println("=== === Checkmate! === ===");
                System.out.println(board);
                System.out.println(currentPlayer.getColor().invert() + " wins!");
                break;
            }

            ui.commit();

            while (true) {
                try {
                    System.out.println(board.getAllPossibleMoves(turn));
                    Move move = currentPlayer.getMove();
                    System.out.println(move);
                    board.makeMove(move);
                    break;
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    System.out.println("Move is invalid!");
                    new Scanner(System.in).nextLine();
                }
            }

            turn = turn.invert();
            moveCount += 1;


            for (CapableOfPlaying player : players) {
                player.updateValues(board.deepCopy(), turn, moveCount);
            }
            ui.updateValues(board, turn, moveCount);



        }

        return turn;
    }
}
