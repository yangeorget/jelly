package net.yangeorget.jelly;

import java.util.Arrays;

/**
 * @author y.georget
 */
public class JellyImpl
        implements Jelly {
    private static final byte[] POSITIONS_BUFFER = new byte[Board.MAX_WIDTH * Board.MAX_HEIGHT];
    private static final char[] COLOR_BUFFER = new char[Board.MAX_WIDTH * Board.MAX_HEIGHT];
    private static final int[] END_BUFFER = new int[Board.MAX_WIDTH * Board.MAX_HEIGHT];

    private final byte[] positions;
    private final char[] color;
    private final int[] end;

    private final byte heightWidth;
    private boolean isFixed;
    // bounding box
    private byte leftMin;
    private byte rightMax;
    private byte topMin;
    private byte bottomMax;

    JellyImpl(final byte heightWidth,
              final boolean isFixed,
              final byte leftMin,
              final byte rightMax,
              final byte topMin,
              final byte bottomMax,
              final char color,
              final byte... positions) {
        this(heightWidth,
             isFixed,
             leftMin,
             rightMax,
             topMin,
             bottomMax,
             new int[] { positions.length },
             new char[] { color },
             positions);
    }

    public JellyImpl(final byte heightWidth,
                     final boolean isFixed,
                     final byte leftMin,
                     final byte rightMax,
                     final byte topMin,
                     final byte bottomMax,
                     final int[] end,
                     final char[] color,
                     final byte[] positions) {
        this.heightWidth = heightWidth;
        this.isFixed = isFixed;
        this.leftMin = leftMin;
        this.rightMax = rightMax;
        this.topMin = topMin;
        this.bottomMax = bottomMax;
        this.positions = Arrays.copyOf(positions, positions.length);
        this.color = Arrays.copyOf(color, color.length);
        this.end = Arrays.copyOf(end, end.length);
    }

    public JellyImpl(final State state, final int i, final int j) {
        final Board board = state.getBoard();
        heightWidth = value(board.getHeight(), board.getWidth());
        topMin = bottomMax = (byte) i;
        leftMin = rightMax = (byte) j;
        final char[][] matrix = board.getMatrix();
        COLOR_BUFFER[0] = BoardImpl.toFloating(matrix[i][j]);
        END_BUFFER[0] = 0;
        final int index = update(matrix, 0, i, j);
        color = Arrays.copyOf(COLOR_BUFFER, index + 1);
        end = Arrays.copyOf(END_BUFFER, index + 1);
        positions = Arrays.copyOf(POSITIONS_BUFFER, getEnd(index));
        Arrays.sort(positions);
    }

    private int update(final char[][] matrix, int free, final int i, final int j) {
        // TODO: update links
        final char c = matrix[i][j];
        if (BoardImpl.toFloating(c) == COLOR_BUFFER[free]) {
            isFixed |= BoardImpl.isFixed(c);
            matrix[i][j] = Board.BLANK_CHAR;
            POSITIONS_BUFFER[END_BUFFER[free]] = value(i, j);
            END_BUFFER[free]++;
            if (j < leftMin) {
                leftMin = (byte) j;
            }
            if (rightMax < j) {
                rightMax = (byte) j;
            }
            if (i < topMin) {
                topMin = (byte) i;
            }
            if (bottomMax < i) {
                bottomMax = (byte) i;
            }
            if (i > 0) {
                free = update(matrix, free, i - 1, j);
            }
            final int i1 = i + 1;
            if (i1 < getHeight()) {
                free = update(matrix, free, i1, j);
            }
            if (j > 0) {
                free = update(matrix, free, i, j - 1);
            }
            final int j1 = j + 1;
            if (j1 < getWidth()) {
                free = update(matrix, free, i, j1);
            }
        }
        return free;
    }

    @Override
    public JellyImpl clone() {
        return new JellyImpl(heightWidth, isFixed, leftMin, rightMax, topMin, bottomMax, end, color, positions);
    }

    @Override
    public String toString() {
        return ";positions="
               + Arrays.toString(positions)
               + ";color="
               + Arrays.toString(color)
               + ";end="
               + Arrays.toString(end);
    }

    private void move(final byte vec) {
        // TODO : update links on state
        for (int index = positions.length; --index >= 0;) {
            positions[index] += vec;
        }
    }

    @Override
    public boolean mayMoveLeft() {
        return !isFixed && leftMin != 0;
    }

    @Override
    public void moveLeft() {
        move(Board.LEFT);
        leftMin--;
        rightMax--;
    }

    @Override
    public boolean mayMoveRight() {
        return !isFixed && rightMax != getWidth() - 1;
    }

    @Override
    public void moveRight() {
        move(Board.RIGHT);
        leftMin++;
        rightMax++;
    }

    @Override
    public boolean mayMoveDown() {
        return !isFixed && bottomMax != getHeight() - 1;
    }

    @Override
    public void moveDown() {
        move(Board.DOWN);
        topMin++;
        bottomMax++;
    }

    @Override
    public void moveUp() {
        move(Board.UP);
        topMin--;
        bottomMax--;
    }

    int getWidth() {
        return getJ(heightWidth);
    }

    int getHeight() {
        return getI(heightWidth);
    }

    final static int getI(final byte pos) {
        return (pos >> Board.MAX_COORDINATE_LOG2) & Board.COORDINATE_MASK;
    }

    final static int getJ(final byte pos) {
        return pos & Board.COORDINATE_MASK;
    }

    final static byte value(final int i, final int j) {
        return (byte) ((i << Board.MAX_COORDINATE_LOG2) | j);
    }

    @Override
    public boolean overlaps(final Jelly jelly) {
        final JellyImpl j = (JellyImpl) jelly;
        if (j.getRightMax() < leftMin
            || rightMax < j.getLeftMin()
            || j.getBottomMax() < topMin
            || bottomMax < j.getTopMin()) {
            return false;
        }
        final byte[] jPositions = j.positions;
        final int size = positions.length;
        final int jSize = jPositions.length;
        int index = 0;
        int jIndex = 0;
        while (true) {
            final byte jPosition = jPositions[jIndex];
            while (positions[index] < jPosition) {
                if (++index == size) {
                    return false;
                }
            }
            final byte position = positions[index];
            if (position == jPosition) {
                return true;
            }
            while (position > jPositions[jIndex]) {
                if (++jIndex == jSize) {
                    return false;
                }
            }
        }
    }

    @Override
    public boolean overlaps(final boolean[][] walls) {
        for (final byte position : positions) {
            if (walls[getI(position)][getJ(position)]) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void updateBoard(final Board board) {
        final char[][] matrix = board.getMatrix();
        for (int i = 0; i < end.length; i++) {
            char c = color[i];
            c = isFixed ? BoardImpl.toFixed(c) : c;
            for (int j = getStart(i); j < getEnd(i); j++) {
                final byte position = positions[j];
                matrix[getI(position)][getJ(position)] = c;
            }
        }
    }

    private int getStart(final int index) {
        return index == 0 ? 0 : end[index - 1];
    }

    private int getEnd(final int index) {
        return end[index];
    }

    byte getLeftMin() {
        return leftMin;
    }

    byte getTopMin() {
        return topMin;
    }

    byte getRightMax() {
        return rightMax;
    }

    byte getBottomMax() {
        return bottomMax;
    }
}
