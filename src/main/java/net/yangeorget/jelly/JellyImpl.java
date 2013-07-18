package net.yangeorget.jelly;

import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author y.georget
 */
public final class JellyImpl
        implements Jelly {
    private static final Logger LOG = LoggerFactory.getLogger(JellyImpl.class);

    /**
     * Contains starting points of candidate segments (which may be empty).
     */
    private static final byte[] CANDIDATE_SEGMENT_BUF = new byte[Board.MAX_SIZE];
    /**
     * Candidate positions for current segment (thus of the same color).
     */
    private static final byte[] CANDIDATE_POS_BUF = new byte[Board.MAX_SIZE];
    /**
     * All the positions of a jelly.
     */
    private static final byte[] POS_BUF = new byte[Board.MAX_SIZE];
    /**
     * All the colors of a jelly.
     */
    private static final byte[] COL_BUF = new byte[Board.MAX_SIZE];
    /**
     * Ends (in the sense of next free indices) of jelly segments.
     */
    private static final byte[] END_BUF = new byte[Board.MAX_SIZE];

    private static final byte[] EP_IDX_BUF = new byte[Board.MAX_EMERGING_FLOATING];
    private static final byte[] EP_COL_BUF = new byte[Board.MAX_EMERGING_FLOATING];

    private static int segmentIndex, floatingIndex, freeSegmentIndex, emptySegmentNb;

    byte[] positions, end, color, emergingIndices, emergingColors;
    private final Board board;
    private boolean isFloating;
    private byte shift;

    /**
     * Used in test only.
     * @param board
     * @param isFixed
     * @param leftMin
     * @param rightMax
     * @param topMin
     * @param bottomMax
     * @param color
     * @param positions
     */
    JellyImpl(final Board board, final char color, final byte... positions) {
        this(board,
             true,
             (byte) 0,
             new byte[] { (byte) positions.length },
             new byte[] { (byte) color },
             positions,
             new byte[0],
             new byte[0]);
    }

    private JellyImpl(final Board board,
                      final boolean isFloating,
                      final byte shift,
                      final byte[] end,
                      final byte[] color,
                      final byte[] positions,
                      final byte[] emergingIndices,
                      final byte[] emergingColors) {
        this.board = board;
        this.isFloating = isFloating;
        this.shift = shift;
        this.emergingColors = Arrays.copyOf(emergingColors, emergingColors.length);
        // no need to clone since they won't be updated
        this.positions = positions;
        this.color = color;
        this.end = end;
        this.emergingIndices = emergingIndices;
    }

    public JellyImpl(final Board board, final int i, final int j) {
        this.board = board;
        isFloating = true;
        update(board.getLinkStarts(), board.getLinkEnds(), i, j);
        color = Arrays.copyOf(COL_BUF, segmentIndex);
        end = Arrays.copyOf(END_BUF, segmentIndex);
        positions = Arrays.copyOf(POS_BUF, getStart(end, segmentIndex));
        floatingIndex = 0;
        final int floatingNb = board.getFloatingEmergingPositionNb();
        for (int fepIndex = 0; fepIndex < floatingNb; fepIndex++) {
            final int epIndex = getEmergingIndex(board.getFloatingEmergingPosition(fepIndex));
            if (epIndex >= 0) {
                EP_IDX_BUF[floatingIndex] = (byte) epIndex;
                EP_COL_BUF[floatingIndex] = board.getFloatingEmergingColor(fepIndex);
                floatingIndex++;
            }
        }
        emergingIndices = Arrays.copyOf(EP_IDX_BUF, floatingIndex);
        emergingColors = Arrays.copyOf(EP_COL_BUF, floatingIndex);
    }

    private final void update(final byte[] linkStarts, final byte[] linkEnds, final int si, final int sj) {
        // we have one single candidate segment for now
        CANDIDATE_SEGMENT_BUF[0] = Cells.value(si, sj);
        freeSegmentIndex = 1;
        // we don't have any empty segment yet
        segmentIndex = emptySegmentNb = 0;
        while (segmentIndex + emptySegmentNb < freeSegmentIndex) {
            final byte start = getStart(END_BUF, segmentIndex);
            // the current segment ends where it starts (it is empty)
            END_BUF[segmentIndex] = start;
            // let's init the candidate positions with the current candidate segment
            CANDIDATE_POS_BUF[0] = CANDIDATE_SEGMENT_BUF[segmentIndex];
            for (int index = 0, freeIndex = 1; index < freeIndex; index++) {
                final byte pos = CANDIDATE_POS_BUF[index];
                final int i = Cells.getI(pos);
                final int j = Cells.getJ(pos);
                final byte c = board.getColor(i, j);
                if (c >= Board.A_BYTE) {
                    final byte color = BoardImpl.toFloating(c);
                    // let's store the color of the current segment if not yet done
                    if (start == END_BUF[segmentIndex]) {
                        COL_BUF[segmentIndex] = color;
                    }
                    // has to be true because we want to treat the current segment only here
                    if (color == COL_BUF[segmentIndex]) {
                        board.blank(i, j);
                        isFloating &= BoardImpl.isFloating(c);
                        insertPositionInSortedSegment(start, pos);
                        handleLinkedPosition(linkStarts, linkEnds, pos);
                        if (i > 0) {
                            CANDIDATE_POS_BUF[freeIndex++] = (byte) (pos + Board.UP);
                        }
                        if (i < board.getHeight1()) {
                            CANDIDATE_POS_BUF[freeIndex++] = (byte) (pos + Board.DOWN);
                        }
                        if (j > 0) {
                            CANDIDATE_POS_BUF[freeIndex++] = (byte) (pos + Board.LEFT);
                        }
                        if (j < board.getWidth1()) {
                            CANDIDATE_POS_BUF[freeIndex++] = (byte) (pos + Board.RIGHT);
                        }
                    }
                }
            }
            if (start == END_BUF[segmentIndex]) {
                emptySegmentNb++;
            } else {
                segmentIndex++;
            }
        }
    }

    /**
     * Let's insert pos in POS_BUF while keeping the segment sorted.
     */
    private final void insertPositionInSortedSegment(final int start, final byte pos) {
        final int end = END_BUF[segmentIndex];
        final int insertionPoint = -1 - Arrays.binarySearch(POS_BUF, start, end, pos);
        for (int i = end; i > insertionPoint; i--) {
            POS_BUF[i] = POS_BUF[i - 1];
        }
        POS_BUF[insertionPoint] = pos;
        END_BUF[segmentIndex]++;
    }

    private final void handleLinkedPosition(final byte[] linkStarts, final byte[] linkEnds, final byte pos) {
        final int idx = Utils.contains(linkStarts, 0, linkStarts.length, pos);
        if (idx >= 0) {
            final byte linkedPos = linkEnds[idx];
            if (Utils.contains(POS_BUF, 0, END_BUF[segmentIndex], linkedPos) < 0
                && Utils.contains(CANDIDATE_SEGMENT_BUF, 0, freeSegmentIndex, linkedPos) < 0) {
                CANDIDATE_SEGMENT_BUF[freeSegmentIndex++] = linkedPos;
            }
        }
    }

    @Override
    public final JellyImpl clone() {
        return new JellyImpl(board, isFloating, shift, end, color, positions, emergingIndices, emergingColors);
    }

    @Override
    public final String toString() {
        return ";isFloating="
               + isFloating
               + ";shift="
               + shift
               + ";positions="
               + Arrays.toString(positions)
               + ";color="
               + Arrays.toString(color)
               + ";end="
               + Arrays.toString(end)
               + ";emergingIndices="
               + Arrays.toString(emergingIndices)
               + ";emergingColors="
               + Arrays.toString(emergingColors);
    }

    private final void move(final int vec) {
        shift += vec;
    }

    @Override
    public final boolean mayMoveLeft() {
        if (!isFloating) {
            return false;
        }
        for (int index = positions.length; --index >= 0;) {
            if (Cells.getJ(getPosition(index)) == 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public final void moveLeft() {
        move(Board.LEFT);
    }

    @Override
    public final boolean mayMoveRight() {
        if (!isFloating) {
            return false;
        }
        for (int index = positions.length; --index >= 0;) {
            if (Cells.getJ(getPosition(index)) == board.getWidth1()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public final void moveRight() {
        move(Board.RIGHT);
    }

    @Override
    public final boolean mayMoveDown() {
        if (!isFloating) {
            return false;
        }
        for (int index = positions.length; --index >= 0;) {
            if (Cells.getI(getPosition(index)) == board.getHeight1()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public final void moveDown() {
        move(Board.DOWN);
    }

    @Override
    public final boolean mayMoveUp() {
        if (!isFloating) {
            return false;
        }
        for (int index = positions.length; --index >= 0;) {
            if (Cells.getI(getPosition(index)) == 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public final void moveUp() {
        move(Board.UP);
    }

    @Override
    public final boolean overlaps(final Jelly jelly) {
        final JellyImpl j = (JellyImpl) jelly;
        for (int segment = 0; segment < end.length; segment++) {
            if (j.overlaps(shift, positions, end, segment)) {
                return true;
            }
        }
        return false;
    }

    final boolean overlaps(final byte aShift, final byte[] aPositions, final byte[] aEnd, final int aSegment) {
        for (int segment = 0; segment < end.length; segment++) {
            if (overlaps(shift, positions, end, segment, aShift, aPositions, aEnd, aSegment)) {
                return true;
            }
        }
        return false;
    }

    static final boolean overlaps(final byte aShift,
                                  final byte[] aPositions,
                                  final byte[] aEnd,
                                  final int aSegment,
                                  final byte bShift,
                                  final byte[] bPositions,
                                  final byte[] bEnd,
                                  final int bSegment) {
        for (int aIndex = getStart(aEnd, aSegment), bIndex = getStart(bEnd, bSegment);;) {
            while (aPositions[aIndex] + aShift < bPositions[bIndex] + bShift) {
                if (++aIndex == aEnd[aSegment]) {
                    return false;
                }
            }
            if (aPositions[aIndex] + aShift == bPositions[bIndex] + bShift) {
                return true;
            }
            while (aPositions[aIndex] + aShift > bPositions[bIndex] + bShift) {
                if (++bIndex == bEnd[bSegment]) {
                    return false;
                }
            }
        }
    }

    @Override
    public final boolean overlapsWalls() {
        for (int index = 0; index < positions.length; index++) {
            if (board.isWall(getPosition(index))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public final void updateBoard() {
        final int size = end.length;
        if (size == 1) {
            updateBoard((byte) 0, end[0], color[0]);
        } else {
            int ls = getStart(end, size - 1);
            int le;
            for (int i = 0; i < size; i++) {
                le = getStart(end, i);
                updateBoard(le, end[i], color[i]);
                board.addLink(getPosition(ls), getPosition(le));
                ls = le;
            }
        }
        for (int epIndex = 0; epIndex < getNotEmergedNb(); epIndex++) {
            final byte color = getEmergingColor(epIndex);
            if (color != 0) {
                board.addFloatingEmerging(getEmergingPosition(epIndex), color);
            }
        }
    }

    private final void updateBoard(final int start, final int end, byte c) {
        if (!isFloating) {
            c = BoardImpl.toFixed(c);
        }
        for (int j = start; j < end; j++) {
            board.setColor(getPosition(j), c);
        }
    }

    private static final byte getStart(final byte[] end, final int segmentIndex) {
        return segmentIndex == 0 ? 0 : end[segmentIndex - 1];
    }

    @Override
    public final int getSegmentNb() {
        return color.length;
    }

    @Override
    public final int getPositionsNb() {
        return positions.length;
    }

    @Override
    public final byte getPosition(final int index) {
        return (byte) (positions[index] + shift);
    }

    @Override
    public final byte getStart(final int segmentIndex) {
        return getStart(end, segmentIndex);
    }

    @Override
    public final byte getEnd(final int segmentIndex) {
        return end[segmentIndex];
    }

    @Override
    public final byte getColor(final int segmentIndex) {
        return color[segmentIndex];
    }

    @Override
    public final byte getEmergingColor(final int epIndex) {
        return emergingColors[epIndex];
    }

    @Override
    public final byte getEmergingPosition(final int epIndex) {
        return getPosition(emergingIndices[epIndex]);
    }

    @Override
    public final int getEpIndex(final byte ep) {
        return Arrays.binarySearch(emergingIndices, (byte) getEmergingIndex(ep));
    }

    private final int getEmergingIndex(final byte ep) {
        return Utils.contains(positions, 0, positions.length, (byte) (ep - shift));
    }

    @Override
    public final int getNotEmergedNb() {
        return emergingColors.length;
    }

    @Override
    public final void markAsEmerged(final int epIndex) {
        emergingColors[epIndex] = 0;
    }

    @Override
    public boolean allEmerged() {
        for (final byte emergingColor : emergingColors) {
            if (emergingColor != 0) {
                return false;
            }
        }
        return true;
    }
}
