package net.yangeorget.jelly;

import java.util.List;

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

    static void appendAsChars(final StringBuilder builder, final List<Byte> a) {
        for (final Byte b : a) {
            builder.append((char) b.byteValue());
        }
    }

    static void appendAsChars(final StringBuilder builder, final byte[] a) {
        for (final byte b : a) {
            builder.append((char) b);
        }
    }

    /**
     * Serializes a boolean array as an integer.
     * @param builder the builder to serialize into
     * @param a the boolean array
     */
    static void appendAsInt(final StringBuilder builder, final boolean[] a) {
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

    static void appendAsHex(final StringBuilder builder, final byte[] a) {
        for (final byte b : a) {
            builder.append(String.format("%02X", b));
        }
    }

    static void serializeBytes(final StringBuilder builder, final List<Byte> a) {
        for (final Byte b : a) {
            builder.append(String.format("%02X", b));
        }
    }
}
