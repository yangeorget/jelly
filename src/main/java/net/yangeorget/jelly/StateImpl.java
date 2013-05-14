package net.yangeorget.jelly;

import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StateImpl
        implements State {
    private static final Logger LOG = LoggerFactory.getLogger(StateImpl.class);

    private Jelly[] jellies;
    private final Board board;

    public StateImpl(final Board board) {
        this.board = board;
        jellies = board.getJellies();
    }

    public StateImpl(final State state) {
        board = state.getBoard();
        final Jelly[] stateJellies = state.getJellies();
        final int size = stateJellies.length;
        jellies = new Jelly[size];
        for (int i = 0; i < size; i++) {
            jellies[i] = stateJellies[i].clone();
        }
    }

    @Override
    public StateImpl clone() {
        return new StateImpl(this);
    }

    @Override
    public Jelly[] getJellies() {
        return jellies;
    }

    @Override
    public String move(final Jelly jelly, final int move) {
        if (!hMove(jelly, move)) {
            return null;
        }
        gravity();
        final Board board = toBoard();
        jellies = board.getJellies();
        return board.toString();
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
        for (int j = 0; j < jellies.length; j++) {
            if (!jellies[j].isFixed()) {
                final StateImpl clone = clone();
                final Jelly[] cloneJellies = clone.getJellies();
                if (clone.gravity(cloneJellies[j])) {
                    jellies = cloneJellies;
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
        board.clear();
        for (final Jelly jelly : jellies) {
            jelly.updateBoard(board);
        }
        return board;
    }

    @Override
    public int getDistinctColorsNb() {
        final Set<Character> colors = new HashSet<>();
        for (final Jelly j : getJellies()) {
            colors.add(BoardImpl.toFloating(j.getColor()));
        }
        return colors.size();
    }

    @Override
    public Board getBoard() {
        return board;
    }
}
