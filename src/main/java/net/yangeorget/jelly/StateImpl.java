package net.yangeorget.jelly;

import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StateImpl
        implements State {
    private static final Logger LOG = LoggerFactory.getLogger(StateImpl.class);
    private static final Jelly[] JELLIES_BUFFER = new Jelly[Board.MAX_SIZE];
    private static int jelliesIndex;

    private final Board board;
    private String serialization;
    private Jelly[] jellies;
    private State parent;

    public StateImpl(final Board board) {
        this.board = board;
        updateFromBoard();
    }

    /**
     * Use for cloning.
     * @param state the parent state
     */
    public StateImpl(final StateImpl state) {
        board = state.getBoard();
        parent = state;
        serialization = state.getSerialization();
        final Jelly[] jellies = state.getJellies();
        final int size = jellies.length;
        this.jellies = new Jelly[size];
        for (int i = 0; i < size; i++) {
            this.jellies[i] = jellies[i].clone(this);
        }
    }

    @Override
    public final void updateBoard() {
        board.clearLinks();
        for (final Jelly jelly : jellies) {
            jelly.updateBoard();
        }
        board.updateLinks();
    }

    @Override
    public final void updateFromBoard() {
        serialization = board.serialize();
        jelliesIndex = 0;
        final char[][] matrix = board.getMatrix();
        final boolean[][] walls = board.getWalls();
        final int height = board.getHeight();
        final int width = board.getWidth();
        for (byte i = 0; i < height; i++) {
            for (byte j = 0; j < width; j++) {
                if (matrix[i][j] != Board.BLANK_CHAR && !walls[i][j]) {
                    JELLIES_BUFFER[jelliesIndex++] = new JellyImpl(this, i, j);
                }
            }
        }
        // faster than jellies = Arrays.copyOf(JELLIES_BUFFER, nb);
        jellies = new Jelly[jelliesIndex];
        System.arraycopy(JELLIES_BUFFER, 0, jellies, 0, jelliesIndex);
    }

    @Override
    public final StateImpl clone() {
        return new StateImpl(this);
    }

    @Override
    public final Jelly[] getJellies() {
        return jellies;
    }

    @Override
    public final boolean moveLeft(final int j) {
        return moveLeft(jellies[j]);
    }

    final boolean moveLeft(final Jelly jelly) {
        if (jelly.mayMoveLeft()) {
            jelly.moveLeft();
            if (jelly.overlapsWalls()) {
                return false;
            }
            for (final Jelly j : jellies) {
                if (!jelly.equals(j) && jelly.overlaps(j) && !moveLeft(j)) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public final boolean moveRight(final int j) {
        return moveRight(jellies[j]);
    }

    final boolean moveRight(final Jelly jelly) {
        if (jelly.mayMoveRight()) {
            jelly.moveRight();
            if (jelly.overlapsWalls()) {
                return false;
            }
            for (final Jelly j : jellies) {
                if (!jelly.equals(j) && jelly.overlaps(j) && !moveRight(j)) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    final boolean moveDown(final Jelly jelly) {
        if (jelly.mayMoveDown()) {
            jelly.moveDown();
            JELLIES_BUFFER[jelliesIndex++] = jelly;
            if (jelly.overlapsWalls()) {
                return false;
            }
            for (final Jelly j : jellies) {
                if (!jelly.equals(j) && jelly.overlaps(j) && !moveDown(j)) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public final void gravity() { // TODO: use a cache to check if moves are possible
        for (boolean gravityAgain = true; gravityAgain;) {
            gravityAgain = false;
            for (final Jelly jelly : jellies) {
                if (jelly.mayMoveDown()) {
                    jelliesIndex = 0;
                    if (moveDown(jelly)) {
                        gravityAgain = true;
                    } else {
                        while (--jelliesIndex >= 0) {
                            JELLIES_BUFFER[jelliesIndex].moveUp();
                        }
                    }
                }
            }
        }
        updateBoard();
        updateFromBoard();
    }

    @Override
    public final String toString() {
        return "board="
               + board
               + ";walls="
               + board.getWalls()
               + ";jellies="
               + (jellies == null ? "null" : Arrays.asList(jellies));
    }

    @Override
    public final Board getBoard() {
        return board;
    }

    @Override
    public final String getSerialization() {
        return serialization;
    }

    @Override
    public final boolean isSolved() {
        int jellyNb = 0;
        for (final Jelly jelly : jellies) {
            jellyNb += jelly.getSegmentNb();
        }
        return jellyNb == board.getJellyColorNb();
    }

    @Override
    public final void explain(final int step) {
        updateBoard();
        LOG.debug("=== STEP " + step + " ===\n" + board.toString());
        updateFromBoard();
        if (parent != null) {
            parent.explain(step + 1);
        }
    }
}
