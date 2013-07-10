package net.yangeorget.jelly;


/**
 * @author y.georget
 */
public class SerializerCountImpl
        extends AbstractSerializer {
    @Override
    void serializeMatrix(final StringBuilder builder, final char[][] matrix) {
        int count = 0;
        for (final char[] line : matrix) {
            for (final char c : line) {
                if (c <= Board.WALL_CHAR) {
                    count++;
                } else {
                    if (count > 0) {
                        builder.append(count);
                        count = 0;
                    }
                    builder.append(c);
                }
            }
        }
    }
}
