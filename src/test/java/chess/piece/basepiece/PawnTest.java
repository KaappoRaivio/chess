package chess.piece.basepiece;

import chess.board.Board;
import chess.misc.Position;
import chess.move.EnPassantMove;
import chess.move.NoMove;
import chess.move.NormalMove;
import chess.move.PromotionMove;
import chess.piece.Pawn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.function.BooleanSupplier;

import static org.junit.jupiter.api.Assertions.*;

class PawnTest {
    final private Board board1 = Board.fromFile("/home/kaappo/git/chess/src/test/resources/boards/starting_position.txt");
    final private Board board2 = Board.fromFile("/home/kaappo/git/chess/src/test/resources/boards/pos6.txt");
    final private Board board3 = Board.fromFile("/home/kaappo/git/chess/src/test/resources/boards/pos10.txt");
    final private Board board4 = Board.fromFile("/home/kaappo/git/chess/src/test/resources/boards/pos5.txt");



    @Test
    void getForwardDirection () {
        assertEquals(1,new Pawn(PieceColor.WHITE).getForwardDirection(), "A white pawn goes forward when the y position increases");
        assertEquals(-1,new Pawn(PieceColor.BLACK).getForwardDirection(), "A black pawn goes forward when the y position decreases");
    }

    @Test
    void testMoveGeneration () {
        assertAll(this::testDoubleForward, this::testEnPassant, this::testCapture);
    }

    private void testDoubleForward () {
        Piece pawn1 = new Pawn(PieceColor.WHITE);

        assertEquals(pawn1.getPossibleMoves(board1, Position.fromString("e2"), board1.getLastMove()),
                Set.of(new NormalMove(Position.fromString("e2"), Position.fromString("e3"), board1),
                            new NormalMove(Position.fromString("e2"), Position.fromString("e4"), board1)
        ));
    }

    private void testEnPassant () {
        Piece pawn1 = new Pawn(PieceColor.WHITE);

        System.out.println(pawn1.getPossibleMoves(board2, Position.fromString("e5"), board2.getLastMove()) + ", " + board2.getLastMove());
        assertEquals(pawn1.getPossibleMoves(board2, Position.fromString("e5"), board2.getLastMove()),
                Set.of(new NormalMove(Position.fromString("e5"), Position.fromString("e6"), board2),
                             new EnPassantMove(Position.fromString("e5"), Position.fromString("f6"), board2)
        ));
    }

    private void testCapture () {
        Piece pawn1 = new Pawn(PieceColor.WHITE);

        assertEquals(pawn1.getPossibleMoves(board3, Position.fromString("f3"), board3.getLastMove()),
                Set.of(new NormalMove(Position.fromString("f3"), Position.fromString("e4"), board3),
                            new NormalMove(Position.fromString("f3"), Position.fromString("g4"), board3),
                            new NormalMove(Position.fromString("f3"), Position.fromString("f4"), board3)
        ));
    }

    private void testPromotion () {
        Piece pawn1 = new Pawn(PieceColor.BLACK);

        assertEquals(pawn1.getPossibleMoves(board4, Position.fromString("d2"), board4.getLastMove()),
            Set.of(new PromotionMove(Position.fromString("d2"), Position.fromString("d1"), board4, PieceType.QUEEN),
                    new PromotionMove(Position.fromString("d2"), Position.fromString("d1"), board4, PieceType.KNIGHT),
                    new PromotionMove(Position.fromString("d2"), Position.fromString("d1"), board4, PieceType.ROOK),
                    new PromotionMove(Position.fromString("d2"), Position.fromString("d1"), board4, PieceType.BISHOP)
        ));
    }
}