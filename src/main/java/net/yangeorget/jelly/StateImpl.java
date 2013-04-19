package net.yangeorget.jelly;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StateImpl
        implements State {
    private static final Logger LOG = LoggerFactory.getLogger(StateImpl.class);

    private List<Jelly> jellies;
    private final Frame frame;

    public StateImpl(final Board board) {
        frame = board;
        jellies = board.getJellies();
    }

    @Override
    public State clone() {
        return new StateImpl(toBoard());
    }

    @Override
    public List<Jelly> getJellies() {
        return jellies;
    }

    @Override
    public State move(final int j, final int move) {
        final State state = clone();
        if (state.slide(state.getJellies()
                             .get(j), move)) {
            state.gravity();
            jellies = state.toBoard()
                           .getJellies();
            return state;
        } else {
            return null;
        }
    }

    @Override
    public boolean slide(final Jelly jelly, final int move) {
        if (!jelly.hMove(move)) {
            return false;
        }
        for (final Jelly j : jellies) {
            if (!jelly.equals(j) && jelly.overlaps(j) && !slide(j, move)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void gravity() {
        boolean gravity = false;
        for (int j = 0; j < getJellies().size(); j++) {
            final State state = clone();
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

    @Override
    public boolean gravity(final Jelly jelly) {
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
        return "frame=" + frame + ";jellies=" + jellies;
    }

    @Override
    public Board toBoard() {
        final char[][] m = new char[frame.getHeight()][frame.getWidth()];
        for (int i = 0; i < frame.getHeight(); i++) {
            for (int j = 0; j < frame.getWidth(); j++) {
                m[i][j] = ' '; // TODO: optim
            }
        }
        for (final Jelly jelly : jellies) {
            for (final Position position : jelly.getPositions()) {
                m[position.getI()][position.getJ()] = jelly.getColor();
            }
        }
        return new BoardImpl(m);
    }

    @Override
    public int getDistinctColorsNb() {
        // TODO Auto-generated method stub
        return 0;
    }
}
