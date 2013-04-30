package net.yangeorget.jelly;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class BoardImpl
        implements Board {
    private final char[][] matrix;
    private final int height;
    private final int width;
    final boolean[][] visited;

    public BoardImpl(final char[][] matrix) {
        this.matrix = matrix;
        height = matrix.length;
        width = matrix[0].length;
        visited = new boolean[height][width];
    }

    public BoardImpl(final String... strings) {
        height = strings.length;
        width = strings[0].length();
        matrix = new char[height][];
        for (int i = 0; i < height; i++) {
            matrix[i] = strings[i].toCharArray();
        }
        visited = new boolean[height][width];
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        toString(builder);
        return builder.toString();
    }

    private void toString(final StringBuilder builder) {
        for (int i = 0; i < height - 1; i++) {
            builder.append(matrix[i]);
            builder.append('\n');
        }
        builder.append(matrix[height - 1]);
    }

    @Override
    public char get(final int i, final int j) {
        return matrix[i][j];
    }

    @Override
    public boolean equals(final Object o) {
        return Arrays.deepEquals(matrix, ((BoardImpl) o).matrix);
    }

    @Override
    public List<Jelly> getJellies() {
        for (int i = 0; i < height; i++) {
            Arrays.fill(visited[i], false);
        }
        final List<Jelly> jellies = new LinkedList<>();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (!visited[i][j] && !Character.isWhitespace((matrix[i][j]))) {
                    jellies.add(new JellyImpl(this, visited, matrix[i][j], i, j));
                }
            }
        }
        return jellies;
    }

    @Override
    public boolean cellIsFixed(final int i, final int j) {
        return isFixed(matrix[i][j]);
    }

    public static boolean isFixed(final char color) {
        return !Character.isUpperCase(color);
    }

    @Override
    public boolean cellHasColor(final int i, final int j, final char color) {
        return getColor(matrix[i][j]) == getColor(color);
    }

    public static char getColor(final char color) {
        return Character.toUpperCase(color);
    }

    @Override
    public void clear() {
        for (int i = 0; i < height; i++) {
            Arrays.fill(matrix[i], ' ');
        }
    }

    @Override
    public void set(final byte i, final byte j, final char color) {
        matrix[i][j] = color;
    }
}
