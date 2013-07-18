package net.yangeorget.jelly;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * An implementation of a board.
 * @author y.georget
 */
public final class BoardImpl
        implements Board {
    private static final byte[] LINK_START_BUF = new byte[MAX_SIZE], LINK_END_BUF = new byte[MAX_SIZE],
            EP_POS_BUF = new byte[MAX_SIZE], EP_COL_BUF = new byte[MAX_SIZE], FEP_POS_BUF = new byte[MAX_SIZE],
            FEP_COL_BUF = new byte[MAX_SIZE];
    private static final byte a_A_DELTA = a_BYTE - A_BYTE;
    private static int linksIndex;
    private static int epIndex;
    private static int fepIndex;

    private final int height, width, height1, width1, jellyColorNb;
    private int jellyPositionNb;
    private final byte[][] matrix;
    private final boolean[][] walls;
    private byte[] linkStarts, linkEnds, emergingPositions, emergingColors, floatingEmergingPositions,
            floatingEmergingColors;

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
            colors.add(BoardImpl.toFloating((byte) color));
        }
        jellyColorNb = colors.size();
        jellyPositionNb += allEmergingColors.length;
        computeLinks(linkCycles);
        computeEP(allEmergingPositions, allEmergingColors);
    }

    final void computeEP(final byte[] allEmergingPositions, final char[] allEmergingColors) {
        clearEmerging();
        clearFloatingEmerging();
        for (int epIndex = 0; epIndex < allEmergingColors.length; epIndex++) {
            addEP(allEmergingPositions[epIndex], (byte) allEmergingColors[epIndex]);
        }
        storeEmerging();
        storeFloatingEmerging();
    }

    final void computeLinks(final byte[][] linkCycles) {
        clearLinks();
        for (final byte[] linkCycle : linkCycles) {
            computeLinks(linkCycle);
        }
        storeLinks();
    }

    final void computeMatrixAndWalls(final Set<Byte> colors, final String[] strings) {
        for (int i = 0; i < height; i++) {
            matrix[i] = new byte[width];
            for (byte j = 0; j < width; j++) {
                matrix[i][j] = (byte) strings[i].charAt(j);
                if (matrix[i][j] == WALL_BYTE) {
                    walls[i][j] = true;
                } else if (matrix[i][j] != SPACE_BYTE) {
                    colors.add(BoardImpl.toFloating(matrix[i][j]));
                    jellyPositionNb++;
                }
            }
        }
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
    public final void clearLinks() {
        linksIndex = 0;
    }

    final void clearEmerging() {
        epIndex = 0;
    }

    @Override
    public final void clearFloatingEmerging() {
        fepIndex = 0;
    }

    @Override
    public final void addLink(final byte start, final byte end) {
        LINK_START_BUF[linksIndex] = start;
        LINK_END_BUF[linksIndex] = end;
        linksIndex++;
    }

    final void addEP(final byte ep, final byte color) {
        if (walls[Cells.getI(ep)][Cells.getJ(ep)]) {
            addEmerging(ep, color);
        } else {
            addFloatingEmerging(ep, color);
        }
    }

    final void addEmerging(final byte ep, final byte color) {
        EP_POS_BUF[epIndex] = ep;
        EP_COL_BUF[epIndex] = color;
        epIndex++;
    }

    @Override
    public final void addFloatingEmerging(final byte ep, final byte color) {
        FEP_POS_BUF[fepIndex] = ep;
        FEP_COL_BUF[fepIndex] = color;
        fepIndex++;
    }

    @Override
    public final void storeLinks() {
        linkStarts = Arrays.copyOf(LINK_START_BUF, linksIndex);
        linkEnds = Arrays.copyOf(LINK_END_BUF, linksIndex);
    }

    final void storeEmerging() {
        emergingPositions = Arrays.copyOf(EP_POS_BUF, epIndex);
        emergingColors = Arrays.copyOf(EP_COL_BUF, epIndex);
    }

    @Override
    public final void storeFloatingEmerging() {
        floatingEmergingPositions = Arrays.copyOf(FEP_POS_BUF, fepIndex);
        floatingEmergingColors = Arrays.copyOf(FEP_COL_BUF, fepIndex);
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
    public final String toString() {
        final StringBuilder builder = new StringBuilder();
        toString(builder);
        return builder.toString();
    }

    private final void toString(final StringBuilder builder) {
        for (int i = 0; i < height; i++) {
            for (final byte b : matrix[i]) {
                builder.append((char) b);
            }
            builder.append('\n');
        }
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
        builder.append(";floatingEmergingPositions=");
        builder.append(Arrays.toString(floatingEmergingPositions));
        builder.append(";floatingEmergingColors=");
        builder.append(Arrays.toString(floatingEmergingColors));
        builder.append('\n');
    }

    @Override
    public final boolean isWall(final byte position) {
        return walls[Cells.getI(position)][Cells.getJ(position)];
    }

    @Override
    public final void setColor(final byte position, final byte c) {
        matrix[Cells.getI(position)][Cells.getJ(position)] = c;
    }

    @Override
    public final byte getColor(final int i, final int j) {
        return matrix[i][j];
    }

    @Override
    public final void blank(final int i, final int j) {
        matrix[i][j] = SPACE_BYTE;
    }

    /**
     * Returns a boolean indicating if the cell is floating.
     * @param c the color of a cell
     * @return a boolean
     */
    public final static boolean isFloating(final byte c) {
        return c < a_BYTE;
    }

    /**
     * Converts a cell to a floating cell of the same color.
     * @param c the color of a cell
     * @return a cell
     */
    public final static byte toFloating(final byte c) {
        if (isFloating(c)) {
            return c;
        } else {
            return (byte) (c - a_A_DELTA);
        }
    }

    /**
     * Converts a cell to a fixed cell of the same color.
     * @param c the color of a cell
     * @return a cell
     */
    public final static byte toFixed(final byte c) {
        if (isFloating(c)) {
            return (byte) (c + a_A_DELTA);
        } else {
            return c;
        }
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
    public final byte[] getFloatingEmergingPositions() {
        return floatingEmergingPositions;
    }

    @Override
    public final byte[] getFloatingEmergingColors() {
        return floatingEmergingColors;
    }

    @Override
    public final byte getEmergingPosition(final int epIndex) {
        return emergingPositions[epIndex];
    }

    @Override
    public final byte getEmergingColor(final int epIndex) {
        return emergingColors[epIndex];
    }

    @Override
    public final int getEmergingPositionNb() {
        return emergingPositions.length;
    }

    @Override
    public final byte getFloatingEmergingPosition(final int epIndex) {
        return floatingEmergingPositions[epIndex];
    }

    @Override
    public final byte getFloatingEmergingColor(final int epIndex) {
        return floatingEmergingColors[epIndex];
    }

    @Override
    public final int getFloatingEmergingPositionNb() {
        return floatingEmergingPositions.length;
    }

    @Override
    public final int getEmergingIndex(final byte ep) {
        return Arrays.binarySearch(emergingPositions, ep);
    }
}
