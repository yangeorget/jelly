package net.yangeorget.jelly;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author y.georget
 */
public class JellyImpl
        implements Jelly {
    private final List<Jelly.Position> positions;

    private final int width;
    private final int height;
    private char color;

    private JellyImpl(final int width, final int height, final char color) {
        this.width = width;
        this.height = height;
        this.color = color;
        positions = new LinkedList<>();
    }

    public JellyImpl(final int width, final int height, final char color, final Collection<Jelly.Position> positions) {
        this(width, height, color);
        for (final Jelly.Position position : positions) {
            this.positions.add(new Position(position));
        }
        Collections.sort(this.positions);
    }

    public JellyImpl(final Board board, final boolean[][] visited, final char color, final int i, final int j) {
        this(board.getWidth(), board.getHeight(), color);
        update(board, visited, i, j);
        Collections.sort(positions);
    }

    private void update(final Board board, final boolean[][] visited, final int i, final int j) {
        if (!visited[i][j] && board.cellHasColor(i, j, color)) {
            this.color = board.cellIsFixed(i, j) ? board.get(i, j) : color;
            visited[i][j] = true;
            positions.add(new Position(i, j));
            if (i > 0) {
                update(board, visited, i - 1, j);
            }
            if (i < board.getHeight() - 1) {
                update(board, visited, i + 1, j);
            }
            if (j > 0) {
                update(board, visited, i, j - 1);
            }
            if (j < board.getWidth() - 1) {
                update(board, visited, i, j + 1);
            }
        }
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
        return "color=" + color + ";positions=" + positions.toString();
    }

    @Override
    public boolean hMove(final int move) {
        if (isFixed()) {
            return false;
        }
        for (final Jelly.Position position : positions) {
            if (!position.hMove(move, width)) {
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
        for (final Jelly.Position position : positions) {
            if (!position.vMove(move, height)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean overlaps(final Jelly jelly) {
        final JellyImpl j = (JellyImpl) jelly;
        int index = 0;
        int jIndex = 0;
        while (true) {
            while (positions.get(index)
                            .compareTo(j.positions.get(jIndex)) < 0) {
                if (++index == positions.size()) {
                    return false;
                }
            }
            if (positions.get(index)
                         .compareTo(j.positions.get(jIndex)) == 0) {
                return true;
            }
            while (positions.get(index)
                            .compareTo(j.positions.get(jIndex)) > 0) {
                if (++jIndex == j.positions.size()) {
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
        for (final Jelly.Position position : positions) {
            board[position.getI()][position.getJ()] = color;
        }
    }
}
