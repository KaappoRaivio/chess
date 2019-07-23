package players;

import chess.board.Board;
import chess.move.Move;
import chess.piece.basepiece.PieceColor;
import runner.CapableOfPlaying;
import runner.UI;

public class Player implements CapableOfPlaying {
    protected Board board;
    protected PieceColor turn;
    protected int moveCount;

    protected PieceColor color;
    private UI ui;
    private String name;

    public Player (PieceColor color, String name, UI ui) {
        this.color = color;
        this.ui  = ui;
        this.name = name;
    }

    @Override
    public Move getMove () {
        return ui.getMove(color);
    }

    @Override
    public void updateValues (Board board, PieceColor turn, int moveCount) {
        this.board = board;
        this.turn = turn;
        this.moveCount = moveCount;
    }

    @Override
    public PieceColor getColor () {
        return color;
    }

    @Override
    public String toString () {
        return "Player " + name;
    }
}
