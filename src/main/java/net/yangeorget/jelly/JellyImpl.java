package net.yangeorget.jelly;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

public class JellyImpl
        implements Jelly {
    private Set<Position> positions;

    public JellyImpl() {
        positions = new TreeSet<>();
    }

    public JellyImpl(final Collection<Position> col) {
        positions = clone(col);
    }

    @Override
    public Set<Position> getPositions() {
        return positions;
    }

    @Override
    public Jelly clone() {
        return new JellyImpl(positions);
    }

    @Override
    public void restore(final Jelly j) {
        positions = j.getPositions();

    }

    private static Set<Position> clone(final Collection<Position> col) {
        final Set<Position> clonedCol = new TreeSet<>();
        for (final Position position : col) {
            clonedCol.add(new Position(position));
        }
        return clonedCol;
    }

    @Override
    public void store(final int i, final int j) {
        positions.add(new Position(i, j));
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
    public boolean moveHorizontally(final int move, final int width) {
        for (final Position position : positions) {
            if (!position.moveHorizontally(move, width)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean moveDown(final int height) {
        for (final Position position : positions) {
            if (!position.moveDown(height)) {
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
    public void updateBoard(final char[][] board, final Character color) {
        for (final Position position : positions) {
            board[position.getI()][position.getJ()] = color;
        }
    }
}
