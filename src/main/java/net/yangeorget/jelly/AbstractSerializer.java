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
        serializeBytes(builder, board.getLinkStarts());
        serializeBytes(builder, board.getLinkEnds());
    }

    void serializeEmerging(final StringBuilder builder, final State state) {
        serializeBooleans(builder, state.getEmerged());
        final BoardImpl board = (BoardImpl) state.getBoard();
        serializeBytes(builder, board.floatingEmergingPositions);
        serializeBytesAsChars(builder, board.floatingEmergingColors);
    }

    /**
     * Serializes the matrix.
     * @param builder the builder to serialize into
     * @param matrix the matrix
     */
    abstract void serializeMatrix(final StringBuilder builder, final byte[][] matrix);

    /**
     * Serializes a boolean array as an integer.
     * @param builder the builder to serialize into
     * @param a the boolean array
     */
    static void serializeBooleans(final StringBuilder builder, final boolean[] a) {
        final int size = a.length;
        if (size > Board.MAX_EMERGING) {
            throw new RuntimeException("Too many emerging jellies!");
        }
        int ser = 0;
        for (int i = 0; i < size; i++) {
            if (a[i]) {
                ser |= 1 << (size - 1 - i);
            }
        }
        builder.append(ser);
    }

    static void serializeBytes(final StringBuilder builder, final byte[] a) {
        for (final byte b : a) {
            builder.append(String.format("%02X", b));
        }
    }

    static void serializeBytes(final StringBuilder builder, final List<Byte> a) {
        for (final Byte b : a) {
            builder.append(String.format("%02X", b));
        }
    }

    static void serializeBytesAsChars(final StringBuilder builder, final List<Byte> a) {
        for (final Byte b : a) {
            builder.append(BoardImpl.toChar(b));
        }
    }

    static void serializeBytesAsChars(final StringBuilder builder, final byte[] a) {
        for (final byte b : a) {
            builder.append(BoardImpl.toChar(b));
        }
    }
}
