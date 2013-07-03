package net.yangeorget.jelly;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * An implementation of a board.
 * @author y.georget
 */
public class BoardImpl
        implements Board {
    private static final byte[] LINK_START_BUF = new byte[MAX_SIZE];
    private static final byte[] LINK_END_BUF = new byte[MAX_SIZE];
    private static int linksIndex;

    private final int height;
    private final int width;
    private final int height1;
    private final int width1;
    private final int jellyColorNb;
    private int jellyPositionNb;
    private final char[][] matrix;
    private final boolean[][] walls;
    private byte[] linkStarts;
    private byte[] linkEnds;
    private final List<Byte> emergingPositions;
    private final List<Character> emergingColors;
    private final List<Byte> floatingEmergingPositions;
    private final List<Character> floatingEmergingColors;

    /**
     * Auxilliary constructor.
     * @param strings the lines of the board
     * @param linkCycles the links as cycles between positions
     */
    public BoardImpl(final String[] strings, final byte[]... linkCycles) {
        this(strings, new byte[0], new char[0], linkCycles);
    }

    /**
     * Main constructor.
     * @param strings the lines of the board as strings
     * @param linkCycles an array of link cycle (each link cycle is a cycle of links between single color jellies)
     */
    public BoardImpl(final String[] strings,
                     final byte[] allEmergingPositions,
                     final char[] allEmergingColors,
                     final byte[]... linkCycles) {
        height = strings.length;
        width = strings[0].length();
        height1 = height - 1;
        width1 = width - 1;
        matrix = new char[height][];
        walls = new boolean[height][width];
        final Set<Character> colors = new HashSet<>();
        computeMatrixAndWalls(colors, strings);
        for (final char color : allEmergingColors) {
            colors.add(BoardImpl.toFloating(color));
        }
        final int allEmergingNb = allEmergingColors.length;
        jellyPositionNb += allEmergingNb;
        jellyColorNb = colors.size();
        computeLinks(linkCycles);
        emergingPositions = new LinkedList<>();
        emergingColors = new LinkedList<>();
        floatingEmergingPositions = new LinkedList<>();
        floatingEmergingColors = new LinkedList<>();
        for (int epIndex = 0; epIndex < allEmergingNb; epIndex++) {
            final byte ep = allEmergingPositions[epIndex];
            final char epColor = allEmergingColors[epIndex];
            if (walls[getI(ep)][getJ(ep)]) {
                emergingPositions.add(ep);
                emergingColors.add(epColor);
            } else {
                floatingEmergingPositions.add(ep);
                floatingEmergingColors.add(epColor);
            }
        }
    }

    void computeMatrixAndWalls(final Set<Character> colors, final String[] strings) {
        for (int i = 0; i < height; i++) {
            matrix[i] = strings[i].toCharArray();
            for (byte j = 0; j < width; j++) {
                final char color = matrix[i][j];
                if (color == WALL_CHAR) {
                    walls[i][j] = true;
                } else if (color != Board.BLANK_CHAR) {
                    colors.add(BoardImpl.toFloating(color));
                    jellyPositionNb++;
                }
            }
        }
    }

    void computeLinks(final byte[][] linkCycles) {
        clearLinks();
        for (final byte[] linkCycle : linkCycles) {
            computeLinks(linkCycle);
        }
        storeLinks();
    }

    /**
     * Creates links from a cycle.
     * @param linkCycle the cycle
     */
    private final void computeLinks(final byte[] linkCycle) {
        final int iMax = linkCycle.length - 1;
        for (int i = 0; i < iMax; i++) {
            addLink(linkCycle[i], linkCycle[i + 1]);
        }
        addLink(linkCycle[iMax], linkCycle[0]);
    }

    @Override
    public void clearLinks() {
        linksIndex = 0;
    }

    @Override
    public final void addLink(final byte start, final byte end) {
        LINK_START_BUF[linksIndex] = start;
        LINK_END_BUF[linksIndex] = end;
        linksIndex++;
    }

    @Override
    public final void storeLinks() {
        linkStarts = Arrays.copyOf(LINK_START_BUF, linksIndex);
        linkEnds = Arrays.copyOf(LINK_END_BUF, linksIndex);
    }

    @Override
    public final int getJellyColorNb() {
        return jellyColorNb;
    }

    @Override
    public final int getJellyPositionNb() {
        return jellyPositionNb;
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
        builder.append(emergingPositions);
        builder.append(";emergingColors=");
        builder.append(emergingColors);
        builder.append(";floatingEmergingPositions=");
        builder.append(floatingEmergingPositions);
        builder.append(";floatingEmergingColors=");
        builder.append(floatingEmergingColors);
        builder.append('\n');
    }

    @Override
    public final boolean isWall(final byte position) {
        return isWall(getI(position), getJ(position));
    }

    @Override
    public final boolean isWall(final int i, final int j) {
        return walls[i][j];
    }

    @Override
    public final boolean isBlank(final byte position) {
        return isBlank(getI(position), getJ(position));
    }

    @Override
    public final boolean isBlank(final int i, final int j) {
        return matrix[i][j] == BLANK_CHAR;
    }

    @Override
    public final boolean isColored(final byte position) {
        return isColored(getI(position), getJ(position));
    }

    @Override
    public final boolean isColored(final int i, final int j) {
        return !isBlank(i, j) && !isWall(i, j);
    }

    @Override
    public final void setColor(final byte position, final char c) {
        setColor(getI(position), getJ(position), c);
    }

    @Override
    public final void setColor(final int i, final int j, final char c) {
        matrix[i][j] = c;
    }

    @Override
    public final char getColor(final byte position) {
        return getColor(getI(position), getJ(position));
    }

    @Override
    public final char getColor(final int i, final int j) {
        return matrix[i][j];
    }

    @Override
    public final void blank(final byte position) {
        blank(getI(position), getJ(position));
    }

    @Override
    public final void blank(final int i, final int j) {
        matrix[i][j] = BLANK_CHAR;
    }

    /**
     * Returns a boolean indicating if the cell is fixed.
     * @param c the color of a cell
     * @return a boolean
     */
    public static final boolean isFixed(final char c) {
        return (c & FIXED_FLAG) != 0;
    }

    /**
     * Converts a cell to a floating cell of the same color.
     * @param c the color of a cell
     * @return a cell
     */
    public static final char toFloating(final char c) {
        return (char) (c & ~FIXED_FLAG);
    }

    /**
     * Converts a cell to a fixed cell of the same color.
     * @param c the color of a cell
     * @return a cell
     */
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
    public final byte getEmergingPosition(final int epIndex) {
        return emergingPositions.get(epIndex);
    }

    @Override
    public final char getEmergingColor(final int epIndex) {
        return emergingColors.get(epIndex);
    }

    @Override
    public int getEmergingPositionNb() {
        return emergingPositions.size();
    }

    @Override
    public int getEmergingIndex(final byte ep) {
        return Collections.binarySearch(emergingPositions, ep);
    }

    final static byte value(final int i, final int j) {
        return (byte) ((i << COORDINATE_UB_LOG2) | j);
    }

    final static int getJ(final int pos) {
        return pos & COORDINATE_MASK;
    }

    final static int getI(final int pos) {
        return (pos >> COORDINATE_UB_LOG2) & COORDINATE_MASK;
    }
}
