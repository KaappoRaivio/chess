package chess.piece;

import chess.board.Board;
import chess.misc.Position;
import chess.misc.exceptions.ChessException;
import chess.move.*;
import chess.piece.basepiece.Piece;
import chess.piece.basepiece.PieceColor;
import chess.piece.basepiece.PieceType;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class CastlingKing extends King {

    public CastlingKing (PieceColor color) {
        super(color, color == PieceColor.WHITE ? "♔̅" : "♚̅", 500);
    }


    @Override
    public Set<Move> getPossibleMoves (Board board, Position position, Move lastMove) {
        Set<Move> possiblePositions = super.getPossibleMoves(board, position, lastMove)
                .stream()
                .map(move -> {
                    NormalMove normalMove = ((NormalMove) move);
                    return new CastlingKingMove(normalMove.getOrigin(), normalMove.getDestination(), board);
                }).collect(Collectors.toSet());

        possiblePositions.addAll(handleKingSideCastling(board, position));
        possiblePositions.addAll(handleQueenSideCastling(board, position));
        return possiblePositions;
    }

    private Set<Move> handleKingSideCastling (Board board, Position position) {

        try {
            Piece supposedRook = board.getPieceInSquare(position.offsetX(3));

            if (supposedRook.getType() != PieceType.ROOK || !(supposedRook instanceof CastlingRook)) {
                return Collections.emptySet();
            }

            if (board.isSquareUnderThreat(position)
                    || board.isSquareUnderThreat(position.offsetX(1))
                    || board.isSquareUnderThreat(position.offsetX(2))
                    || !board.isSquareEmpty(position.offsetX(1))
                    || !board.isSquareEmpty(position.offsetX(2))) {
                return Collections.emptySet();
            }

            return Set.of(new CastlingMove(CastlingType.KING_SIDE, color, board));
        } catch (ChessException e) {
            return Collections.emptySet();
        }


    }

    private Set<Move> handleQueenSideCastling (Board board, Position position) {

        try {
            Piece supposedRook = board.getPieceInSquare(position.offsetX(-4));

            if (supposedRook.getType() != PieceType.ROOK || !(supposedRook instanceof CastlingRook)) {
                return Collections.emptySet();
            }

            if (board.isSquareUnderThreat(position)
                    || board.isSquareUnderThreat(position.offsetX(-1))
                    || board.isSquareUnderThreat(position.offsetX(-2))
                    || !board.isSquareUnderThreat(position.offsetX(-1))
                    || !board.isSquareUnderThreat(position.offsetX(-2))
                    || !board.isSquareUnderThreat(position.offsetX(-3))) { // Rook's path can be threatened, so no threat check for position.offsetX(-3)
                return Collections.emptySet();
            }

            return Set.of(new CastlingMove(CastlingType.QUEEN_SIDE, color, board));
        } catch (ChessException e) {
            return Collections.emptySet();
        }
    }

}
