package chess.board;

import chess.misc.ChessException;
import chess.misc.Position;
import chess.piece.NoPiece;
import chess.piece.basepiece.Piece;
import chess.piece.basepiece.PieceType;

public class Board {
    private Piece[][] board;

    public Board () {
        initBoard();
    }

    private void initBoard () {
        board = new Piece[8][8];

        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                board[y][x] = new NoPiece(new Position(x, y));
            }
        }
    }

    public Piece getPieceInSquare (Position position) {
        try {
            return board[position.getY()][position.getX()];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ChessException("Position " + position + " is invalid!");
        }
    }

    public boolean isSquareEmpty (Position position) {
        return getPieceInSquare(position).getType() == PieceType.NO_PIECE;
    }
}
