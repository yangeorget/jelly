package net.yangeorget.jelly;

public class Position
        implements Comparable<Position> {
    private int value; // TODO: use a char?

    public Position(final int i, final int j) {
        this(intValue(i, j));
    }

    public Position(final Position p) {
        this(p.getValue());
    }

    public Position(final int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "(" + getI() + "," + getJ() + ")";
    }

    int getI() {
        return value >> 4;
    }

    int getJ() {
        return value & 0xF;
    }

    public int getValue() {
        return value;
    }

    private static int intValue(final int i, final int j) {
        return (i << 4) + j;
    }

    public boolean moveHorizontally(final int move, final int width) {
        final int j = getJ() + move;
        if (j < 0 || j >= width) {
            return false;
        }
        value = intValue(getI(), j);
        return true;
    }

    @Override
    public int compareTo(final Position o) {
        return value - o.getValue();
    }
}
