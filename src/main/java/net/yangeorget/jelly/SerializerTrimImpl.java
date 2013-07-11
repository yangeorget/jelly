package net.yangeorget.jelly;


/**
 * @author y.georget
 */
public class SerializerTrimImpl
        extends AbstractSerializer {
    @Override
    void serializeMatrix(final StringBuilder builder, final byte[][] matrix) {
        boolean skip = true;
        for (final byte[] line : matrix) {
            if (skip) {
                for (final byte c : line) {
                    skip &= !BoardImpl.isJelly(c);
                    if (!skip) {
                        builder.append(BoardImpl.toChar(c));
                    }
                }
            } else {
                builder.append(line);
            }
        }
    }
}
