package net.yangeorget.jelly;


/**
 * @author y.georget
 */
public class SerializerCountImpl
        extends AbstractSerializer {
    @Override
    void serializeMatrix(final StringBuilder builder, final byte[][] matrix) {
        int count = 0;
        for (final byte[] line : matrix) {
            for (final byte c : line) {
                if (!BoardImpl.isJelly(c)) {
                    count++;
                } else {
                    if (count > 0) {
                        builder.append(count);
                        count = 0;
                    }
                    builder.append(BoardImpl.toChar(c));
                }
            }
        }
    }
}
