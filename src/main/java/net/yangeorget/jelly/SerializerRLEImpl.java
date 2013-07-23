package net.yangeorget.jelly;

import java.util.Arrays;


/**
 * @author y.georget
 */
public final class SerializerRLEImpl
        implements Serializer {
    private static final byte[] SERIALIZATION = new byte[Board.MAX_SIZE + 1 + 2 * Board.MAX_SIZE + 1 + Board.MAX_SIZE];
    private static int index;

    @Override
    public final byte[] serialize(final State state) {
        index = 0;
        final Board board = state.getBoard();
        serializeMatrix(board.getMatrix());
        serialize(Board.SER_DELIM_BYTE);
        serialize(board.getLinkStarts());
        serialize(board.getLinkEnds());
        serialize(Utils.asByte(state.getEmerged()));
        serialize(board.getFloatingEmergingPositions());
        serialize(board.getFloatingEmergingColors());
        return Arrays.copyOf(SERIALIZATION, index);
    }

    final void serializeMatrix(final byte[][] matrix) {
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

    private final void serialize(final byte[] a) {
        for (final byte b : a) {
            serialize(b);
        }
    }

    private final void serialize(final byte b) {
        SERIALIZATION[index++] = b;
    }
}
