package net.yangeorget.jelly;


public interface Jelly {
    boolean contains(Position p);

    boolean hMove(int move);

    boolean vMove(int move);

    boolean overlaps(Jelly j);

    Jelly clone();

    int size();

    boolean isFixed();

    char getColor();

    void updateBoard(char[][] board);

    static class Position
            implements Comparable<Position> {
        private int value; // TODO: use a char?

        public Position(final int i, final int j) {
            this(intValue(i, j));
        }

        public Position(final Position p) {
            this(p.getValue());
        }

        private Position(final int value) {
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

        public boolean hMove(final int move, final int width) {
            final int j = getJ() + move;
            if (j < 0 || j >= width) {
                return false;
            }
            value = intValue(getI(), j);
            return true;
        }

        public boolean vMove(final int move, final int height) {
            final int i = getI() + move;
            if (i < 0 || i >= height) {
                return false;
            }
            value = intValue(i, getJ());
            return true;
        }

        @Override
        public int compareTo(final Position o) {
            return value - o.getValue();
        }
    }
}
