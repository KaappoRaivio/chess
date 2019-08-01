package chess.board;

import chess.move.Move;

import java.io.Serializable;
import java.util.*;

public class BoardStateHistory implements Serializable {
    private Deque<BoardState> states = new LinkedList<>();

    public BoardStateHistory (BoardState initialState) {
        states.push(initialState);
    }

    public BoardStateHistory () { }

    void push () {
        states.push(new BoardState(Optional.ofNullable(states.peek()).orElseThrow()));
    }

    void pull () {
        states.pop();
    }

    public BoardState getCurrentState () {
        return Optional.ofNullable(states.peek()).orElseThrow();
    }
}
