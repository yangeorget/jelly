package net.yangeorget.jelly;

import java.util.List;

/**
 * Abstract serializer.
 * @author y.georget
 */
public abstract class AbstractSerializer
        implements Serializer {
    @Override
    public String serialize(final State state) {
        final StringBuilder builder = new StringBuilder();
        final Board board = state.getBoard();
        serializeMatrix(builder, board.getMatrix());
        builder.append(';');
        serializeLinks(builder, board);
        builder.append(';');
        serializeEmerging(builder, state);
        return builder.toString();
    }

    void serializeLinks(final StringBuilder builder, final Board board) {
        serializeByteArray(builder, board.getLinkStarts());
        serializeByteArray(builder, board.getLinkEnds());
    }

    void serializeEmerging(final StringBuilder builder, final State state) {
        serializeBooleanArray(builder, state.getEmerged());
        final BoardImpl board = (BoardImpl) state.getBoard();
        serializeByteList(builder, board.floatingEmergingPositions);
        serializeCharList(builder, board.floatingEmergingColors);
    }

    /**
     * Serializes the matrix.
     * @param builder the builder to serialize into
     * @param matrix the matrix
     */
    abstract void serializeMatrix(final StringBuilder builder, final char[][] matrix);

    /**
     * Serializes a boolean array as an integer.
     * @param builder the builder to serialize into
     * @param a the boolean array
     */
    static void serializeBooleanArray(final StringBuilder builder, final boolean[] a) {
        if (a.length > Board.MAX_EMERGING) {
            throw new RuntimeException("Too many emerging jellies!");
        }
        int ser = 0;
        for (int i = 0; i < a.length; i++) {
            if (a[i]) {
                ser |= 1 << (a.length - 1 - i);
            }
        }
        builder.append(ser);
    }

    static void serializeByteArray(final StringBuilder builder, final byte[] a) {
        for (int i = 0; i < a.length; i++) {
            builder.append(String.format("%02X", a[i]));
        }
    }

    static void serializeByteList(final StringBuilder builder, final List<Byte> a) {
        for (final Byte b : a) {
            builder.append(String.format("%02X", b));
        }
    }

    static void serializeCharList(final StringBuilder builder, final List<Character> a) {
        for (final Character b : a) {
            builder.append(b);
        }
    }
}
