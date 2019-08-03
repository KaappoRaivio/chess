package chess.board;

import chess.misc.Position;
import chess.misc.Reader;
import chess.misc.exceptions.ChessException;
import chess.move.Move;
import chess.move.NoMove;
import chess.piece.NoPiece;
import chess.piece.basepiece.Piece;
import chess.piece.basepiece.PieceColor;
import chess.piece.basepiece.PieceType;
import misc.Pair;
import misc.Saver;

import java.io.Serializable;
import java.util.BitSet;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

public class Board implements Serializable{
    private Piece[][] board;

    private final int dimX;
    private final int dimY;

    private RepetitionTracker repetitionTracker = new RepetitionTracker();

    private BoardStateHistory stateHistory;


    private Board (Piece[][] board) {
        this.dimX = board[0].length;
        this.dimY = board.length;

        initBoard(dimX, dimY);
        this.board = board;

        Pair<Position, Position> kingPositions = findKings();
        stateHistory = new BoardStateHistory(new BoardState(kingPositions.getFirst(), kingPositions.getSecond(), 0, new NoMove()));
    }


    public static Board fromFile (String path) {
        return fromFile(path, BoardNotation.DEFAULT_NOTATION);
    }

    public static Board fromFile (String path, BoardNotation boardNotation) {
        String[] lines = Reader.readFile(path).split("\n");

        Piece[][] buffer = new Piece[8][8];

            for (int y = 0; y < 8; y++) {
                String[] line = lines[7 - y].split("( )+");

            for (int x = 0; x < 8; x++) {
                buffer[y][x] = boardNotation.getPiece(line[x]);
            }
        }

        return new Board(buffer);
    }

    private Pair<Position, Position> findKings () {
        Position whiteKingPos = null;
        Position blackKingPos = null;

        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                Piece piece = getPieceInSquare(new Position(x, y));

                if (piece.getType() == PieceType.KING && piece.getColor() == PieceColor.WHITE) {
                    whiteKingPos = new Position(x, y);
                } else if (piece.getType() == PieceType.KING && piece.getColor() == PieceColor.BLACK) {
                    blackKingPos = new Position(x, y);
                }
            }
        }

        return new Pair<>(Optional.ofNullable(whiteKingPos).orElseThrow(() -> new ChessException("Couldn't find white king from board " + toString() + "!")),
                Optional.ofNullable(blackKingPos).orElseThrow(() -> new ChessException("Couldn't find black king from board " + toString() + "!")));
    }




    private void initBoard (int dimX, int dimY) {
        board = new Piece[dimY][dimX];

        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                board[y][x] = new NoPiece();
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


    public boolean isMoveLegal (Move move) {
        boolean result = getPieceInSquare(move.getOrigin()).getPossibleMoves(this, move.getOrigin(), getLastMove()).contains(move);
        if (result) {
            executeMove(move);
            boolean isStateLegal = !isCheck(move.getColor());
            unMakeMove(1);

            return isStateLegal;
        } else {
            return false;
        }
    }


    public Set<Move> getAllPossibleMoves (PieceColor color) {
        Set<Move> moves = new LinkedHashSet<>();

        for (int y = 0; y < dimX; y++) {
            for (int x = 0; x < dimY; x++) {
                Piece piece = getPieceInSquare(new Position(x, y));
                if (piece.getColor() != color) {
                    continue;
                }

                for (Move possibleMove : piece.getPossibleMoves(this, new Position(x, y), getLastMove())) {
                    if (!isMoveLegal(possibleMove)) {
//                        new ChessException("Warning! not legal: " + new Move(new Position(x, y), possibleMove, this) + "!").printStackTrace();
//                        System.out.println("Rejecting " + possibleMove + "!");
                    } else {
                        moves.add(possibleMove);
                    }
                }
            }
        }

        return moves;
    }

    public void makeMove (Move move) {
        if (isMoveLegal(move)) {
            executeMove(move);
        } else {
            throw new ChessException("Move " + move + " is not legal for position " + this + "!");
        }
    }

    private void executeMove (Move move) {
        move.makeMove(board);

        stateHistory.push();

        if (move.affectsKingPosition()) {
            var pair = move.getNewKingPosition();

            switch (pair.getFirst()) {
                case WHITE:
                    stateHistory.getCurrentState().setWhiteKingPosition(pair.getSecond());
                    break;
                case BLACK:
                    stateHistory.getCurrentState().setBlackKingPosition(pair.getSecond());
                    break;
            }
        }

        if (move.resetsFiftyMoveRule()) {
            stateHistory.getCurrentState().setMovesSinceFiftyMoveReset(0);
        } else {
            stateHistory.getCurrentState().setMovesSinceFiftyMoveReset(stateHistory.getCurrentState().getMovesSinceFiftyMoveReset() + 1);
        }

        stateHistory.getCurrentState().setLastMove(move);

        repetitionTracker.add(this);

    }

    public void unMakeMove (int level) {
        for (int i = 0; i < level; i++) {
            undo(stateHistory.getCurrentState().getLastMove());
            stateHistory.pull();
        }
    }

    private void undo (Move lastMove) {
        repetitionTracker.subtract(this);
        lastMove.unmakeMove(board);
    }

    public boolean isCheck (PieceColor color) {
        Position position;

        switch (color) {
            case WHITE:
                position = stateHistory.getCurrentState().getWhiteKingPosition();
                break;
            case BLACK:
                position = stateHistory.getCurrentState().getBlackKingPosition();
                break;
            default:
                throw new ChessException("No NoColor!");
        }

        return ThreatChecker.isUnderThreat(Optional.ofNullable(position).orElseThrow(), this);
    }

    public boolean isCheckMate (PieceColor color) {
        return isCheck(color) && getAllPossibleMoves(color).size() == 0;
    }

    public boolean isDraw (PieceColor turn) {
        return repetitionTracker.isDraw() || stateHistory.getCurrentState().getMovesSinceFiftyMoveReset() >= 50 || (!isCheck(turn) && getAllPossibleMoves(turn).size() == 0);
    }

    @Override
    public String toString() {
        String hPadding = " ";
        String vPadding = "";

        StringBuilder builder = new StringBuilder(vPadding).append(hPadding).append("\n  a b c d e f g h").append(hPadding).append("\n").append(vPadding);
        for (int y = dimY - 1; y >= 0; y--) {
            if (y < dimY - 1) {
                builder.append("\n");
            }

            builder.append(y + 1).append(hPadding);

            for (int x = 0; x < board[y].length; x++) {
                builder.append(board[y][x]);

                if (x + 1 < board[y].length) {
                    builder.append(" ");
                }
            }

            builder.append(hPadding).append(y + 1);
        }

        return builder.append("\n").append(vPadding).append(hPadding).append(" A B C D E F G H").append(hPadding).toString();
    }

    @Override
    public int hashCode () {
        BitSet result = new BitSet(32);

        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                Position position = new Position(x, y);
                if (!isSquareEmpty(position)) {
                    Piece piece = getPieceInSquare(position);

                    result.xor(ZobristBitStrings.getInstance().getSet(position, piece.getIndex(this, position, getLastMove())));
                }
            }
        }

        return ZobristBitStrings.bitSetToInt(result);
    }

    @Override
    public boolean equals (Object object) {
        if (object == null) {
            return false;
        }

        return getClass() == object.getClass() && hashCode() == object.hashCode();
    }

    public Board deepCopy () {
        return new Saver<Board>().deepCopy(this, Board.class);
    }

    public Move getLastMove () {
        return stateHistory.getCurrentState().getLastMove();
    }

    public int getDimX () {
        return dimX;
    }

    public int getDimY () {
        return dimY;
    }

    Piece[][] getBoard () {
        return board;
    }

    public BoardStateHistory getStateHistory () {
        return stateHistory;
    }
}
