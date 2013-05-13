package net.yangeorget.jelly;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StateImpl
        implements State {
    private static final Logger LOG = LoggerFactory.getLogger(StateImpl.class);

    private List<Jelly> jellies;
    private final Board board;

    public StateImpl(final Board board) {
        this.board = board;
        jellies = board.getJellies();
    }

    public StateImpl(final State state) {
        board = state.getBoard();
        jellies = new ArrayList<>(state.getJellies()
                                       .size());
        for (final Jelly jelly : state.getJellies()) {
            jellies.add(jelly.clone());
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
        for (int j = 0; j < getJellies().size(); j++) {
            if (!getJelly(j).isFixed()) {
                final StateImpl clone = clone();
                if (clone.gravity(clone.getJelly(j))) {
                    jellies = clone.getJellies();
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
