package players;

import chess.move.Move;
import chess.piece.basepiece.PieceColor;
import runner.UI;

import java.util.Iterator;
import java.util.Random;

public class RandomAI extends Player {
    public RandomAI (PieceColor color, String name, UI ui) {
        super(color, name, ui);
    }

    @Override
    public Move getMove () {
        var moves = board.getAllPossibleMoves(color);

        int index = new Random().nextInt(moves.size());
        Iterator<Move> iter = moves.iterator();
        for (int i = 0; i < index; i++) {
            iter.next();
        }
        return iter.next();
    }
}
