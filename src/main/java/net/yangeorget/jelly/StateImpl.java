package net.yangeorget.jelly;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StateImpl
        implements State {
    private static final Logger LOG = LoggerFactory.getLogger(StateImpl.class);

    private Jelly[] jellies;
    private final Board board;
    private static final List<Jelly> MOVED_JELLIES = new LinkedList<>();
    private String serialization;

    public StateImpl(final Board board) {
        this.board = board;
        serialization = board.serialize();
        jellies = board.extractJellies();
    }

    public StateImpl(final State state) {
        board = state.getBoard();
        this.serialization = state.getSerialization();
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
        if (jelly.overlaps(board.getWalls())) {
            return false;
        }
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
        if (jelly.overlaps(board.getWalls())) {
            return false;
        }
        for (final Jelly j : jellies) {
            if (!jelly.equals(j) && jelly.overlaps(j) && !moveRight(j)) {
                return false;
            }
        }
        return true;
    }

    boolean moveDown(final Jelly jelly, final List<Jelly> movedJellies) {
        if (!jelly.mayMoveDown()) {
            return false;
        }
        jelly.moveDown();
        movedJellies.add(jelly);
        if (jelly.overlaps(board.getWalls())) {
            return false;
        }
        for (final Jelly j : jellies) {
            if (!jelly.equals(j) && jelly.overlaps(j) && !moveDown(j, movedJellies)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void gravity() { // TODO: use a cache to check if moves are possible
        for (boolean gravityAgain = true; gravityAgain;) {
            gravityAgain = false;
            for (final Jelly jelly : jellies) {
                if (jelly.mayMoveDown()) {
                    MOVED_JELLIES.clear();
                    if (moveDown(jelly, MOVED_JELLIES)) {
                        gravityAgain = true;
                    } else {
                        for (final Jelly je : MOVED_JELLIES) {
                            je.moveUp();
                        }
                    }
                }
            }
        }
        board.apply(jellies);
        serialization = board.serialize();
        jellies = board.extractJellies();
    }

    @Override
    public String toString() {
        return "board=" + board + ";walls=" + board.getWalls() + ";jellies=" + Arrays.asList(jellies);
    }


    @Override
    public Board getBoard() {
        return board;
    }

    @Override
    public String getSerialization() {
        return serialization;
    }

    @Override
    public boolean isSolved() {
        return getJellies().length == board.getJellyColorNb();
    }
}
