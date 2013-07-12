package net.yangeorget.jelly;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * An implementation of a board.
 * @author y.georget
 */
public class BoardImpl
        implements Board {
    private static final byte[] LINK_START_BUF = new byte[MAX_SIZE], LINK_END_BUF = new byte[MAX_SIZE];
    private static final byte WALL_SPACE_DELTA = 3, A_SPACE_DELTA = 33, a_A_DELTA = 32, a_SPACE_DELTA = A_SPACE_DELTA
                                                                                                        + a_A_DELTA;
    private static int linksIndex;

    private final int height, width, height1, width1, jellyColorNb;
    private int jellyPositionNb;
    private final byte[][] matrix;
    private final boolean[][] walls;
    private byte[] linkStarts, linkEnds;
    final List<Byte> emergingPositions, emergingColors, floatingEmergingPositions, floatingEmergingColors;

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
        matrix = new byte[height][];
        walls = new boolean[height][width];
        final Set<Byte> colors = new HashSet<>();
        computeMatrixAndWalls(colors, strings);
        for (final char color : allEmergingColors) {
            colors.add(BoardImpl.toFloating(toByte(color)));
        }
        final int allEmergingNb = allEmergingColors.length;
        jellyPositionNb += allEmergingNb;
        jellyColorNb = colors.size();
        computeLinks(linkCycles);
        emergingPositions = new ArrayList<>(allEmergingNb);
        emergingColors = new ArrayList<>(allEmergingNb);
        floatingEmergingPositions = new ArrayList<>(allEmergingNb);
        floatingEmergingColors = new ArrayList<>(allEmergingNb);
        for (int epIndex = 0; epIndex < allEmergingNb; epIndex++) {
            final byte ep = allEmergingPositions[epIndex];
            final byte epColor = toByte(allEmergingColors[epIndex]);
            if (walls[getI(ep)][getJ(ep)]) {
                emergingPositions.add(ep);
                emergingColors.add(epColor);
            } else {
                floatingEmergingPositions.add(ep);
                floatingEmergingColors.add(epColor);
            }
        }
    }

    static byte toByte(final char c) {
        return (byte) (c - ' ');
    }

    static char toChar(final byte spaceDelta) {
        return (char) (spaceDelta + ' ');
    }

    void computeMatrixAndWalls(final Set<Byte> colors, final String[] strings) {
        for (int i = 0; i < height; i++) {
            matrix[i] = new byte[width];
            for (byte j = 0; j < width; j++) {
                matrix[i][j] = toByte(strings[i].charAt(j));
                if (matrix[i][j] == WALL_SPACE_DELTA) {
                    walls[i][j] = true;
                } else if (matrix[i][j] != 0) {
                    colors.add(BoardImpl.toFloating(matrix[i][j]));
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
        for (int i = 0; i < height; i++) {
            AbstractSerializer.serializeBytesAsChars(builder, matrix[i]);
            builder.append('\n');
        }
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


    public static boolean isJelly(final byte c) {
        return c >= A_SPACE_DELTA;
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
        return matrix[i][j] == 0;
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
    public final void setColor(final byte position, final byte c) {
        setColor(getI(position), getJ(position), c);
    }

    @Override
    public final void setColor(final int i, final int j, final byte c) {
        matrix[i][j] = c;
    }

    @Override
    public final byte getColor(final byte position) {
        return getColor(getI(position), getJ(position));
    }

    @Override
    public final byte getColor(final int i, final int j) {
        return matrix[i][j];
    }

    @Override
    public final void blank(final byte position) {
        blank(getI(position), getJ(position));
    }

    @Override
    public final void blank(final int i, final int j) {
        matrix[i][j] = 0;
    }

    /**
     * Returns a boolean indicating if the cell is fixed.
     * @param c the color of a cell
     * @return a boolean
     */
    public static final boolean isFixed(final byte c) {
        return c >= a_SPACE_DELTA;
    }

    /**
     * Converts a cell to a floating cell of the same color.
     * @param c the color of a cell
     * @return a cell
     */
    public static final byte toFloating(final byte c) {
        return isFixed(c) ? (byte) (c - a_A_DELTA) : c; // TODO: optimize
    }

    /**
     * Converts a cell to a fixed cell of the same color.
     * @param c the color of a cell
     * @return a cell
     */
    public static final byte toFixed(final byte c) {
        return isFixed(c) ? c : (byte) (c + a_A_DELTA); // TODO: optimize
    }

    @Override
    public final byte[][] getMatrix() {
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
    public final byte getEmergingColor(final int epIndex) {
        return emergingColors.get(epIndex);
    }

    @Override
    public int getEmergingPositionNb() {
        return emergingPositions.size();
    }

    @Override
    public void clearFloatingEmerging() {
        floatingEmergingPositions.clear();
        floatingEmergingColors.clear();
    }

    @Override
    public void addFloatingEmerging(final byte emergingPosition, final byte emergingColor) {
        floatingEmergingPositions.add(emergingPosition);
        floatingEmergingColors.add(emergingColor);
    }

    @Override
    public final byte getFloatingEmergingPosition(final int epIndex) {
        return floatingEmergingPositions.get(epIndex);
    }

    @Override
    public final byte getFloatingEmergingColor(final int epIndex) {
        return floatingEmergingColors.get(epIndex);
    }

    @Override
    public int getFloatingEmergingPositionNb() {
        return floatingEmergingPositions.size();
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
