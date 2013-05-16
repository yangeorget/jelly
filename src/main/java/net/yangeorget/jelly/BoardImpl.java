package net.yangeorget.jelly;

import java.util.Arrays;


public class BoardImpl
        implements Board {
    private final char[][] matrix;
    private final int height;
    private final int width;

    private final boolean[][] visited;
    private final Jelly[] jelliesBuffer;
    private final int wallNb;

    private static final char FIXED_FLAG = (char) 32;
    private static final char BLANK_CHAR = ' ';
    private static final char A_CHAR = 'A';

    public BoardImpl(final String... strings) {
        height = strings.length;
        width = strings[0].length();
        matrix = new char[height][];
        for (int i = 0; i < height; i++) {
            matrix[i] = strings[i].toCharArray();
        }
        visited = new boolean[height][width];
        jelliesBuffer = new Jelly[height * width];
        wallNb = computeWalls(0);
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
        final int height1 = height - 1;
        for (int i = 0; i < height1; i++) {
            builder.append(matrix[i]);
            builder.append('\n');
        }
        builder.append(matrix[height1]);
    }

    @Override
    public char get(final int i, final int j) {
        return matrix[i][j];
    }

    @Override
    public void set(final int i, final int j, final char color) {
        matrix[i][j] = color;
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
        final int nb = computeJellies(wallNb);
        final Jelly[] jellies = new Jelly[nb];
        System.arraycopy(jelliesBuffer, 0, jellies, 0, nb);
        return jellies;
    }

    private int computeJellies(int nb) {
        for (byte i = 0; i < height; i++) {
            for (byte j = 0; j < width; j++) {
                final char color = matrix[i][j];
                if (color != BLANK_CHAR && color >= A_CHAR && !visited[i][j]) {
                    jelliesBuffer[nb++] = new JellyImpl(this, visited, color, i, j);
                }
            }
        }
        return nb;
    }

    private int computeWalls(int nb) {
        for (byte i = 0; i < height; i++) {
            for (byte j = 0; j < width; j++) {
                final char color = matrix[i][j];
                if (color != BLANK_CHAR && color < A_CHAR && !visited[i][j]) {
                    jelliesBuffer[nb++] = new JellyImpl(this, visited, color, i, j);
                }
            }
        }
        return nb;
    }

    @Override
    public void clear() {
        for (int i = 0; i < height; i++) {
            Arrays.fill(matrix[i], BLANK_CHAR);
        }
    }

    public static boolean isFixed(final char c) {
        return (c & FIXED_FLAG) != 0;
    }

    public static char toFloating(final char c) {
        return c < A_CHAR ? c : (char) (c & ~FIXED_FLAG);
    }
}
