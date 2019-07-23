package chess.board;

import chess.misc.ChessException;
import chess.misc.Position;
import chess.move.Move;
import chess.piece.King;
import chess.piece.NoPiece;
import chess.piece.basepiece.Piece;
import chess.piece.basepiece.PieceColor;
import chess.piece.basepiece.PieceType;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Board {
    private Piece[][] board;

    private final int dimX;
    private final int dimY;

    private Position whiteKingPos;
    private Position blackKingPos;

    private MoveHistory moveHistory = new MoveHistory();

    private static final String hPadding = " ";
    private static final String vPadding = "";




    private Board (Piece[][] board, Position whiteKingPos, Position blackKingPos) {
        this.board = board;
        this.dimX = board[0].length;
        this.dimY = board.length;

        this.whiteKingPos = whiteKingPos;
        this.blackKingPos = blackKingPos;
    }

    public void undo (int level) {
        moveHistory.undo(board, level);
    }

    private static final Pattern moveTimePattern = Pattern.compile(   "^" + // line start
                                                                            "[a-hA-H][1-8]" + // Chess position notation
                                                                            " +" + // Some amount of whitespace
                                                                            "[-]?\\d" +  // possibly a minus sign, and a single number
                                                                            "$", // Line end
            Pattern.MULTILINE);

    public static Board fromFile (String path, BoardNotation notation) {
        String data;
        try {
//            Optional.ofNullable(Board.class.getResourceAsStream(path).readAllBytes()).orElseThrow(() -> new RuntimeException("Invalid path " + path + "!"));
            data = readFile(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        Piece[][] boardBuffer = new Piece[8][8];

        String[] lines = data.split("\n");

        Position whiteKingPos = null;
        Position blackKingPos = null;

        for (int y = 0; y < 8; y++) {
            String[] row = lines[y].split(" +");

            for (int x = 0; x < 8; x++) {
                Piece pieceTypeAndColor = notation.getPieceTypeAndColor(row[x]); // "7 - y" because the y axis is inverted in the source file
                if (pieceTypeAndColor.getType() == PieceType.KING && pieceTypeAndColor.getColor() == PieceColor.WHITE) {
                    if (whiteKingPos == null) {
                        whiteKingPos = new Position(x, y);
                    } else {
                        throw new ChessException("Cannot have more than one kings (on board file " + path + ")!");
                    }
                } else if (pieceTypeAndColor.getType() == PieceType.KING && pieceTypeAndColor.getColor() == PieceColor.BLACK) {
                    if (blackKingPos == null) {
                        blackKingPos = new Position(x, 7 - y);
                    } else {
                        throw new ChessException("Cannot have more than one kings (on board file " + path + ")!");
                    }
                }

                boardBuffer[7 - y][x] = pieceTypeAndColor;
            }
        }

        if (whiteKingPos == null || blackKingPos == null) {
            throw new ChessException("Couldn't find kings from board file " + path + "!");
        }

        Board board = new Board(boardBuffer, whiteKingPos, blackKingPos);

        for (String line : lines) {
            if (moveTimePattern.matcher(line).matches()) {
                String[] split = line.split(" +");
                Position position = Position.fromString(split[0]);
                int amount = Integer.parseInt(split[1]);

                Piece piece = board.getPieceInSquare(position);
                if (amount >= 0) {
                    piece.setHasMoved(true);
                    piece.setAmountOfMovesSinceLastMoving(amount);
                }
            }
        }

        return board;
    }

    private static String readFile (String path) throws IOException {
        return StringUtils.join(Files.lines(Paths.get(path), StandardCharsets.UTF_8).collect(Collectors.toList()), '\n');
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

    public Set<Position> getDestinations (Position position) {
        return getPieceInSquare(position).getPossiblePositions(this, position);
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

    Piece[][] getBoard () {
        return board;
    }

    public boolean isMoveLegal (Move move) {
        boolean result = getPieceInSquare(move.getOrigin()).getPossiblePositions(this, move.getOrigin()).contains(move.getDestination());
        PieceColor color = getPieceInSquare(move.getOrigin()).getColor();
        if (result) {
            makeDummyMove(move);
            boolean isPositionLegal = !isSquareUnderThreat(color == PieceColor.WHITE ? whiteKingPos : blackKingPos); // is the player's king in check after the move?
            undo(1);

            return isPositionLegal;
        }
        return false;
    }
    
    public Set<Move> getAllPossibleMoves () {
        Set<Move> moves = new HashSet<>();

        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                for (Position possiblePosition : getPieceInSquare(new Position(x, y)).getPossiblePositions(this, new Position(x, y))) {
                    if (!isMoveLegal(new Move(new Position(x, y), possiblePosition, this))) {
                        new ChessException("Warning! not legal: " + new Move(new Position(x, y), possiblePosition, this) + "!").printStackTrace();
                    } else {
                        moves.add(new Move(new Position(x, y), possiblePosition, this));
                    }
                }
            }
        }

        return moves;
    }

    public void makeMove (Move move) {
        if (isMoveLegal(move)) {
            makeDummyMove(move);
        } else {
            throw new ChessException("Move " + move + " isn't legal for position \n" + this + "\n!");
        }
    }

    private void makeDummyMove (Move move) {
        Position origin = move.getOrigin();
        Position destination = move.getDestination();
        if (getPieceInSquare(origin).getType() == PieceType.KING) {
            if (getPieceInSquare(origin).getColor() == PieceColor.WHITE) {
                whiteKingPos = destination;
            } else if (getPieceInSquare(origin).getColor() == PieceColor.BLACK) {
                blackKingPos = destination;
            } else {
                throw new ChessException("King has NO_COLOR!");
            }
        }

        boolean castling = isCastling(move);

        board[destination.getY()][destination.getX()] = getPieceInSquare(origin);
        board[origin.getY()][origin.getX()] = new NoPiece();

        broadcastMove(move);
        getPieceInSquare(destination).onMoved(move, this);
        moveHistory.addMove(move);
        moveRookIfCastling(move, castling);
    }

    private void moveRookIfCastling (Move move, boolean castling) {
        if (castling) {
            int castlingDirection = getCastlingDirection(move);

            Position origin = move.getOrigin();

            if (castlingDirection > 0) {
                Position rookPos = new Position(7, origin.getY());
                board[origin.getY()][origin.getX() + 1] = getPieceInSquare(rookPos);
                board[rookPos.getY()][rookPos.getX()] = new NoPiece();
            } else if (castlingDirection < 0) {
                Position rookPos = new Position(0, origin.getY());
                board[origin.getY()][origin.getX() - 1] = getPieceInSquare(rookPos);
                board[rookPos.getY()][rookPos.getX()] = new NoPiece();
            } else {
                throw new ChessException("Error! " + move);
            }
        }
    }

    private boolean isCastling (Move move) {
        return getPieceInSquare(move.getOrigin()).getType() == PieceType.KING && Math.abs(move.getOrigin().getX() - move.getDestination().getX()) == 2;
    }

    private int getCastlingDirection (Move move) {
        return Integer.compare(move.getDestination().getX() - move.getOrigin().getX(), 0);
    }

    private void broadcastMove (Move move) {
        for (int y = 0; y < dimY; y++) {
            for (int x = 0; x < dimX; x++) {
                getPieceInSquare(new Position(x, y)).onAnotherPieceMoved(move, this);
            }
        }
    }

    public Board deepCopy () {
        Piece[][] newBuffer = new Piece[dimY][dimX];

        for (int y = 0; y < board.length; y++) {
            if (board[y].length >= 0) System.arraycopy(board[y], 0, newBuffer[y], 0, board[y].length);
        }

        Board another = new Board(board, whiteKingPos, blackKingPos);
        another.moveHistory = moveHistory.deepCopy();

        return another;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(hPadding).append(" a b c d e f g h").append(hPadding).append("\n").append(vPadding);
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

        return builder.append("\n").append(vPadding).append(hPadding).append(" A B C D E F G H").append(hPadding).append("\n").toString();
    }
}
