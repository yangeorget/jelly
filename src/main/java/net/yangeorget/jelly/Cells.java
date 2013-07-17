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

    static final byte value(final int i, final int j) {
        return (byte) ((i << 4) | j);
    }

    static final int getJ(final byte pos) {
        return pos & 0xF;
    }

    static final int getI(final byte pos) {
        return (pos >> 4) & 0xF;
    }
}
