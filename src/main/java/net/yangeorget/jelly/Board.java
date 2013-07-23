package net.yangeorget.jelly;

/**
 * A board
 * @author y.georget
 */
public interface Board
        extends JellyCounters {
    byte SPACE_BYTE = 32;
    byte WALL_BYTE = 35;
    byte A_BYTE = 65;
    byte a_BYTE = 97;
    byte SER_DELIM_BYTE = -1;

    /**
     * The maximal number of emerging jellies.
     */
    int MAX_EMERGING_FIXED = 8;
    int MAX_EMERGING_FLOATING = 32;

    int MAX_WIDTH = Cells.COORDINATE_UB;
    int MAX_HEIGHT = Cells.COORDINATE_UB;
    int MAX_SIZE = MAX_WIDTH * MAX_HEIGHT;

    int LEFT = -1;
    int RIGHT = 1;
    int UP = -MAX_WIDTH;
    int DOWN = MAX_WIDTH;

    /**
     * Returns the height of the board.
     * @return an int
     */
    int getHeight();

    /**
     * Returns the width of the board.
     * @return an int
     */
    int getWidth();

    /**
     * Returns the maximal vertical position in the board.
     * @return an int
     */
    int getHeight1();

    /**
     * Returns the maximal horizontal position in the board.
     * @return an int
     */
    int getWidth1();

    /**
     * Returns the matrix.
     * @return a bi-dimensional array of char
     */
    byte[][] getMatrix();

    void setColor(byte position, byte color);

    void blank(int i, int j);

    boolean isWall(byte position);

    byte getColor(int i, int j);

    /**
     * Returns the starts of the links.
     * @return a array of bytes
     */
    byte[] getLinkStarts();

    /**
     * Returns the ends of the links.
     * @return a array of bytes
     */
    byte[] getLinkEnds();

    void clearFloatingEmerging();

    void storeFloatingEmerging();

    /**
     * @param emergingPosition
     * @param emergingColor
     */
    void addFloatingEmerging(byte emergingPosition, byte emergingColor);

    byte getFloatingEmergingPosition(int epIndex);

    byte getFloatingEmergingColor(int epIndex);

    int getFloatingEmergingPositionNb();

    byte getEmergingColor(int epIndex);

    byte getEmergingPosition(int epIndex);

    int getEmergingPositionNb();

    int getEmergingIndex(byte ep);

    byte[] getFloatingEmergingPositions();

    byte[] getFloatingEmergingColors();

    /**
     * To be called before storing links.
     */
    void clearLinks();

    /**
     * To be called after storing links.
     */
    void storeLinks();

    /**
     * Stores a link (before a start and an end).
     * @param start the start position
     * @param end the end position
     */
    void addLink(byte start, byte end);
}
