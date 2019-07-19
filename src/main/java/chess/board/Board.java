package chess.board;

import chess.misc.ChessException;
import chess.misc.Position;
import chess.piece.NoPiece;
import chess.piece.basepiece.Piece;
import chess.piece.basepiece.PieceType;

public class Board {
    private Piece[][] board;

    private final int dimX;
    private final int dimY;


    public Board () {
        this(8, 8);
    }

    public Board (int dimX, int dimY) {
        initBoard(dimX, dimY);

        this.dimX = dimX;
        this.dimY = dimY;
    }

    private void initBoard (int dimX, int dimY) {
        board = new Piece[dimY][dimX];

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

    public boolean isSquareUnderThreat (Position position) {
        return ThreatChecker.isUnderThreat(position, this);
    }

    public int getDimX () {
        return dimX;
    }

    public int getDimY () {
        return dimY;
    }
}
