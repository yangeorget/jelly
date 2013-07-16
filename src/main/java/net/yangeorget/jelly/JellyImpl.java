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
    // bounding box
    private byte leftMin, rightMax, topMin, bottomMax;

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
    JellyImpl(final Board board,
              final byte leftMin,
              final byte rightMax,
              final byte topMin,
              final byte bottomMax,
              final char color,
              final byte... positions) {
        this(board,
             true,
             leftMin,
             rightMax,
             topMin,
             bottomMax,
             new byte[] { (byte) positions.length },
             new byte[] { (byte) color },
             positions,
             new byte[0],
             new byte[0]);
    }

    private JellyImpl(final Board board,
                      final boolean isFloating,
                      final byte leftMin,
                      final byte rightMax,
                      final byte topMin,
                      final byte bottomMax,
                      final byte[] end,
                      final byte[] color,
                      final byte[] positions,
                      final byte[] emergingIndices,
                      final byte[] emergingColors) {
        this.board = board;
        this.isFloating = isFloating;
        this.leftMin = leftMin;
        this.rightMax = rightMax;
        this.topMin = topMin;
        this.bottomMax = bottomMax;
        this.positions = Arrays.copyOf(positions, positions.length);
        this.color = Arrays.copyOf(color, color.length);
        this.end = Arrays.copyOf(end, end.length);
        this.emergingIndices = Arrays.copyOf(emergingIndices, emergingIndices.length);
        this.emergingColors = Arrays.copyOf(emergingColors, emergingColors.length);
    }

    public JellyImpl(final Board board, final int i, final int j) {
        this.board = board;
        isFloating = true;
        topMin = bottomMax = (byte) i;
        leftMin = rightMax = (byte) j;
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

    private void update(final byte[] linkStarts, final byte[] linkEnds, final int si, final int sj) {
        // we have one single candidate segment for now
        CANDIDATE_SEGMENT_BUF[0] = Cells.value(si, sj);
        freeSegmentIndex = 1;
        // we don't have any empty segment yet
        segmentIndex = emptySegmentNb = 0;
        while (segmentIndex + emptySegmentNb < freeSegmentIndex) {
            final int start = getStart(END_BUF, segmentIndex);
            // the current segment ends where it starts (it is empty)
            END_BUF[segmentIndex] = (byte) start;
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
                        if (j < leftMin) {
                            leftMin = (byte) j;
                        }
                        if (rightMax < j) {
                            rightMax = (byte) j;
                        }
                        if (i < topMin) {
                            topMin = (byte) i;
                        }
                        if (bottomMax < i) {
                            bottomMax = (byte) i;
                        }
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
    private void insertPositionInSortedSegment(final int start, final byte pos) {
        final int end = END_BUF[segmentIndex];
        final int insertionPoint = -1 - Arrays.binarySearch(POS_BUF, start, end, pos);
        for (int i = end; i > insertionPoint; i--) {
            POS_BUF[i] = POS_BUF[i - 1];
        }
        POS_BUF[insertionPoint] = pos;
        END_BUF[segmentIndex]++;
    }

    private void handleLinkedPosition(final byte[] linkStarts, final byte[] linkEnds, final byte pos) {
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
    public JellyImpl clone() {
        return new JellyImpl(board,
                             isFloating,
                             leftMin,
                             rightMax,
                             topMin,
                             bottomMax,
                             end,
                             color,
                             positions,
                             emergingIndices,
                             emergingColors);
    }

    @Override
    public String toString() {
        return "positions="
               + Arrays.toString(positions)
               + ";color="
               + Arrays.toString(color)
               + ";end="
               + Arrays.toString(end)
               + ";isFloating="
               + isFloating
               + ";leftMin="
               + leftMin
               + ";rightMax="
               + rightMax
               + ";topMin="
               + topMin
               + ";bottomMax="
               + bottomMax
               + ";emergingIndices="
               + Arrays.toString(emergingIndices)
               + ";emergingColors="
               + Arrays.toString(emergingColors);
    }

    private void move(final int vec) {
        for (int index = positions.length; --index >= 0;) {
            positions[index] += vec;
        }
    }

    @Override
    public boolean mayMoveLeft() {
        return isFloating && leftMin != 0;
    }

    @Override
    public void moveLeft() {
        move(Board.LEFT);
        leftMin--;
        rightMax--;
    }

    @Override
    public boolean mayMoveRight() {
        return isFloating && rightMax != board.getWidth1();
    }

    @Override
    public void moveRight() {
        move(Board.RIGHT);
        leftMin++;
        rightMax++;
    }

    @Override
    public boolean mayMoveDown() {
        return isFloating && bottomMax != board.getHeight1();
    }

    @Override
    public void moveDown() {
        move(Board.DOWN);
        topMin++;
        bottomMax++;
    }

    @Override
    public boolean mayMoveUp() {
        return isFloating && topMin != 0;
    }

    @Override
    public void moveUp() {
        move(Board.UP);
        topMin--;
        bottomMax--;
    }

    @Override
    public boolean overlaps(final Jelly jelly) {
        final JellyImpl j = (JellyImpl) jelly;
        if (j.rightMax < leftMin || rightMax < j.leftMin || j.bottomMax < topMin || bottomMax < j.topMin) {
            return false;
        }
        for (int segment = 0; segment < end.length; segment++) {
            if (j.overlaps(positions, end, segment)) {
                return true;
            }
        }
        return false;
    }

    boolean overlaps(final byte[] aPositions, final byte[] aEnd, final int aSegment) {
        for (int segment = 0; segment < end.length; segment++) {
            if (overlaps(positions, end, segment, aPositions, aEnd, aSegment)) {
                return true;
            }
        }
        return false;
    }

    static boolean overlaps(final byte[] aPositions,
                            final byte[] aEnd,
                            final int aSegment,
                            final byte[] bPositions,
                            final byte[] bEnd,
                            final int bSegment) {
        for (int aIndex = getStart(aEnd, aSegment), bIndex = getStart(bEnd, bSegment);;) {
            while (aPositions[aIndex] < bPositions[bIndex]) {
                if (++aIndex == aEnd[aSegment]) {
                    return false;
                }
            }
            if (aPositions[aIndex] == bPositions[bIndex]) {
                return true;
            }
            while (aPositions[aIndex] > bPositions[bIndex]) {
                if (++bIndex == bEnd[bSegment]) {
                    return false;
                }
            }
        }
    }

    @Override
    public boolean overlapsWalls() {
        for (final byte position : positions) {
            if (board.isWall(position)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void updateBoard() {
        final int size = end.length;
        if (size == 1) {
            updateBoard((byte) 0, end[0], color[0]);
        } else {
            int ls = getStart(end, size - 1);
            int le;
            for (int i = 0; i < size; i++) {
                le = getStart(end, i);
                updateBoard(le, end[i], color[i]);
                board.addLink(positions[ls], positions[le]);
                ls = le;
            }
        }
        for (int epIndex = 0; epIndex < getNotEmergedNb(); epIndex++) {
            final byte color = getEmergingColor(epIndex);
            if (color != 0) {
                board.addFloatingEmerging(getEmergingPosition(emergingIndices[epIndex]), color);
            }
        }
    }

    private void updateBoard(final int start, final int end, byte c) {
        if (!isFloating) {
            c = BoardImpl.toFixed(c);
        }
        for (int j = start; j < end; j++) {
            board.setColor(positions[j], c);
        }
    }

    private static int getStart(final byte[] end, final int segmentIndex) {
        return segmentIndex == 0 ? 0 : end[segmentIndex - 1];
    }

    @Override
    public int getSegmentNb() {
        return color.length;
    }

    @Override
    public byte[] getPositions() {
        return positions;
    }

    @Override
    public int getStart(final int segmentIndex) {
        return getStart(end, segmentIndex);
    }

    @Override
    public int getEnd(final int segmentIndex) {
        return end[segmentIndex];
    }

    @Override
    public byte getColor(final int segmentIndex) {
        return color[segmentIndex];
    }

    @Override
    public byte getEmergingColor(final int epIndex) {
        return emergingColors[epIndex];
    }

    @Override
    public byte getEmergingPosition(final int emergingIndex) {
        return positions[emergingIndex];
    }

    @Override
    public int getEpIndex(final byte ep) {
        return Arrays.binarySearch(emergingIndices, (byte) getEmergingIndex(ep));
    }

    private int getEmergingIndex(final byte ep) {
        return Utils.contains(positions, 0, positions.length, ep);
    }

    @Override
    public int getNotEmergedNb() {
        return emergingColors.length;
    }

    @Override
    public void markAsEmerged(final int epIndex) {
        emergingColors[epIndex] = 0;
    }
}
