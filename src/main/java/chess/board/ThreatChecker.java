package chess.board;

import chess.misc.exceptions.ChessException;
import chess.misc.Position;
import chess.piece.King;
import chess.piece.Knight;
import chess.piece.basepiece.Piece;
import chess.piece.basepiece.PieceColor;
import chess.piece.basepiece.PieceType;

public class ThreatChecker {
    static boolean isUnderThreat (Position square, Board board) {
        boolean pawns = checkForPawns(square, board);
        boolean kings = checkForKings(square, board);
        boolean knights = checkForKnights(square, board);
        boolean bishopsAndQueens = checkForBishopsAndQueens(square, board);
        boolean rooksAndQueens = checkForRooksAndQueens(square, board);

        return pawns
            || kings
            || knights
            || bishopsAndQueens
            || rooksAndQueens;
    }

    private static boolean checkForRooksAndQueens (Position square, Board board) {


        Piece piece = board.getPieceInSquare(square);

        int x = square.getX();
        int y = square.getY();

        for (int checkY = y; checkY < board.getDimY(); checkY++) {
            Piece currentPiece = board.getPieceInSquare(new Position(x, checkY));

            if (currentPiece.getColor() == piece.getColor()) {
                if (currentPiece.getType() == PieceType.ROOK || currentPiece.getType() == PieceType.QUEEN) {
                    return true;
                } else {
                    break;
                }
            } else if (currentPiece.getColor() == piece.getColor().invert()) {
                break;
            }
        }

        for (int checkY = y; checkY >= 0; checkY--) {
            Piece currentPiece = board.getPieceInSquare(new Position(x, checkY));

            if (currentPiece.getColor() == piece.getColor()) {
                if (currentPiece.getType() == PieceType.ROOK || currentPiece.getType() == PieceType.QUEEN) {
                    return true;
                } else {
                    break;
                }
            } else if (currentPiece.getColor() == piece.getColor().invert()) {
                break;
            }
        }

        for (int checkX = x; checkX < board.getDimX(); checkX++) {
            Piece currentPiece = board.getPieceInSquare(new Position(checkX, y));

            if ((currentPiece.getType() == PieceType.ROOK || currentPiece.getType() == PieceType.QUEEN) && currentPiece.getColor() == piece.getColor().invert()) {
                return true;
            }
        }

        for (int checkX = x; checkX >= 0; checkX--) {
            Piece currentPiece = board.getPieceInSquare(new Position(checkX, y));

            if (currentPiece.getColor() == piece.getColor()) {
                if (currentPiece.getType() == PieceType.ROOK || currentPiece.getType() == PieceType.QUEEN) {
                    return true;
                } else {
                    break;
                }
            } else if (currentPiece.getColor() == piece.getColor().invert()) {
                break;
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
            Piece currentPiece = board.getPieceInSquare(new Position(checkX, checkY));

            if (currentPiece.getColor() == piece.getColor()) {
                if (currentPiece.getType() == PieceType.ROOK || currentPiece.getType() == PieceType.QUEEN) {
                    return true;
                } else {
                    break;
                }
            } else if (currentPiece.getColor() == piece.getColor().invert()) {
                break;
            }

            checkX += 1;
            checkY += 1;
        }

        checkX = x;
        checkY = y;

        while (checkX < board.getDimX() && checkY >= 0) {
            Piece currentPiece = board.getPieceInSquare(new Position(checkX, checkY));

            if (currentPiece.getColor() == piece.getColor()) {
                if (currentPiece.getType() == PieceType.ROOK || currentPiece.getType() == PieceType.QUEEN) {
                    return true;
                } else {
                    break;
                }
            } else if (currentPiece.getColor() == piece.getColor().invert()) {
                break;
            }

            checkX += 1;
            checkY -= 1;
        }

        checkX = x;
        checkY = y;

        while (checkX >= 0 && checkY >= 0) {
            Piece currentPiece = board.getPieceInSquare(new Position(checkX, checkY));

            if (currentPiece.getColor() == piece.getColor()) {
                if (currentPiece.getType() == PieceType.ROOK || currentPiece.getType() == PieceType.QUEEN) {
                    return true;
                } else {
                    break;
                }
            } else if (currentPiece.getColor() == piece.getColor().invert()) {
                break;
            }

            checkX -= 1;
            checkY -= 1;
        }

        checkX = x;
        checkY = y;

        while (checkX >= 0 && checkY < board.getDimY()) {
            Piece currentPiece = board.getPieceInSquare(new Position(checkX, checkY));

            if (currentPiece.getColor() == piece.getColor()) {
                if (currentPiece.getType() == PieceType.ROOK || currentPiece.getType() == PieceType.QUEEN) {
                    return true;
                } else {
                    break;
                }
            } else if (currentPiece.getColor() == piece.getColor().invert()) {
                break;
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

        Piece right = null;
        try {
            right = board.getPieceInSquare(square.offset(1, -piece.getForwardDirection()));
        } catch (ChessException ignored) { }


        Piece left = null;
        try {
            left = board.getPieceInSquare(square.offset(-1, -piece.getForwardDirection()));
        } catch (ChessException ignored) { }


        return right != null && right.getType() == PieceType.PAWN && right.getColor() == piece.getColor().invert()
            ||  left != null && left.getType() == PieceType.PAWN &&  left.getColor() == piece.getColor().invert();
    }

}
