package chess.board;

import chess.misc.ChessException;
import chess.misc.Position;
import chess.move.Move;
import chess.piece.NoPiece;
import chess.piece.basepiece.Piece;
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

    private MoveHistory moveHistory = new MoveHistory();

    private static final String hPadding = " ";
    private static final String vPadding = "";


    public Board () {
        this(8, 8);
    }

    public Board (int dimX, int dimY) {
        initBoard(dimX, dimY);

        this.dimX = dimX;
        this.dimY = dimY;
    }

    private Board (Piece[][] board) {
        this.board = board;
        dimX = board[0].length;
        dimY = board.length;
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

        for (int y = 0; y < 8; y++) {
            String[] row = lines[y].split(" +");

            for (int x = 0; x < 8; x++) {
                boardBuffer[7 - y][x] = notation.getPieceTypeAndColor(row[x]);  // "7 - y" because the y axis is inverted in the source file
            }
        }

        Board board = new Board(boardBuffer);

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
        if (!result) {
            System.out.println("BINGO2");
        }
        return result;
    }
    
    public Set<Move> getAllPossibleMoves () {
        Set<Move> moves = new HashSet<>();

        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                for (Position possiblePosition : getPieceInSquare(new Position(x, y)).getPossiblePositions(this, new Position(x, y))) {
                    if (!isMoveLegal(new Move(new Position(x, y), possiblePosition, this))) {
                        System.out.println("BINGO");
                    }
                    moves.add(new Move(new Position(x, y), possiblePosition, this));
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

        board[destination.getY()][destination.getX()] = getPieceInSquare(origin);
        board[origin.getY()][origin.getX()] = new NoPiece();

        broadcastMove(move);
        getPieceInSquare(destination).onMoved(move, this);
        moveHistory.addMove(move);
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

        Board another = new Board(board);
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
