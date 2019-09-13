package chess.board;

import chess.move.Move;
import chess.move.NoMove;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

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

    public List<BoardState> getPreviousStates () {
        return (List<BoardState>) states;
    }

    @Override
    public String toString () {
        return "BoardStateHistory{" +
                "states=" + states +
                '}';
    }
}
