package chess.board;

import chess.misc.ChessException;
import chess.misc.Position;
import chess.piece.King;
import chess.piece.Knight;
import chess.piece.basepiece.Piece;
import chess.piece.basepiece.PieceColor;
import chess.piece.basepiece.PieceType;

public class ThreatChecker {
    public static boolean isUnderThreat (Position square, Board board) {
        return checkForPawns(square, board)
            && checkForKings(square, board)
            && checkForKnights(square, board)
            && checkForBishopsAndQueens(square, board)
            && checkForRooksAndQueens(square, board);
    }

    private static boolean checkForRooksAndQueens (Position square, Board board) {
        Piece piece = board.getPieceInSquare(square);
        int forwardDirection = piece.getForwardDirection();

        int x = square.getX();
        int y = square.getY();

        for (int checkY = y; checkY < board.getDimY(); checkY++) {
            Piece currentPiece = board.getPieceInSquare(square.offsetY(checkY));

            if ((currentPiece.getType() == PieceType.ROOK || currentPiece.getType() == PieceType.QUEEN) && currentPiece.getColor() == piece.getColor().invert()) {
                return true;
            }
        }

        for (int checkY = y; checkY >= 0; checkY--) {
            Piece currentPiece = board.getPieceInSquare(square.offsetY(checkY));

            if ((currentPiece.getType() == PieceType.ROOK || currentPiece.getType() == PieceType.QUEEN) && currentPiece.getColor() == piece.getColor().invert()) {
                return true;
            }
        }

        for (int checkX = x; checkX < board.getDimX(); checkX++) {
            Piece currentPiece = board.getPieceInSquare(square.offsetX(checkX));

            if ((currentPiece.getType() == PieceType.ROOK || currentPiece.getType() == PieceType.QUEEN) && currentPiece.getColor() == piece.getColor().invert()) {
                return true;
            }
        }

        for (int checkX = x; checkX >= 0; checkX--) {
            Piece currentPiece = board.getPieceInSquare(square.offsetX(checkX));

            if ((currentPiece.getType() == PieceType.ROOK || currentPiece.getType() == PieceType.QUEEN) && currentPiece.getColor() == piece.getColor().invert()) {
                return true;
            }
        }

        return false;
    }

    private static boolean checkForBishopsAndQueens (Position square, Board board) {
        Piece piece = board.getPieceInSquare(square);

        int x = square.getX();
        int y = square.getY();

        int checkX = x;
        int checkY = y;

        while (checkX < board.getDimX() && checkY < board.getDimY()) {
            Piece currentPiece = board.getPieceInSquare(square.offset(checkX, checkY));

            if ((currentPiece.getType() == PieceType.BISHOP || currentPiece.getType() == PieceType.QUEEN) && currentPiece.getColor() == piece.getColor().invert()) {
                return true;
            }

            checkX += 1;
            checkY += 1;
        }

        checkX = x;
        checkY = y;

        while (checkX < board.getDimX() && checkY >= 0) {
            Piece currentPiece = board.getPieceInSquare(square.offset(checkX, checkY));

            if ((currentPiece.getType() == PieceType.BISHOP || currentPiece.getType() == PieceType.QUEEN) && currentPiece.getColor() == piece.getColor().invert()) {
                return true;
            }

            checkX += 1;
            checkY -= 1;
        }

        checkX = x;
        checkY = y;

        while (checkX >= 0 && checkY >= 0) {
            Piece currentPiece = board.getPieceInSquare(square.offset(checkX, checkY));

            if ((currentPiece.getType() == PieceType.BISHOP || currentPiece.getType() == PieceType.QUEEN) && currentPiece.getColor() == piece.getColor().invert()) {
                return true;
            }

            checkX -= 1;
            checkY -= 1;
        }

        checkX = x;
        checkY = y;

        while (checkX >= 0 && checkY < board.getDimY()) {
            Piece currentPiece = board.getPieceInSquare(square.offset(checkX, checkY));

            if ((currentPiece.getType() == PieceType.BISHOP || currentPiece.getType() == PieceType.QUEEN) && currentPiece.getColor() == piece.getColor().invert()) {
                return true;
            }

            checkX -= 1;
            checkY += 1;
        }

        return false;
    }



    private static boolean checkForKnights (Position square, Board board) {
        PieceColor color = board.getPieceInSquare(square).getColor();

        for (Position offset : Knight.offsets) {
            Position newPosition;
            try {
                newPosition = square.offset(offset);
            } catch (ChessException e) {
                continue;
            }

            Piece piece = board.getPieceInSquare(newPosition);
            if (piece.getType() == PieceType.KNIGHT && piece.getColor() == color.invert()) {
                return true;
            }
        }

        return false;
    }

    private static boolean checkForKings (Position square, Board board) {
        PieceColor color = board.getPieceInSquare(square).getColor();

        for (Position offset : King.offsets) {
            Position newPosition;
            try {
                newPosition = square.offset(offset);
            } catch (ChessException e) {
                continue;
            }

            Piece piece = board.getPieceInSquare(newPosition);
            if (piece.getType() == PieceType.KING && piece.getColor() == color.invert()) {
                return true;
            }
        }

        return false;
    }

    private static boolean checkForPawns (Position square, Board board) {
        Piece piece = board.getPieceInSquare(square);

        Piece right = board.getPieceInSquare(square.offset(1, -piece.getForwardDirection()));
        Piece left = board.getPieceInSquare(square.offset(-1, -piece.getForwardDirection()));

        return right.getType() == PieceType.PAWN && right.getColor() == piece.getColor().invert()
            ||  left.getType() == PieceType.PAWN &&  left.getColor() == piece.getColor().invert();
    }

}
