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
            Map.entry(".", new Pair<>(PieceType.NO_PIECE, PieceColor.NO_COLOR)),

            Map.entry("p", new Pair<>(PieceType.PAWN, PieceColor.WHITE)),
            Map.entry("b", new Pair<>(PieceType.BISHOP, PieceColor.WHITE)),
            Map.entry("n", new Pair<>(PieceType.KNIGHT, PieceColor.WHITE)),
            Map.entry("r", new Pair<>(PieceType.ROOK, PieceColor.WHITE)),
            Map.entry("q", new Pair<>(PieceType.QUEEN, PieceColor.WHITE)),
            Map.entry("k", new Pair<>(PieceType.KING, PieceColor.WHITE)),

            Map.entry("P", new Pair<>(PieceType.PAWN, PieceColor.BLACK)),
            Map.entry("B", new Pair<>(PieceType.BISHOP, PieceColor.BLACK)),
            Map.entry("N", new Pair<>(PieceType.KNIGHT, PieceColor.BLACK)),
            Map.entry("R", new Pair<>(PieceType.ROOK, PieceColor.BLACK)),
            Map.entry("Q", new Pair<>(PieceType.QUEEN, PieceColor.BLACK)),
            Map.entry("K", new Pair<>(PieceType.KING, PieceColor.BLACK))
    ));

    private Map<String, Pair<PieceType, PieceColor>> pieces;

    private BoardNotation (Map<String, Pair<PieceType, PieceColor>> pieces) {
        this.pieces = pieces;
    }

    Piece getPieceTypeAndColor (String text) {
        Pair<PieceType, PieceColor> typeAndColor = Optional.ofNullable(pieces.get(text)).orElseThrow(() -> new ChessException("Unknown char " + text + "!"));
        PieceColor color = typeAndColor.getSecond();

        switch (typeAndColor.getFirst()) {
            case PAWN:
                return new Pawn(color);
            case KNIGHT:
                return new Knight(color);
            case BISHOP:
                return new Bishop(color);
            case ROOK:
                return new Rook(color);
            case QUEEN:
                return new Queen(color);
            case KING:
                return new King(color);
            case NO_PIECE:
                return new NoPiece();
            default:
                throw new ChessException("Unexpected error: Unknown char " + text + "!");
        }
    }
}
