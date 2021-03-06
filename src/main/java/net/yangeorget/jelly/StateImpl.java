package net.yangeorget.jelly;

import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An implementation of a state.
 * @author y.georget
 */
public final class StateImpl
        implements State {
    private static final Logger LOG = LoggerFactory.getLogger(StateImpl.class);
    private static final Jelly[] JELLY_BUF = new Jelly[Board.MAX_SIZE], EP_JELLY_BUF = new Jelly[Board.MAX_SIZE];
    private static final int[] EP_INDEX_BUF = new int[Board.MAX_SIZE];
    private static final boolean[] BLOCKED = new boolean[Board.MAX_SIZE];
    private static final Serializer SERIALIZER = new SerializerRLEImpl();
    private static int jellyIndex, emergingIndex;

    private final Board board;
    private byte[] serialization;
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
        // the first serialization would depend on the way the board has been entered
        // updateSerialization();
        updateFromBoard();
        updateBoard();
        updateSerialization();
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
        board.storeFloatingEmerging();
    }

    @Override
    public final void updateSerialization() {
        serialization = SERIALIZER.serialize(this);
    }

    @Override
    public final void updateFromBoard() {
        final int height = board.getHeight();
        final int width = board.getWidth();
        jellyIndex = 0;
        final byte[][] matrix = board.getMatrix();
        for (byte i = 0; i < height; i++) {
            final byte[] line = matrix[i];
            for (byte j = 0; j < width; j++) {
                if (line[j] >= Board.A_BYTE) {
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
    public final boolean move(final int j, final int vec) {
        jellyIndex = 0;
        return move(jellies[j], vec);
    }

    final boolean move(final Jelly jelly, final int vec) {
        if (jelly.mayMove(vec)) {
            jelly.move(vec);
            JELLY_BUF[jellyIndex++] = jelly;
            if (jelly.overlapsWalls()) {
                return false;
            }
            for (final Jelly j : jellies) {
                if (!jelly.equals(j) && jelly.overlaps(j) && !move(j, vec)) {
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
        gravity();
        emergence();
    }

    /**
     * Applies gravity.
     */
    final void gravity() {
        Arrays.fill(BLOCKED, false);
        for (boolean iterate = true; iterate;) {
            iterate = false;
            for (int i = jellies.length; --i >= 0;) {
                if (!BLOCKED[i]) {
                    if (move(i, Board.DOWN)) {
                        iterate = true;
                    } else {
                        undoMove(Board.DOWN);
                        BLOCKED[i] = true;
                    }
                }
            }
        }
        // in any case we want to:
        // - update the serialization
        // - merge jellies
        updateBoard();
        updateSerialization();
        updateFromBoard();

    }

    /**
     * Give a chance to emerging jellies.
     */
    final void emergence() {
        final byte vec = Board.UP;
        for (int j = 0; j < jellies.length; j++) { // TODO: this is wrong since the number of jellies is going to evolve
                                                   // inside this loop
            // instead we want to continue until everything has emerged
            emergingIndex = 0;
            final Jelly jelly = jellies[j];
            for (int segmentIndex = 0; segmentIndex < jelly.getSegmentNb(); segmentIndex++) {
                final byte segmentColor = jelly.getColor(segmentIndex);
                final byte end = jelly.getEnd(segmentIndex);
                for (int i = jelly.getStart(segmentIndex); i < end; i++) {
                    final byte ep = (byte) (jelly.getPosition(i) - vec);
                    int epIndex = board.getEmergingIndex(ep);
                    if (epIndex >= 0) {
                        if (!emerged[epIndex] && BoardImpl.toFloating(board.getEmergingColor(epIndex)) == segmentColor) {
                            storeEmergingCandidate(null, epIndex);
                        }
                    } else {
                        for (final Jelly je : jellies) {
                            epIndex = je.getEpIndex(ep);
                            if (epIndex >= 0) {
                                if (BoardImpl.toFloating(je.getEmergingColor(epIndex)) == segmentColor) {
                                    storeEmergingCandidate(je, epIndex);
                                }
                                break;
                            }
                        }

                    }
                }
            }
            if (emergingIndex > 0) {
                if (move(j, vec)) {
                    while (--emergingIndex >= 0) {
                        final Jelly epJelly = EP_JELLY_BUF[emergingIndex];
                        final int epIndex = EP_INDEX_BUF[emergingIndex];
                        if (epJelly == null) { // let the candidate emerge from the wall
                            board.setColor((byte) (board.getEmergingPosition(epIndex) + vec),
                                           board.getEmergingColor(epIndex));
                            emerged[epIndex] = true;
                        } else { // let the candidate emerge from the jelly
                            board.setColor((byte) (epJelly.getEmergingPosition(epIndex) + vec),
                                           epJelly.getEmergingColor(epIndex));
                            epJelly.markAsEmerged(epIndex);
                        }
                    }
                    updateBoard();
                    updateSerialization();
                    updateFromBoard();
                } else {
                    undoMove(vec);
                }
            }
        }
    }

    private final void storeEmergingCandidate(final Jelly jelly, final int epIndex) {
        EP_JELLY_BUF[emergingIndex] = jelly;
        EP_INDEX_BUF[emergingIndex] = epIndex;
        emergingIndex++;
    }

    @Override
    public final void undoMove(final int vec) {
        while (--jellyIndex >= 0) {
            JELLY_BUF[jellyIndex].move(-vec);
        }
    }

    @Override
    public final String toString() {
        return "board="
               + board
               + ";emerged="
               + Arrays.toString(emerged)
               + ";jellies="
               + (jellies == null ? "null" : Arrays.asList(jellies))
               + ";serialization="
               + Arrays.toString(serialization);
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
        int nb = getNotEmergedFromWallsNb();
        for (final Jelly jelly : jellies) {
            nb += jelly.getSegmentNb() + jelly.getEmergingColorNb();
        }
        return nb;
    }

    @Override
    public final int getJellyPositionNb() {
        int nb = getNotEmergedFromWallsNb();
        for (final Jelly jelly : jellies) {
            nb += jelly.getPositionNb() + jelly.getEmergingColorNb();
        }
        return nb;
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
    public final byte[] getSerialization() {
        return serialization;
    }

    @Override
    public final void clearSerialization() {
        serialization = null;
    }

    @Override
    public final boolean[] getEmerged() {
        return emerged;
    }

    private final int getNotEmergedFromWallsNb() {
        int nb = 0;
        for (final boolean e : emerged) {
            if (!e) {
                nb++;
            }
        }
        return nb;
    }
}
