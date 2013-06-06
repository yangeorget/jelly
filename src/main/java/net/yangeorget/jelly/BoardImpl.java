package net.yangeorget.jelly;

import java.util.Arrays;
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

    private static final byte[][] LINKS_BUF = { new byte[Board.MAX_WIDTH * Board.MAX_HEIGHT],
                                               new byte[Board.MAX_WIDTH * Board.MAX_HEIGHT] };

    public BoardImpl(final String[] strings) {
        this(strings, new byte[][] { {}, {} });
    }

    public BoardImpl(final String[] strings, final byte[][] links) {
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
        builder.append('\n');
        for (int i = 0; i < links[0].length; i++) {
            builder.append(links[0][i]);
            builder.append("-");
            builder.append(links[1][i]);
            builder.append('\n');
        }
    }

    @Override
    public String serialize() { // TODO: fix to take into account links
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < height; i++) {
            builder.append(matrix[i]);
        }
        final String serialization = builder.toString();
        for (int i = 0; i < serialization.length(); i++) {
            final char c = serialization.charAt(i);
            if (c != Board.BLANK_CHAR && c != Board.WALL_CHAR) {
                return serialization.substring(i);
            }
        }
        return "";
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
    public byte[] getLinks(final int index) {
        return links[index];
    }

    @Override
    public void updateLinks(final int index) {
        links[0] = Arrays.copyOf(LINKS_BUF[0], index);
        links[1] = Arrays.copyOf(LINKS_BUF[1], index);
    }

    @Override
    public void storeLink(final int index, final byte start, final byte end) {
        final int index1 = index + 1;
        LINKS_BUF[0][index] = LINKS_BUF[1][index1] = start;
        LINKS_BUF[0][index1] = LINKS_BUF[1][index] = end;
    }
}
