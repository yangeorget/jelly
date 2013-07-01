package net.yangeorget.jelly;

/**
 * Utilities.
 * @author y.georget
 */
public final class Utils {
    /**
     * Don't let anybody instantiate this class.
     */
    private Utils() {
    }

    /**
     * Linear time algorithm to find an element in a byte array.
     * @param tab the array
     * @param from the starting point
     * @param to the end (exclusive) point
     * @param val the byte value to look for
     * @return the index where found or -1 if not found
     */
    public final static int contains(final byte[] tab, final int from, final int to, final byte val) {
        for (int i = from; i < to; i++) {
            if (tab[i] == val) {
                return i;
            }
        }
        return -1;
    }
}
