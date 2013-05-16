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
    public boolean moveLeft(final int j) {
        return moveLeft(jellies[j]);
    }

    boolean moveLeft(final Jelly jelly) {
        if (!jelly.mayMoveLeft()) {
            return false;
        }
        jelly.moveLeft();
        for (final Jelly j : jellies) {
            if (!jelly.equals(j) && jelly.overlaps(j) && !moveLeft(j)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean moveRight(final int j) {
        return moveRight(jellies[j]);
    }

    boolean moveRight(final Jelly jelly) {
        if (!jelly.mayMoveRight()) {
            return false;
        }
        jelly.moveRight();
        for (final Jelly j : jellies) {
            if (!jelly.equals(j) && jelly.overlaps(j) && !moveRight(j)) {
                return false;
            }
        }
        return true;
    }

    boolean moveDown(final Jelly jelly) {
        if (!jelly.mayMoveDown()) {
            return false;
        }
        jelly.moveDown();
        for (final Jelly j : jellies) {
            if (!jelly.equals(j) && jelly.overlaps(j) && !moveDown(j)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void gravity() { // TODO: use a cache to check if moves are possible
        for (boolean gravity = true; gravity;) {
            gravity = false;
            for (int j = 0; j < jellies.length; j++) {
                if (jellies[j].mayMoveDown()) {
                    final StateImpl clone = clone(); // TODO: avoid cloning when movedown (moveup instead)
                    final Jelly[] cloneJellies = clone.getJellies();
                    if (clone.moveDown(cloneJellies[j])) {
                        jellies = cloneJellies;
                        gravity = true;
                    }
                }
            }
        }
        board.clear();
        for (final Jelly jelly : jellies) {
            jelly.updateBoard(board);
        }
        jellies = board.getJellies();
    }

    @Override
    public String toString() {
        return "jellies=" + jellies;
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
