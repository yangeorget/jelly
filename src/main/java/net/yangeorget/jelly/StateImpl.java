package net.yangeorget.jelly;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StateImpl
        implements State {
    private static final Logger LOG = LoggerFactory.getLogger(StateImpl.class);

    private List<Jelly> jellies;
    private final int width;
    private final int height;

    public StateImpl(final Board board) {
        this(board.getHeight(), board.getWidth(), board.getJellies(), false);
    }

    public StateImpl(final StateImpl state) {
        this(state.height, state.width, state.getJellies(), true);
    }

    private StateImpl(final int height, final int width, final List<Jelly> jellies, final boolean copy) {
        this.height = height;
        this.width = width;
        if (copy) {
            this.jellies = new ArrayList<>(jellies.size());
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
    public Jelly getJelly(final int index) {
        return jellies.get(index);
    }

    @Override
    public Board move(final Jelly jelly, final int move) {
        if (hMove(jelly, move)) {
            gravity();
            final Board board = toBoard();
            jellies = board.getJellies();
            return board;
        } else {
            return null;
        }
    }

    boolean hMove(final Jelly jelly, final int move) {
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
            if (!getJelly(j).isFixed()) { // TODO: optimize replace by cannot move
                final StateImpl state = clone();
                if (state.gravity(state.getJelly(j))) {
                    jellies = state.getJellies();
                    gravity = true;
                }
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
            colors.add(BoardImpl.getColor(j.getColor()));
        }
        return colors.size();
    }
}
