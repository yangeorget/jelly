package net.yangeorget.jelly;

import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An implementation of a state.
 * @author y.georget
 */
public class StateImpl
        implements State {
    private static final Logger LOG = LoggerFactory.getLogger(StateImpl.class);
    // private static final Serializer SERIALIZER = new SerializerTrimImpl();
    private static final Serializer SERIALIZER = new SerializerCountImpl();
    private static final Jelly[] JELLY_BUF = new Jelly[Board.MAX_SIZE];
    private static final int[] EP_INDEX_BUF = new int[Board.MAX_SIZE];
    private static int jellyIndex;
    private static int epIndexIndex;

    private final Board board;
    private StringBuilder serialization;
    private Jelly[] jellies;
    private State parent;
    private final boolean[] emerged;

    /**
     * Constructs the state from a board.
     * @param board a board
     */
    public StateImpl(final Board board) {
        this.board = board;
        emerged = new boolean[board.getEmergingPositionNb()];
        updateFromBoard();
    }

    /**
     * Use for cloning.
     * @param state the parent state
     */
    public StateImpl(final StateImpl state) {
        board = state.getBoard();
        parent = state;
        // serialization is not copied
        final Jelly[] jellies = state.getJellies();
        final int size = jellies.length;
        this.jellies = new Jelly[size];
        for (int i = 0; i < size; i++) {
            this.jellies[i] = jellies[i].clone();
        }
        this.emerged = Arrays.copyOf(state.emerged, state.emerged.length);
    }

    @Override
    public final void updateBoard() {
        board.clearLinks();
        for (final Jelly jelly : jellies) {
            jelly.updateBoard();
        }
        board.storeLinks();
    }

    @Override
    public final void updateFromBoard() {
        serialization = SERIALIZER.serialize(this);
        final int height = board.getHeight();
        final int width = board.getWidth();
        jellyIndex = 0;
        for (byte i = 0; i < height; i++) {
            for (byte j = 0; j < width; j++) {
                if (board.isColored(i, j)) {
                    JELLY_BUF[jellyIndex++] = new JellyImpl(board, i, j);
                }
            }
        }
        // faster than jellies = Arrays.copyOf(JELLIES_BUF, nb);
        jellies = new Jelly[jellyIndex];
        System.arraycopy(JELLY_BUF, 0, jellies, 0, jellyIndex);
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
        jellyIndex = 0;
        return moveLeft(jellies[j]);
    }

    final boolean moveLeft(final Jelly jelly) {
        if (jelly.mayMoveLeft()) {
            jelly.moveLeft();
            JELLY_BUF[jellyIndex++] = jelly;
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
        jellyIndex = 0;
        return moveRight(jellies[j]);
    }

    final boolean moveRight(final Jelly jelly) {
        if (jelly.mayMoveRight()) {
            jelly.moveRight();
            JELLY_BUF[jellyIndex++] = jelly;
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

    final boolean moveDown(final int j) {
        jellyIndex = 0;
        return moveDown(jellies[j]);
    }

    final boolean moveDown(final Jelly jelly) {
        if (jelly.mayMoveDown()) {
            jelly.moveDown();
            JELLY_BUF[jellyIndex++] = jelly;
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

    final boolean moveUp(final int j) {
        jellyIndex = 0;
        return moveUp(jellies[j]);
    }

    final boolean moveUp(final Jelly jelly) {
        if (jelly.mayMoveUp()) {
            jelly.moveUp();
            JELLY_BUF[jellyIndex++] = jelly;
            if (jelly.overlapsWalls()) {
                return false;
            }
            for (final Jelly j : jellies) {
                if (!jelly.equals(j) && jelly.overlaps(j) && !moveUp(j)) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public final void process() {
        moveDown();
        updateBoard();
        updateFromBoard();
        if (getNotEmergedNb() != 0 && moveUp()) {
            updateBoard();
            updateFromBoard();
        }
    }

    /**
     * Applies gravity.
     */
    final void moveDown() {
        final int size = jellies.length;
        final boolean[] blocked = new boolean[size];
        for (boolean movedDown = true; movedDown;) {
            movedDown = false;
            for (int i = size; --i >= 0;) {
                if (!blocked[i]) {
                    if (moveDown(i)) {
                        movedDown = true;
                    } else {
                        undoMoveDown();
                        blocked[i] = true;
                    }
                }
            }
        }
    }

    /**
     * Give a chance to emerging jellies.
     * @return a boolean indicating if jellies have emerged
     */
    final boolean moveUp() {
        // TODO: do we need more than one structure to store the candidate
        boolean someEmerged = false;
        for (int j = 0; j < jellies.length; j++) {
            epIndexIndex = 0;
            computeEmergingCandidates(jellies[j]);
            if (epIndexIndex > 0) {
                if (moveUp(j)) {
                    someEmerged = true;
                    while (--epIndexIndex >= 0) {
                        // let the candidate emerge from the wall
                        final int epIndex = EP_INDEX_BUF[epIndexIndex];
                        board.setColor((byte) (board.getEmergingPosition(epIndex) + Board.UP),
                                       board.getEmergingColor(epIndex));
                        emerged[epIndex] = true;
                        // TODO: we want to do the same with the jellies
                    }
                } else {
                    undoMoveUp();
                }
            }
        }
        return someEmerged;
    }

    /**
     * Computes the candidates for emerging.
     */
    final private void computeEmergingCandidates(final Jelly jelly) {
        final int segmentNb = jelly.getSegmentNb();
        final byte[] positions = jelly.getPositions();
        for (int segmentIndex = 0; segmentIndex < segmentNb; segmentIndex++) {
            final char segmentColor = jelly.getColor(segmentIndex);
            final int start = jelly.getStart(segmentIndex);
            final int end = jelly.getEnd(segmentIndex);
            for (int i = start; i < end; i++) {
                final byte ep = (byte) (positions[i] + Board.DOWN);
                // let's compute the emerging candidate from the walls
                int epIndex = board.getEmergingIndex(ep);
                if (epIndex >= 0
                    && !emerged[epIndex]
                    && BoardImpl.toFloating(board.getEmergingColor(epIndex)) == segmentColor) {
                    EP_INDEX_BUF[epIndexIndex++] = epIndex;
                }
                // let's compute the emerging candidate from the jellies
                for (final Jelly j : jellies) {
                    if (j.getEmergingPositionNb() > 0) { // optimisation
                        epIndex = j.getEmergingIndex(ep);
                        if (epIndex >= 0 && BoardImpl.toFloating(j.getEmergingColor(epIndex)) == segmentColor) {
                            // TODO: are we sure that we want to store this here?
                            EP_INDEX_BUF[epIndexIndex++] = epIndex;
                        }
                    }
                }
            }
        }
    }

    @Override
    public final void undoMoveLeft() {
        while (--jellyIndex >= 0) {
            JELLY_BUF[jellyIndex].moveRight();
        }
    }

    @Override
    public final void undoMoveRight() {
        while (--jellyIndex >= 0) {
            JELLY_BUF[jellyIndex].moveLeft();
        }
    }

    private final void undoMoveDown() {
        while (--jellyIndex >= 0) {
            JELLY_BUF[jellyIndex].moveUp();
        }
    }

    private final void undoMoveUp() {
        while (--jellyIndex >= 0) {
            JELLY_BUF[jellyIndex].moveDown();
        }
    }

    @Override
    public final String toString() {
        return "board=" + board + ";jellies=" + (jellies == null ? "null" : Arrays.asList(jellies));
    }

    @Override
    public final Board getBoard() {
        return board;
    }

    @Override
    public final boolean isSolved() {
        return getJellyColorNb() == board.getJellyColorNb();
    }

    @Override
    public final int getJellyColorNb() {
        int nb = 0;
        for (final Jelly jelly : jellies) {
            nb += jelly.getSegmentNb();
        }
        return nb + getNotEmergedNb();
    }

    @Override
    public final int getJellyPositionNb() {
        int nb = 0;
        for (final Jelly jelly : jellies) {
            nb += jelly.getPositions().length;
        }
        return nb + getNotEmergedNb();
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

    @Override
    public final StringBuilder getSerialization() {
        return serialization;
    }

    @Override
    public final void clearSerialization() {
        serialization = null;
    }

    @Override
    public boolean[] getEmerged() {
        return emerged;
    }

    private int getNotEmergedNb() {
        int nb = 0;
        for (final boolean e : emerged) {
            if (!e) {
                nb++;
            }
        }
        for (final Jelly j : jellies) {
            nb += j.getEmergingPositionNb();
        }
        return nb;
    }
}
