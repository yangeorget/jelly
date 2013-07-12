package net.yangeorget.jelly;


/**
 * @author y.georget
 */
public class SerializerCountImpl
        implements Serializer {

    @Override
    public Object serialize(final State state) {
        final StringBuilder builder = new StringBuilder();
        final Board board = state.getBoard();
        serializeMatrix(builder, board.getMatrix());
        builder.append(';');
        serializeLinks(builder, board);
        builder.append(';');
        serializeEmerging(builder, state);
        return builder.toString();
    }

    // TODO: serialize as byte array
    void serializeMatrix(final StringBuilder builder, final byte[][] matrix) {
        int count = 0;
        for (final byte[] line : matrix) {
            for (final byte c : line) {
                if (c == Board.SPACE_BYTE || c == Board.WALL_BYTE) {
                    count++;
                } else {
                    if (count > 0) {
                        builder.append(count);
                        count = 0;
                    }
                    builder.append((char) c);
                }
            }
        }
    }

    void serializeLinks(final StringBuilder builder, final Board board) {
        Utils.appendAsHex(builder, board.getLinkStarts());
        Utils.appendAsHex(builder, board.getLinkEnds());
    }

    void serializeEmerging(final StringBuilder builder, final State state) {
        Utils.appendAsInt(builder, state.getEmerged());
        final BoardImpl board = (BoardImpl) state.getBoard();
        Utils.serializeBytes(builder, board.floatingEmergingPositions);
        Utils.appendAsChars(builder, board.floatingEmergingColors);
    }
}
