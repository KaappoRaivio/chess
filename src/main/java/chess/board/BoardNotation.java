package chess.board;

import chess.misc.exceptions.ChessException;
import chess.piece.*;
import chess.piece.basepiece.Piece;
import chess.piece.basepiece.PieceColor;
import chess.piece.basepiece.PieceType;
import misc.Pair;

import java.util.Map;
import java.util.Optional;

public class BoardNotation {
    public static final BoardNotation DEFAULT_NOTATION = new BoardNotation(Map.ofEntries(
            Map.entry(".", new NoPiece()),

            Map.entry("p", new Pawn(PieceColor.WHITE)),
            Map.entry("b", new Bishop(PieceColor.WHITE)),
            Map.entry("n", new Knight(PieceColor.WHITE)),
            Map.entry("r", new Rook(PieceColor.WHITE)),
            Map.entry("q", new Queen(PieceColor.WHITE)),
            Map.entry("k", new King(PieceColor.WHITE)),

            Map.entry("P", new Pawn(PieceColor.BLACK)),
            Map.entry("B", new Bishop(PieceColor.BLACK)),
            Map.entry("N", new Knight(PieceColor.BLACK)),
            Map.entry("R", new Rook(PieceColor.BLACK)),
            Map.entry("Q", new Queen(PieceColor.BLACK)),
            Map.entry("K", new King(PieceColor.BLACK)),

            Map.entry("k̅", new CastlingKing(PieceColor.WHITE)),
            Map.entry("K̅", new CastlingKing(PieceColor.BLACK)),
            Map.entry("r̅", new CastlingRook(PieceColor.WHITE)),
            Map.entry("R̅", new CastlingRook(PieceColor.BLACK))
    ));

    private Map<String, Piece> pieces;

    private BoardNotation (Map<String, Piece> pieces) {
        this.pieces = pieces;
    }

    Piece getPiece (String text) {
        return Optional.ofNullable(pieces.get(text)).orElseThrow(() -> new ChessException("Unknown char " + text + "!"));
    }
}
