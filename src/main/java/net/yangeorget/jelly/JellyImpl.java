package net.yangeorget.jelly;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author y.georget
 */
public class JellyImpl
        implements Jelly {
    private final Set<Position> positions; // TODO: use list
    private final Frame frame;
    private char color;

    public JellyImpl(final Frame frame) {
        this.frame = frame;
        positions = new TreeSet<>();
    }

    public JellyImpl(final Frame frame, final Collection<Position> col) {
        this(frame);
        add(col);
    }

    public JellyImpl(final Board board, final boolean[][] visited, final char color, final int i, final int j) {
        this(board);
        this.color = color;
        update(board, visited, i, j);
    }

    private void update(final Board board, final boolean[][] visited, final int i, final int j) {
        final char c = board.get(i, j);
        if (!visited[i][j] && Character.toUpperCase(color) == Character.toUpperCase(c)) {
            this.color = Character.isLowerCase(c) ? Character.toLowerCase(c) : Character.toUpperCase(c);
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
        return Character.isLowerCase(color);
    }

    private void add(final Collection<Position> col) {
        for (final Position position : col) {
            positions.add(new Position(position));
        }
    }

    @Override
    public Set<Position> getPositions() {
        return positions;
    }

    @Override
    public Jelly clone() {
        return new JellyImpl(frame, positions);
    }

    @Override
    public String toString() {
        return positions.toString();
    }

    @Override
    public int size() {
        return positions.size();
    }

    @Override
    public boolean contains(final Position p) {
        return positions.contains(p);
    }

    @Override
    public boolean hMove(final int move) {
        if (isFixed()) {
            return false;
        }
        for (final Position position : positions) {
            if (!position.hMove(move, frame.getWidth())) {
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
        for (final Position position : positions) {
            if (!position.vMove(move, frame.getHeight())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean overlaps(final Jelly j) {
        if (size() > j.size()) {
            return j.overlaps(this);
        }
        for (final Position p : positions) {
            if (j.contains(p)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public char getColor() {
        return color;
    }
}
