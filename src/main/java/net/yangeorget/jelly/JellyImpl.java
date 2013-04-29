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
    private char color;

    private JellyImpl(final int width, final int height, final char color) {
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public JellyImpl(final int width, final int height, final char color, final byte... positions) {
        this(width, height, color);
        this.positions = new byte[positions.length];
        System.arraycopy(positions, 0, this.positions, 0, positions.length);
        Arrays.sort(this.positions);
    }

    public JellyImpl(final Board board, final boolean[][] visited, final char color, final int i, final int j) {
        this(board.getWidth(), board.getHeight(), color);
        final int free = update(board, visited, 0, i, j);
        positions = new byte[free];
        System.arraycopy(BUFFER, 0, positions, 0, free);
        Arrays.sort(positions);
    }

    private int update(final Board board, final boolean[][] visited, int free, final int i, final int j) {
        if (!visited[i][j] && board.cellHasColor(i, j, color)) {
            color = board.cellIsFixed(i, j) ? board.get(i, j) : color;
            visited[i][j] = true;
            BUFFER[free++] = value(i, j);
            if (i > 0) {
                free = update(board, visited, free, i - 1, j);
            }
            if (i < board.getHeight() - 1) {
                free = update(board, visited, free, i + 1, j);
            }
            if (j > 0) {
                free = update(board, visited, free, i, j - 1);
            }
            if (j < board.getWidth() - 1) {
                free = update(board, visited, free, i, j + 1);
            }
        }
        return free;
    }

    @Override
    public boolean isFixed() {
        return BoardImpl.isFixed(color);
    }

    @Override
    public JellyImpl clone() {
        return new JellyImpl(width, height, color, positions);
    }

    @Override
    public String toString() {
        return "color=" + color + ";positions=" + Arrays.toString(positions);
    }

    @Override
    public boolean hMove(final int move) {
        if (isFixed()) {
            return false;
        }
        for (int i = positions.length; --i >= 0;) {
            if (!hMove(i, move)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean vMove(final int move) {
        if (isFixed()) {
            return false;
        }
        for (int i = positions.length; --i >= 0;) {
            if (!vMove(i, move)) {
                return false;
            }
        }
        return true;
    }

    static byte getI(final byte pos) {
        return (byte) (pos >> 4);
    }

    static byte getJ(final byte pos) {
        return (byte) (pos & 0xF);
    }

    static byte value(final int i, final int j) {
        return (byte) ((i << 4) + j);
    }

    private boolean hMove(final int index, final int move) {
        final byte position = positions[index];
        final int j = getJ(position) + move;
        if (j < 0 || j >= width) {
            return false;
        }
        positions[index] = value(getI(position), j);
        return true;
    }

    private boolean vMove(final int index, final int move) {
        final byte position = positions[index];
        final int i = getI(position) + move;
        if (i < 0 || i >= height) {
            return false;
        }
        positions[index] = value(i, getJ(position));
        return true;
    }

    @Override
    public boolean overlaps(final Jelly jelly) {
        final JellyImpl j = (JellyImpl) jelly;
        int index = 0;
        int jIndex = 0;
        while (true) {
            while (positions[index] < j.positions[jIndex]) {
                if (++index == positions.length) {
                    return false;
                }
            }
            if (positions[index] == j.positions[jIndex]) {
                return true;
            }
            while (positions[index] > j.positions[jIndex]) {
                if (++jIndex == j.positions.length) {
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
    public void updateBoard(final char[][] board) {
        for (final byte position : positions) {
            board[getI(position)][getJ(position)] = color;
        }
    }
}
