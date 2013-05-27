package net.yangeorget.jelly;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StateImpl
        implements State {
    private static final Logger LOG = LoggerFactory.getLogger(StateImpl.class);
    private static final Jelly[] JELLIES_BUFFER = new Jelly[Board.MAX_HEIGHT * Board.MAX_WIDTH];
    private static final List<Jelly> MOVED_JELLIES = new LinkedList<>();

    private Jelly[] jellies;
    private final Board board;
    private String serialization;

    public StateImpl(final Board board) {
        this.board = board;
        computeSerializationAndJellies();
    }

    public StateImpl(final StateImpl state) {
        board = state.getBoard();
        serialization = state.getSerialization();
        final Jelly[] jellies = state.getJellies();
        final int size = jellies.length;
        this.jellies = new Jelly[size];
        for (int i = 0; i < size; i++) {
            this.jellies[i] = jellies[i].clone();
        }
    }


    @Override
    public void computeSerializationAndJellies() {
        serialization = board.serialize();
        int nb = 0;
        final char[][] matrix = board.getMatrix();
        final boolean[][] walls = board.getWalls();
        for (byte i = 0; i < board.getHeight(); i++) {
            for (byte j = 0; j < board.getWidth(); j++) {
                if (matrix[i][j] != Board.BLANK_CHAR && !walls[i][j]) {
                    JELLIES_BUFFER[nb++] = new JellyImpl(this, i, j);
                }
            }
        }
        jellies = new Jelly[nb];
        System.arraycopy(JELLIES_BUFFER, 0, jellies, 0, nb);
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
        computeSerializationAndJellies();
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
