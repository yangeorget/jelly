package net.yangeorget.jelly;


/**
 * @author y.georget
 */
public class SerializerTrimImpl
        extends AbstractSerializer {
    @Override
    void serializeMatrix(final StringBuilder builder, final char[][] matrix) {
        boolean skip = true;
        for (final char[] line : matrix) {
            if (skip) {
                for (final char c : line) {
                    skip &= c == Board.BLANK_CHAR || c == Board.WALL_CHAR;
                    if (!skip) {
                        builder.append(c);
                    }
                }
            } else {
                builder.append(line);
            }
        }
    }
}
