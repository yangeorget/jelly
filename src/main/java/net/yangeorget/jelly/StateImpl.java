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
    private static final int[] EMERGED_INDEX_BUF = new int[Board.MAX_SIZE];
    private static int jelliesIndex;

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
        emerged = new boolean[board.getEmergingPositions().length];
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
        board.updateLinks();
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
        // faster than jellies = Arrays.copyOf(JELLIES_BUFFER, nb);
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
        final char[][] matrix = board.getMatrix();
        final byte[] emergingPositions = board.getEmergingPositions();
        final char[] emergingColors = board.getEmergingColors();
        boolean someEmerged = false;
        for (int j = 0; j < jellies.length; j++) {
            int freeIndex = computeEmergingCandidates(matrix, emergingPositions, emergingColors, jellies[j]);
            if (freeIndex > 0) {
                if (moveUp(j)) {
                    someEmerged = true;
                    while (--freeIndex >= 0) {
                        emergeCandidate(matrix, emergingPositions, emergingColors, freeIndex);
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
    final private int computeEmergingCandidates(final char[][] matrix,
                                                final byte[] emergingPositions,
                                                final char[] emergingColors,
                                                final Jelly jelly) {
        int freeIndex = 0;
        final int segmentNb = jelly.getSegmentNb();
        for (int segmentIndex = 0; segmentIndex < segmentNb; segmentIndex++) {
            final char segmentColor = jelly.getColor(segmentIndex);
            final int segmentStart = jelly.getStart(segmentIndex);
            final int segmentEnd = jelly.getEnd(segmentIndex);
            final byte[] positions = jelly.getPositions();
            for (int epIndex = 0; epIndex < emerged.length; epIndex++) {
                if (!emerged[epIndex]
                    && BoardImpl.toFloating(emergingColors[epIndex]) == segmentColor
                    && Utils.contains(positions,
                                      segmentStart,
                                      segmentEnd,
                                      (byte) (emergingPositions[epIndex] + Board.UP)) >= 0) {
                    EMERGED_INDEX_BUF[freeIndex++] = epIndex;
                }
            }
        }
        return freeIndex;
    }

    /**
     * Let an emerging candidate emerge.
     * @param matrix the matrix
     * @param emergingPositions the positions
     * @param emergingColors the colors
     * @param index the index of the candidate
     */
    final private void emergeCandidate(final char[][] matrix,
                                       final byte[] emergingPositions,
                                       final char[] emergingColors,
                                       final int index) {
        final int epIndex = EMERGED_INDEX_BUF[index];
        final byte emergingPosition = (byte) (emergingPositions[epIndex] + Board.UP);
        matrix[JellyImpl.getI(emergingPosition)][JellyImpl.getJ(emergingPosition)] = emergingColors[epIndex];
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

    private int getNotEmergedNb() {
        int nb = 0;
        for (final boolean e : emerged) {
            if (!e) {
                nb++;
            }
        }
        return nb;
    }

    @Override
    public final boolean isSolved() {
        int jellyNb = 0;
        for (final Jelly jelly : jellies) {
            jellyNb += jelly.getSegmentNb();
        }
        return jellyNb + getNotEmergedNb() == board.getJellyColorNb();
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
}
