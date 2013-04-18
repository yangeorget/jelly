package net.yangeorget.jelly;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

public class JellyImpl
        implements Jelly {
    private Set<Position> positions;
    private final Frame frame;

    public JellyImpl(final Frame frame) {
        this.frame = frame;
        positions = new TreeSet<>();
    }

    public JellyImpl(final Frame frame, final Collection<Position> col) {
        this(frame);
        add(col);
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
    public void restore(final Jelly j) {
        positions = j.getPositions();

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
    public boolean hMove(final int move) {
        for (final Position position : positions) {
            if (!position.hMove(move, frame.getWidth())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean vMove(final int move) {
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
    public boolean adjacentTo(final Jelly je) {
        Jelly j = clone();
        if (j.hMove(-1)) {
            if (j.overlaps(je)) {
                return true;
            }
        }
        j = clone();
        if (j.hMove(1)) {
            if (j.overlaps(je)) {
                return true;
            }
        }
        j = clone();
        if (j.vMove(-1)) {
            if (j.overlaps(je)) {
                return true;
            }
        }
        j = clone();
        if (j.vMove(1)) {
            if (j.overlaps(je)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void merge(final Jelly je) {
        add(je.getPositions());
    }
}
