package chess.piece;

import chess.board.Board;
import chess.misc.Position;
import chess.move.Move;
import chess.move.NormalMove;
import chess.move.CastlingRookMove;
import chess.piece.basepiece.PieceColor;

import java.util.Set;
import java.util.stream.Collectors;

public class CastlingRook extends Rook {
    public CastlingRook (PieceColor color) {
        super(color, color == PieceColor.WHITE ? "♖̅" : "♜̅");
    }

    @Override
    public Set<Move> getPossibleMoves (Board board, Position position, Move lastMove) {
        return super.getPossibleMoves(board, position, lastMove)
                .stream()
                .map(move -> {
                    NormalMove normalMove = ((NormalMove) move);
                    return new CastlingRookMove(normalMove.getOrigin(), normalMove.getDestination(), board);
                })
                .collect(Collectors.toSet());
    }
}
