package net.yangeorget.jelly;

import java.util.Arrays;

/**
 * @author y.georget
 */
public class JellyImpl
        implements Jelly {
    private static final byte[] BUFFER = new byte[Board.MAX_WIDTH * Board.MAX_HEIGHT];
    private byte[] positions;
    private final byte heightWidth;
    private final char color;
    private boolean isFixed;
    // bounding box
    private byte leftMin;
    private byte rightMax;
    private byte topMin;
    private byte bottomMax;

    private JellyImpl(final byte heightWidth, final char color, final boolean isFixed) {
        this.heightWidth = heightWidth;
        this.color = color;
        this.isFixed = isFixed;
    }

    public JellyImpl(final byte heightWidth,
                     final char color,
                     final boolean isFixed,
                     final byte leftMin,
                     final byte rightMax,
                     final byte topMin,
                     final byte bottomMax,
                     final byte... positions) {
        this(heightWidth, color, isFixed);
        this.leftMin = leftMin;
        this.rightMax = rightMax;
        this.leftMin = leftMin;
        this.bottomMax = bottomMax;
        final int size = positions.length;
        this.positions = new byte[size];
        System.arraycopy(positions, 0, this.positions, 0, size);
    }

    public JellyImpl(final Board board, final char color, final int i, final int j) {
        this(value(board.getHeight(), board.getWidth()), BoardImpl.toFloating(color), BoardImpl.isFixed(color));
        this.leftMin = (byte) getWidth();
        this.rightMax = -1;
        this.leftMin = (byte) getHeight();
        this.bottomMax = -1;
        final int size = update(board.getMatrix(), 0, i, j);
        this.positions = new byte[size];
        System.arraycopy(BUFFER, 0, this.positions, 0, size);
        Arrays.sort(this.positions);
    }

    private int update(final char[][] matrix, int free, final int i, final int j) {
        final char c = matrix[i][j];
        if (BoardImpl.toFloating(c) == color) {
            isFixed |= BoardImpl.isFixed(c);
            matrix[i][j] = Board.BLANK_CHAR;
            BUFFER[free++] = value(i, j);
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
        return new JellyImpl(heightWidth, color, isFixed, leftMin, rightMax, topMin, bottomMax, positions);
    }

    @Override
    public String toString() {
        return "color=" + color + ";positions=" + Arrays.toString(positions);
    }

    @Override
    public boolean mayMoveLeft() {
        return !isFixed && leftMin != 0;
    }

    @Override
    public void moveLeft() {
        for (int index = positions.length; --index >= 0;) {
            positions[index]--;
        }
        leftMin--;
        rightMax--;
    }

    @Override
    public boolean mayMoveRight() {
        return !isFixed && rightMax != getWidth() - 1;
    }

    @Override
    public void moveRight() {
        for (int index = positions.length; --index >= 0;) {
            positions[index]++;
        }
        leftMin++;
        rightMax++;
    }

    @Override
    public boolean mayMoveDown() {
        return !isFixed && bottomMax != getHeight() - 1;
    }

    @Override
    public void moveDown() {
        for (int index = positions.length; --index >= 0;) {
            positions[index] += 16;
        }
        topMin++;
        bottomMax++;
    }

    @Override
    public void moveUp() {
        for (int index = positions.length; --index >= 0;) {
            positions[index] -= 16;
        }
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
        return (pos >> 4) & 0xF;
    }

    final static int getJ(final byte pos) {
        return pos & 0xF;
    }

    final static byte value(final int i, final int j) {
        return (byte) ((i << 4) | j);
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
    public void updateBoard(final Board board) {
        final char[][] matrix = board.getMatrix();
        for (final byte position : positions) {
            matrix[getI(position)][getJ(position)] = color;
        }
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
