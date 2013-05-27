package net.yangeorget.jelly;

import java.util.HashSet;
import java.util.Set;


public class BoardImpl
        implements Board {
    private final int height;
    private final int width;
    private final int jellyColorNb;
    private final char[][] matrix;
    private final boolean[][] walls;
    private final byte[][] links;

    public BoardImpl(final String[] strings, final byte[]... links) {
        height = strings.length;
        width = strings[0].length();
        matrix = new char[height][];
        walls = new boolean[height][width];
        final Set<Character> colors = new HashSet<>();
        for (int i = 0; i < height; i++) {
            matrix[i] = strings[i].toCharArray();
            for (byte j = 0; j < width; j++) {
                final char color = matrix[i][j];
                if (color == WALL_CHAR) {
                    walls[i][j] = true;
                } else if (color != Board.BLANK_CHAR) {
                    colors.add(BoardImpl.toFloating(color));
                }
            }
        }
        jellyColorNb = colors.size();
        this.links = links;
    }

    @Override
    public int getJellyColorNb() {
        return jellyColorNb;
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
    public String serialize() {
        final StringBuilder builder = new StringBuilder();
        serialize(builder);
        final String serialization = builder.toString();
        int i = 0;
        while (serialization.charAt(i) == Board.BLANK_CHAR) {
            if (++i == serialization.length()) {
                return "";
            }
        }
        return serialization.substring(i);
    }

    private void serialize(final StringBuilder builder) {
        for (int i = 0; i < height; i++) {
            builder.append(matrix[i]);
        }
    }

    @Override
    public boolean[][] getWalls() {
        return walls;
    }

    public static boolean isFixed(final char c) {
        return (c & FIXED_FLAG) != 0;
    }

    public static char toFloating(final char c) {
        return (char) (c & ~FIXED_FLAG);
    }

    public static char toFixed(final char c) {
        return (char) (c | FIXED_FLAG);
    }

    @Override
    public char[][] getMatrix() {
        return matrix;
    }

    @Override
    public byte[][] getLinks() {
        return links;
    }
}
