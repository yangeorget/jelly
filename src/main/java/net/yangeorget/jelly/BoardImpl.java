package net.yangeorget.jelly;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class BoardImpl
        implements Board {
    private final int height;
    private final int width;
    private final int height1;
    private final int width1;
    private final int jellyColorNb;
    private final char[][] matrix;
    private final boolean[][] walls;
    private byte[] links0;
    private byte[] links1;

    private static final byte[] LINKS0_BUF = new byte[MAX_SIZE];
    private static final byte[] LINKS1_BUF = new byte[MAX_SIZE];
    private static int linksIndex;

    public BoardImpl(final String[] strings, final byte[]... groups) {
        height = strings.length;
        width = strings[0].length();
        height1 = height - 1;
        width1 = width - 1;
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
        clearLinks();
        for (final byte[] group : groups) {
            createLinks(group);
        }
        updateLinks();
    }

    private final void createLinks(final byte[] group) {
        final int size = group.length;
        for (int i = 0; i < size - 1; i++) {
            storeLink(group[i], group[i + 1]);
        }
        storeLink(group[size - 1], group[0]);
    }


    @Override
    public void clearLinks() {
        linksIndex = 0;
    }

    @Override
    public final void storeLink(final byte start, final byte end) {
        LINKS0_BUF[linksIndex] = start;
        LINKS1_BUF[linksIndex] = end;
        linksIndex++;
    }

    @Override
    public final void updateLinks() {
        links0 = Arrays.copyOf(LINKS0_BUF, linksIndex);
        links1 = Arrays.copyOf(LINKS1_BUF, linksIndex);
    }

    @Override
    public final int getJellyColorNb() {
        return jellyColorNb;
    }

    @Override
    public final int getHeight() {
        return height;
    }

    @Override
    public final int getWidth() {
        return width;
    }

    @Override
    public final int getHeight1() {
        return height1;
    }

    @Override
    public final int getWidth1() {
        return width1;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        toString(builder);
        return builder.toString();
    }

    private final void toString(final StringBuilder builder) {
        final int height1 = height - 1;
        for (int i = 0; i < height1; i++) {
            builder.append(matrix[i]);
            builder.append('\n');
        }
        builder.append(matrix[height1]);
        builder.append('\n');
        for (int i = 0; i < links0.length; i++) {
            builder.append(links0[i]);
            builder.append("-");
            builder.append(links1[i]);
            builder.append('\n');
        }
    }

    @Override
    public final String serialize() {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < height; i++) {
            builder.append(matrix[i]);
        }
        builder.append(Arrays.toString(links0));
        builder.append(Arrays.toString(links1));
        final int size = builder.length();
        for (int i = 0; i < size; i++) {
            final char c = builder.charAt(i);
            if (c != Board.BLANK_CHAR && c != Board.WALL_CHAR) {
                return builder.substring(i);
            }
        }
        // cannot happen
        throw new RuntimeException();
    }

    @Override
    public final boolean[][] getWalls() {
        return walls;
    }

    public static final boolean isFixed(final char c) {
        return (c & FIXED_FLAG) != 0;
    }

    public static final char toFloating(final char c) {
        return (char) (c & ~FIXED_FLAG);
    }

    public static final char toFixed(final char c) {
        return (char) (c | FIXED_FLAG);
    }

    @Override
    public final char[][] getMatrix() {
        return matrix;
    }

    @Override
    public final byte[] getLinks0() {
        return links0;
    }

    @Override
    public final byte[] getLinks1() {
        return links1;
    }
}
