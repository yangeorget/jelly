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
    private static final Serializer SERIALIZER = new SerializerCountImpl();
    private static final Jelly[] JELLY_BUF = new Jelly[Board.MAX_SIZE], EP_JELLY_BUF = new Jelly[Board.MAX_SIZE];
    private static final int[] EP_INDEX_BUF = new int[Board.MAX_SIZE];
    private static int jellyIndex, emergingIndex;

    private final Board board;
    private Object serialization;
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
        // the first serialization depends on the way the board has been entered
        updateFromBoard();
        // this is why we do a second serialization
        updateBoard();
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
        board.clearFloatingEmerging();
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
                if (board.getColor(i, j) >= Board.A_BYTE) {
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
        if (moveUp()) {
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
        if (getNotEmergedNb() == 0) {
            return false;
        }
        boolean someEmerged = false;
        for (int j = 0; j < jellies.length; j++) {
            emergingIndex = 0;
            // it seems smarter to compute this before moving the jelly up
            computeEmergingCandidates(jellies[j]);
            if (emergingIndex > 0) {
                if (moveUp(j)) {
                    someEmerged = true;
                    while (--emergingIndex >= 0) {
                        final Jelly epJelly = EP_JELLY_BUF[emergingIndex];
                        final int epIndex = EP_INDEX_BUF[emergingIndex];
                        if (epJelly == null) {
                            // let the candidate emerge from the wall
                            board.setColor((byte) (board.getEmergingPosition(epIndex) + Board.UP),
                                           board.getEmergingColor(epIndex));
                            emerged[epIndex] = true;
                        } else {
                            // let the candidate emerge from the jelly
                            board.setColor((byte) (epJelly.getEmergingPosition(epIndex) + Board.UP),
                                           epJelly.getEmergingColor(epIndex));
                            epJelly.markAsEmerged(epIndex);
                        }
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
            final byte segmentColor = jelly.getColor(segmentIndex);
            final int start = jelly.getStart(segmentIndex);
            final int end = jelly.getEnd(segmentIndex);
            for (int i = start; i < end; i++) {
                final byte ep = (byte) (positions[i] + Board.DOWN);
                if (!computeEmergingCandidateFromWalls(ep, segmentColor)) {
                    computeEmergingCandidateFromJellies(ep, segmentColor);
                }
            }
        }
    }

    private boolean computeEmergingCandidateFromWalls(final byte ep, final byte segmentColor) {
        final int epIndex = board.getEmergingIndex(ep);
        if (epIndex >= 0) {
            if (!emerged[epIndex] && BoardImpl.toFloating(board.getEmergingColor(epIndex)) == segmentColor) {
                storeEmergingCandidate(null, epIndex);
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean computeEmergingCandidateFromJellies(final byte ep, final byte segmentColor) {
        for (final Jelly j : jellies) {
            if (j.getNotEmergedNb() > 0) { // this is an optimization
                final int epIndex = j.getEpIndex(ep);
                if (epIndex >= 0) {
                    if (BoardImpl.toFloating(j.getEmergingColor(epIndex)) == segmentColor) {
                        storeEmergingCandidate(j, epIndex);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private void storeEmergingCandidate(final Jelly jelly, final int epIndex) {
        EP_JELLY_BUF[emergingIndex] = jelly;
        EP_INDEX_BUF[emergingIndex] = epIndex;
        emergingIndex++;
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
    public final Object getSerialization() {
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
            nb += j.getNotEmergedNb();
        }
        return nb;
    }
}
