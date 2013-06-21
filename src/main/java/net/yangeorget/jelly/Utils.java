package net.yangeorget.jelly;

public final class Utils {
    private Utils() {
    }

    public final static int contains(final byte[] tab, final int from, final int to, final byte val) {
        for (int i = from; i < to; i++) {
            if (tab[i] == val) {
                return i;
            }
        }
        return -1;
    }
}
