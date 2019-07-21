package chess.board;

import chess.misc.ChessException;
import chess.misc.Position;
import chess.piece.NoPiece;
import chess.piece.basepiece.Piece;
import chess.piece.basepiece.PieceType;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Board {
    private Piece[][] board;

    private final int dimX;
    private final int dimY;

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
        dimX = 8;
        dimY = 8;
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

        return builder.append("\n").append(vPadding).append(hPadding).append(" A B C D E F G H").append(hPadding).toString();
    }
}
