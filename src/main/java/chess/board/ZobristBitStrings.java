package chess.board;

import chess.misc.Position;

import java.io.Serializable;
import java.util.BitSet;
import java.util.Random;

public class ZobristBitStrings implements Serializable {
    private BitSet[][] bitSets;
    private Random random = new Random(13253453462L);
    final static int length = 64;


    private static ZobristBitStrings ourInstance = new ZobristBitStrings();

    static ZobristBitStrings getInstance () {
        return ourInstance;
    }

    private ZobristBitStrings () {
        bitSets = new BitSet[64][16];
        initBitSets();
    }

    private void initBitSets () {
        for (int y = 0; y < bitSets.length; y++) {
            for (int x = 0; x < bitSets[y].length; x++) {
                bitSets[y][x] = new BitSet(length);
                for (int i = 0; i < length; i++) {
                    bitSets[y][x].set(i, random.nextBoolean());
                }
            }
        }
    }

    BitSet getSet (Position position, int index) {
        return bitSets[position.getY() * 8 + position.getX()][index];
    }

    public static int bitSetToInt (BitSet bitSet) {
        assert length == 32;
        int bitInteger = 0;

        for (int i = 0; i < 32; i++){
            if (bitSet.get(i)) {
                bitInteger |= (1 << i);
            }
        }
        return bitInteger;
    }

    static long bitSetToLong (BitSet bitSet) {
        assert length == 64;

        long bitLong = 0;

        for (int i = 0; i < 64; i++){
            if (bitSet.get(i)) {
                bitLong |= (1 << i);
            }
        }
        return bitLong;
    }
}
