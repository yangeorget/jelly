package net.yangeorget.jelly;


public class BoardImpl
        implements Board {
    private final char[][] matrix;

    public BoardImpl(final char[][] matrix) {
        this.matrix = matrix;
    }

    public BoardImpl(final String... strings) {
        final int height = strings.length;
        matrix = new char[height][];
        for (int i = 0; i < height; i++) {
            matrix[i] = strings[i].toCharArray();
        }
    }

    @Override
    public int getHeight() {
        return matrix.length;
    }

    @Override
    public int getWidth() {
        return matrix[0].length;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        toString(builder);
        return builder.toString();
    }

    private void toString(final StringBuilder builder) {
        for (int i = 0; i < getHeight(); i++) {
            for (int j = 0; j < getWidth(); j++) {
                builder.append(matrix[i][j]);
            }
            if (i < getHeight() - 1) {
                builder.append("\n");
            }
        }
    }

    @Override
    public char get(final int i, final int j) {
        return matrix[i][j];
    }

    @Override
    public boolean equals(final Object o) {
        final Board b = (Board) o;
        if (getHeight() != b.getHeight() || getWidth() != b.getWidth()) {
            return false;
        }
        for (int i = 0; i < getHeight(); i++) {
            for (int j = 0; j < getWidth(); j++) {
                if (get(i, j) != b.get(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }
}
