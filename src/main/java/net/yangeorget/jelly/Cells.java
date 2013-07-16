package net.yangeorget.jelly;

/**
 * @author y.georget
 */
public final class Cells {
    /**
     * The upper bound (excluded) of a coordinate.
     */
    static final int COORDINATE_UB = 16;

    private Cells() {
    }

    static byte value(final int i, final int j) {
        return (byte) ((i << 4) | j);
    }

    static int getJ(final byte pos) {
        return pos & 0xF;
    }

    static int getI(final byte pos) {
        return (pos >> 4) & 0xF;
    }
}
