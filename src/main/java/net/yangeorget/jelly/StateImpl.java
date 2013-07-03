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
    private static final Jelly[] JELLIES_BUF = new Jelly[Board.MAX_SIZE];
    private static final int[] EMERGED_IDX_BUF = new int[Board.MAX_SIZE];
    private static int jelliesIndex;
    private static int freeIndex;

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
        final char[][] matrix = board.getMatrix();
        final boolean[][] walls = board.getWalls();
        final int height = board.getHeight();
        final int width = board.getWidth();
        jelliesIndex = 0;
        for (byte i = 0; i < height; i++) {
            for (byte j = 0; j < width; j++) {
                if (matrix[i][j] != Board.BLANK_CHAR && !walls[i][j]) {
                    JELLIES_BUF[jelliesIndex++] = new JellyImpl(board, i, j);
                }
            }
        }
        // faster than jellies = Arrays.copyOf(JELLIES_BUF, nb);
        jellies = new Jelly[jelliesIndex];
        System.arraycopy(JELLIES_BUF, 0, jellies, 0, jelliesIndex);
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
        jelliesIndex = 0;
        return moveLeft(jellies[j]);
    }

    final boolean moveLeft(final Jelly jelly) {
        if (jelly.mayMoveLeft()) {
            jelly.moveLeft();
            JELLIES_BUF[jelliesIndex++] = jelly;
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
        jelliesIndex = 0;
        return moveRight(jellies[j]);
    }

    final boolean moveRight(final Jelly jelly) {
        if (jelly.mayMoveRight()) {
            jelly.moveRight();
            JELLIES_BUF[jelliesIndex++] = jelly;
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
        jelliesIndex = 0;
        return moveDown(jellies[j]);
    }

    final boolean moveDown(final Jelly jelly) {
        if (jelly.mayMoveDown()) {
            jelly.moveDown();
            JELLIES_BUF[jelliesIndex++] = jelly;
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
        jelliesIndex = 0;
        return moveUp(jellies[j]);
    }

    final boolean moveUp(final Jelly jelly) {
        if (jelly.mayMoveUp()) {
            jelly.moveUp();
            JELLIES_BUF[jelliesIndex++] = jelly;
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
        boolean someEmerged = false;
        for (int j = 0; j < jellies.length; j++) {
            freeIndex = 0;
            computeEmergingCandidates(board, jellies[j]);
            // TODO: compute emerging candidates from jellies too
            if (freeIndex > 0) {
                if (moveUp(j)) {
                    someEmerged = true;
                    while (--freeIndex >= 0) {
                        emergeCandidate(board);
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
    final private void computeEmergingCandidates(final Board board, final Jelly jelly) {
        final int segmentNb = jelly.getSegmentNb();
        final byte[] positions = jelly.getPositions();
        for (int segmentIndex = 0; segmentIndex < segmentNb; segmentIndex++) {
            final char segmentColor = jelly.getColor(segmentIndex);
            final int start = jelly.getStart(segmentIndex);
            final int end = jelly.getEnd(segmentIndex);
            for (int i = start; i < end; i++) {
                // let's compute the emerging candidate from the walls
                final int epIndex = board.getEmergingIndex((byte) (positions[i] + Board.DOWN));
                if (epIndex >= 0
                    && !emerged[epIndex]
                    && BoardImpl.toFloating(board.getEmergingColor(epIndex)) == segmentColor) {
                    EMERGED_IDX_BUF[freeIndex++] = epIndex;
                }
                // TODO: compute the emerging candidates from the jellies
            }
        }
    }

    /**
     * Let an emerging candidate emerge.
     */
    final private void emergeCandidate(final Board board) {
        final int epIndex = EMERGED_IDX_BUF[freeIndex];
        final byte emergingPosition = (byte) (board.getEmergingPosition(epIndex) + Board.UP);
        board.getMatrix()[BoardImpl.getI(emergingPosition)][BoardImpl.getJ(emergingPosition)] = board.getEmergingColor(epIndex);
        emerged[epIndex] = true;
    }

    @Override
    public final void undoMoveLeft() {
        while (--jelliesIndex >= 0) {
            JELLIES_BUF[jelliesIndex].moveRight();
        }
    }

    @Override
    public final void undoMoveRight() {
        while (--jelliesIndex >= 0) {
            JELLIES_BUF[jelliesIndex].moveLeft();
        }
    }

    private final void undoMoveDown() {
        while (--jelliesIndex >= 0) {
            JELLIES_BUF[jelliesIndex].moveUp();
        }
    }

    private final void undoMoveUp() {
        while (--jelliesIndex >= 0) {
            JELLIES_BUF[jelliesIndex].moveDown();
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
        // TODO: compute also number non emerged from jellies
        return nb;
    }
}
