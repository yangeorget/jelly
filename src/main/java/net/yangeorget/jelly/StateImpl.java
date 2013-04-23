package net.yangeorget.jelly;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StateImpl
        implements State {
    private static final Logger LOG = LoggerFactory.getLogger(StateImpl.class);

    private List<Jelly> jellies;
    private final Frame frame;

    public StateImpl(final Board board) {
        this(board, board.getJellies(), false);
    }

    public StateImpl(final State state) {
        this(state.getFrame(), state.getJellies(), true);
    }

    private StateImpl(final Frame frame, final List<Jelly> jellies, final boolean copy) {
        this.frame = frame;
        if (copy) {
            this.jellies = new LinkedList<>();
            for (final Jelly jelly : jellies) {
                this.jellies.add(jelly.clone());
            }
        } else {
            this.jellies = jellies;
        }
    }

    @Override
    public StateImpl clone() {
        return new StateImpl(this);
    }

    @Override
    public List<Jelly> getJellies() {
        return jellies;
    }

    @Override
    public State move(final int j, final int move) {
        final StateImpl state = new StateImpl(toBoard());
        return state.move(state.getJellies()
                               .get(j), move);
    }

    State move(final Jelly jelly, final int move) {
        if (hMove(jelly, move)) {
            gravity();
            jellies = toBoard().getJellies();
            return this;
        } else {
            return null;
        }
    }

    boolean hMove(final Jelly jelly, final int move) {
        LOG.debug("hMove:" + jelly + " " + move);
        if (!jelly.hMove(move)) {
            return false;
        }
        for (final Jelly j : jellies) {
            if (!jelly.equals(j) && jelly.overlaps(j) && !hMove(j, move)) {
                return false;
            }
        }
        return true;
    }

    void gravity() {
        boolean gravity = false;
        for (int j = 0; j < getJellies().size(); j++) {
            final StateImpl state = clone();
            if (state.gravity(state.getJellies()
                                   .get(j))) {
                jellies = state.getJellies();
                gravity = true;
            }
        }
        if (gravity) {
            gravity();
        }
    }

    boolean gravity(final Jelly jelly) {
        if (!jelly.vMove(1)) {
            return false;
        }
        for (final Jelly j : jellies) {
            if (!jelly.equals(j) && jelly.overlaps(j) && !gravity(j)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "jellies=" + jellies;
    }

    @Override
    public Board toBoard() {
        final int height = frame.getHeight();
        final int width = frame.getWidth();
        final char[][] m = new char[height][width];
        for (int i = 0; i < height; i++) {
            Arrays.fill(m[i], ' ');
        }
        for (final Jelly jelly : jellies) {
            jelly.updateBoard(m);
        }
        return new BoardImpl(m);
    }

    @Override
    public int getDistinctColorsNb() {
        final Set<Character> colors = new HashSet<>();
        for (final Jelly j : getJellies()) {
            colors.add(Character.toUpperCase(j.getColor()));
        }
        return colors.size();
    }

    @Override
    public Frame getFrame() {
        return frame;
    }
}
