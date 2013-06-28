package net.yangeorget.jelly;


/**
 * @author y.georget
 */
public abstract class AbstractSerializer
        implements Serializer {
    @Override
    public StringBuilder serialize(final State state) {
        final StringBuilder builder = new StringBuilder();
        final Board board = state.getBoard();
        serializeMatrix(builder, board.getMatrix());
        builder.append(';');
        serializeByteArray(builder, board.getLinkStarts());
        serializeByteArray(builder, board.getLinkEnds());
        builder.append(';');
        serializeBooleanArray(builder, state.getEmerged());
        return builder;
    }

    abstract void serializeMatrix(final StringBuilder builder, final char[][] matrix);

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
        if (ser != 0) {
            builder.append(ser);
        }
    }

    static void serializeByteArray(final StringBuilder builder, final byte[] a) {
        for (int i = 0; i < a.length; i++) {
            builder.append(String.format("%02X", a[i]));
        }
    }
}
