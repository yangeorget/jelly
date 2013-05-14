package net.yangeorget.jelly;

import java.util.Arrays;

/**
 * @author y.georget
 */
public class JellyImpl
        implements Jelly {
    private static byte[] BUFFER = new byte[Board.MAX_WIDTH * Board.MAX_HEIGHT];
    private byte[] positions;
    private final int width;
    private final int height;
    private final char color;
    private boolean isFixed;


    private JellyImpl(final int width, final int height, final char color, final boolean isFixed) {
        this.width = width;
        this.height = height;
        this.color = color;
        this.isFixed = isFixed;
    }

    public JellyImpl(final int width,
                     final int height,
                     final char color,
                     final boolean isFixed,
                     final byte... positions) {
        this(width, height, color, isFixed);
        final int size = positions.length;
        this.positions = new byte[size];
        System.arraycopy(positions, 0, this.positions, 0, size);
    }

    public JellyImpl(final Board board, final boolean[][] visited, final char color, final int i, final int j) {
        this(board.getWidth(), board.getHeight(), BoardImpl.toFloating(color), BoardImpl.isFixed(color));
        final int free = update(board, visited, 0, i, j);
        positions = new byte[free];
        System.arraycopy(BUFFER, 0, positions, 0, free);
        Arrays.sort(positions);
    }

    private int update(final Board board, final boolean[][] visited, int free, final int i, final int j) {
        final char c = board.get(i, j);
        if (!visited[i][j] && BoardImpl.toFloating(c) == color) {
            isFixed |= BoardImpl.isFixed(c);
            visited[i][j] = true;
            BUFFER[free++] = value(i, j);
            if (i > 0) {
                free = update(board, visited, free, i - 1, j);
            }
            if (i < height - 1) {
                free = update(board, visited, free, i + 1, j);
            }
            if (j > 0) {
                free = update(board, visited, free, i, j - 1);
            }
            if (j < width - 1) {
                free = update(board, visited, free, i, j + 1);
            }
        }
        return free;
    }

    @Override
    public boolean isFixed() {
        return isFixed;
    }

    @Override
    public JellyImpl clone() {
        return new JellyImpl(width, height, color, isFixed, positions);
    }

    @Override
    public String toString() {
        return "color=" + color + ";positions=" + Arrays.toString(positions);
    }

    @Override
    public boolean moveLeft() {
        if (isFixed) {
            return false;
        }
        for (int index = 0; index < positions.length; index++) {
            if (getJ(positions[index]) == 0) {
                return false;
            }
            positions[index]--;
        }
        return true;
    }

    @Override
    public boolean moveRight() {
        if (isFixed) {
            return false;
        }
        for (int index = positions.length; --index >= 0;) {
            if (getJ(positions[index]) == width - 1) {
                return false;
            }
            positions[index]++;
        }
        return true;
    }

    @Override
    public boolean moveDown() {
        if (isFixed || getI(positions[positions.length - 1]) == height - 1) {
            return false;
        }
        for (int index = positions.length; --index >= 0;) {
            positions[index] += 16;
        }
        return true;
    }

    final static byte getI(final byte pos) {
        return (byte) (pos >> 4);
    }

    final static byte getJ(final byte pos) {
        return (byte) (pos & 0xF);
    }

    final static byte value(final int i, final int j) {
        return (byte) ((i << 4) + j);
    }


    @Override
    public boolean overlaps(final Jelly jelly) {
        final JellyImpl j = (JellyImpl) jelly;
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
    public char getColor() {
        return color;
    }

    @Override
    public void updateBoard(final Board board) {
        for (final byte position : positions) {
            board.set(getI(position), getJ(position), color);
        }
    }
}
