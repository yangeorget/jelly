package net.yangeorget.jelly;

import java.util.Arrays;
import java.util.List;


/**
 * @author y.georget
 */
public class SerializerCountImpl
        implements Serializer {
    private static final byte[] SERIALIZATION = new byte[Board.MAX_SIZE + 1 + 2 * Board.MAX_SIZE + 1 + Board.MAX_SIZE];
    private static int index;

    @Override
    public byte[] serialize(final State state) {
        index = 0;
        final BoardImpl board = (BoardImpl) state.getBoard();
        serializeMatrix(board.getMatrix());
        serialize(Board.SER_DELIM_BYTE);
        serialize(board.getLinkStarts());
        serialize(board.getLinkEnds());
        serialize(Board.SER_DELIM_BYTE);
        serialize(Utils.asByte(state.getEmerged()));
        serialize(board.floatingEmergingPositions);
        serialize(board.floatingEmergingColors);
        return Arrays.copyOf(SERIALIZATION, index);
    }

    void serializeMatrix(final byte[][] matrix) {
        int count = 0;
        byte ch = 0;
        for (final byte[] line : matrix) {
            for (byte c : line) {
                if (c == Board.WALL_BYTE) {
                    c = Board.SPACE_BYTE;
                }
                if (count == 0) {
                    count = 1;
                    ch = c;
                } else if (c == ch) {
                    count++;
                } else {
                    serialize((byte) count);
                    serialize(ch);
                    count = 1;
                    ch = c;
                }
            }
        }
        serialize((byte) count);
        serialize(ch);
    }

    private void serialize(final byte[] a) {
        for (final byte b : a) {
            serialize(b);
        }
    }

    private void serialize(final List<Byte> a) {
        for (final Byte b : a) {
            serialize(b);
        }
    }

    private void serialize(final byte b) {
        SERIALIZATION[index++] = b;
    }
}
