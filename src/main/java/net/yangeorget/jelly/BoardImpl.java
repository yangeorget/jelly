package net.yangeorget.jelly;

import java.util.Arrays;


public class BoardImpl
        implements Board {
    private final char[][] matrix;
    private final int height;
    private final int width;

    private final boolean[][] visited;
    private final Jelly[] jelliesBuffer;

    private static final char MASK0 = (char) 223;
    private static final char MASK1 = (char) 32;

    public BoardImpl(final String... strings) {
        height = strings.length;
        width = strings[0].length();
        matrix = new char[height][];
        for (int i = 0; i < height; i++) {
            matrix[i] = strings[i].toCharArray();
        }
        visited = new boolean[height][width];
        jelliesBuffer = new Jelly[height * width];
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
    public Jelly[] getJellies() {
        for (int i = 0; i < height; i++) {
            Arrays.fill(visited[i], false);
        }
        int nb = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                final char color = matrix[i][j];
                if (color != 0 && color != ' ' && !visited[i][j]) {
                    jelliesBuffer[nb++] = new JellyImpl(this, visited, color, i, j);
                }
            }
        }
        final Jelly[] jellies = new Jelly[nb];
        System.arraycopy(jelliesBuffer, 0, jellies, 0, nb);
        return jellies;
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

    public static boolean isFixed(final char c) {
        return (c & MASK1) != 0;
    }

    public static char toFloating(final char c) {
        return c < 'A' ? c : (char) (c & MASK0);
    }
}
