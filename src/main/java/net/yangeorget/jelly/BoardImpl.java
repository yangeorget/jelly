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
    private int jellyColorNb;
    private final char[][] matrix;
    private final boolean[][] walls;
    private byte[] linkStarts;
    private byte[] linkEnds;
    private final byte[] emergingPositions;
    private final char[] emergingColors;

    private static final byte[] LINK_START_BUF = new byte[MAX_SIZE];
    private static final byte[] LINK_END_BUF = new byte[MAX_SIZE];
    private static int linksIndex;

    public BoardImpl(final String[] strings, final byte[]... linkCycles) {
        this(strings, new byte[0], new char[0], linkCycles);
    }

    /**
     * Main constructor.
     * @param strings the lines of the board as strings
     * @param linkCycles an array of link cycle (each link cycle is a cycle of links between single color jellies)
     */
    public BoardImpl(final String[] strings,
                     final byte[] emergingPositions,
                     final char[] emergingColors,
                     final byte[]... linkCycles) {
        height = strings.length;
        width = strings[0].length();
        height1 = height - 1;
        width1 = width - 1;
        matrix = new char[height][];
        walls = new boolean[height][width];
        this.emergingPositions = emergingPositions;
        this.emergingColors = emergingColors;
        processBoardData(strings);
        clearLinks();
        for (final byte[] linkCycle : linkCycles) {
            createLinks(linkCycle);
        }
        updateLinks();
    }

    void processBoardData(final String[] strings) {
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
        for (final char color : emergingColors) {
            colors.add(BoardImpl.toFloating(color));
        }
        jellyColorNb = colors.size();
    }

    private final void createLinks(final byte[] linkCycle) {
        final int size = linkCycle.length;
        for (int i = 0; i < size - 1; i++) {
            storeLink(linkCycle[i], linkCycle[i + 1]);
        }
        storeLink(linkCycle[size - 1], linkCycle[0]);
    }


    @Override
    public void clearLinks() {
        linksIndex = 0;
    }

    @Override
    public final void storeLink(final byte start, final byte end) {
        LINK_START_BUF[linksIndex] = start;
        LINK_END_BUF[linksIndex] = end;
        linksIndex++;
    }

    @Override
    public final void updateLinks() {
        linkStarts = Arrays.copyOf(LINK_START_BUF, linksIndex);
        linkEnds = Arrays.copyOf(LINK_END_BUF, linksIndex);
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
        for (int i = 0; i < linkStarts.length; i++) {
            builder.append(linkStarts[i]);
            builder.append("-");
            builder.append(linkEnds[i]);
            builder.append('\n');
        }
        builder.append(";emergingPositions=");
        builder.append(Arrays.toString(emergingPositions));
        builder.append(";emergingColors=");
        builder.append(Arrays.toString(emergingColors));
        builder.append('\n');
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
    public final byte[] getLinkStarts() {
        return linkStarts;
    }

    @Override
    public final byte[] getLinkEnds() {
        return linkEnds;
    }

    @Override
    public final byte[] getEmergingPositions() {
        return emergingPositions;
    }

    @Override
    public final char[] getEmergingColors() {
        return emergingColors;
    }
}
