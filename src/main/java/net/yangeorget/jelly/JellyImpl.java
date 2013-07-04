package net.yangeorget.jelly;

import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author y.georget
 */
public class JellyImpl
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
    private static final char[] COL_BUF = new char[Board.MAX_SIZE];
    /**
     * Ends (in the sense of next free indices) of jelly segments.
     */
    private static final int[] END_BUF = new int[Board.MAX_SIZE];

    private static int segmentIndex;
    private static int freeSegmentIndex;
    private static int emptySegmentNb;

    byte[] positions;
    char[] color; // TODO: use byte instead
    int[] end;
    private final Board board;
    private boolean isFixed;
    // bounding box
    private byte leftMin;
    private byte rightMax;
    private byte topMin;
    private byte bottomMax;

    JellyImpl(final Board board,
              final boolean isFixed,
              final byte leftMin,
              final byte rightMax,
              final byte topMin,
              final byte bottomMax,
              final char color,
              final byte... positions) {
        this(board,
             isFixed,
             leftMin,
             rightMax,
             topMin,
             bottomMax,
             new int[] { positions.length },
             new char[] { color },
             positions);
    }

    private JellyImpl(final Board board,
                      final boolean isFixed,
                      final byte leftMin,
                      final byte rightMax,
                      final byte topMin,
                      final byte bottomMax,
                      final int[] end,
                      final char[] color,
                      final byte[] positions) {
        this.board = board;
        this.isFixed = isFixed;
        this.leftMin = leftMin;
        this.rightMax = rightMax;
        this.topMin = topMin;
        this.bottomMax = bottomMax;
        this.positions = Arrays.copyOf(positions, positions.length);
        this.color = Arrays.copyOf(color, color.length);
        this.end = Arrays.copyOf(end, end.length);
    }

    public JellyImpl(final Board board, final int i, final int j) {
        this.board = board;
        topMin = bottomMax = (byte) i;
        leftMin = rightMax = (byte) j;
        update(board.getLinkStarts(), board.getLinkEnds(), i, j);
        color = Arrays.copyOf(COL_BUF, segmentIndex);
        end = Arrays.copyOf(END_BUF, segmentIndex);
        positions = Arrays.copyOf(POS_BUF, getStart(end, segmentIndex));
    }

    private final void update(final byte[] linkStarts, final byte[] linkEnds, final int si, final int sj) {
        // we have one single candidate segment for now
        CANDIDATE_SEGMENT_BUF[0] = BoardImpl.value(si, sj);
        freeSegmentIndex = 1;
        // we don't have any empty segment yet
        segmentIndex = emptySegmentNb = 0;
        while (segmentIndex + emptySegmentNb < freeSegmentIndex) {
            final int start = getStart(END_BUF, segmentIndex);
            // the current segment ends where it starts (it is empty)
            END_BUF[segmentIndex] = start;
            // let's init the candidate positions with the current candidate segment
            CANDIDATE_POS_BUF[0] = CANDIDATE_SEGMENT_BUF[segmentIndex];
            for (int index = 0, freeIndex = 1; index < freeIndex; index++) {
                final byte pos = CANDIDATE_POS_BUF[index];
                if (!board.isBlank(pos)) {
                    final char c = board.getColor(pos);
                    final char color = BoardImpl.toFloating(c);
                    // let's store the color of the current segment if not yet done
                    if (start == END_BUF[segmentIndex]) {
                        COL_BUF[segmentIndex] = color;
                    }
                    // has to be true because we want to treat the current segment only here
                    if (color == COL_BUF[segmentIndex]) {
                        board.blank(pos);
                        isFixed |= BoardImpl.isFixed(c);
                        insertPositionInSortedSegment(start, pos);
                        handleLinkedPosition(linkStarts, linkEnds, pos);
                        final byte i = (byte) BoardImpl.getI(pos);
                        final byte j = (byte) BoardImpl.getJ(pos);
                        if (j < leftMin) {
                            leftMin = j;
                        }
                        if (rightMax < j) {
                            rightMax = j;
                        }
                        if (i < topMin) {
                            topMin = i;
                        }
                        if (bottomMax < i) {
                            bottomMax = i;
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
        return new JellyImpl(board, isFixed, leftMin, rightMax, topMin, bottomMax, end, color, positions);
    }

    @Override
    public final String toString() {
        return "positions="
               + Arrays.toString(positions)
               + ";color="
               + Arrays.toString(color)
               + ";end="
               + Arrays.toString(end);
    }

    private final void move(final int vec) {
        for (int index = positions.length; --index >= 0;) {
            positions[index] += vec;
        }
    }

    @Override
    public final boolean mayMoveLeft() {
        return !isFixed && leftMin != 0;
    }

    @Override
    public final void moveLeft() {
        move(Board.LEFT);
        leftMin--;
        rightMax--;
    }

    @Override
    public final boolean mayMoveRight() {
        return !isFixed && rightMax != board.getWidth1();
    }

    @Override
    public final void moveRight() {
        move(Board.RIGHT);
        leftMin++;
        rightMax++;
    }

    @Override
    public final boolean mayMoveDown() {
        return !isFixed && bottomMax != board.getHeight1();
    }

    @Override
    public final void moveDown() {
        move(Board.DOWN);
        topMin++;
        bottomMax++;
    }

    @Override
    public final boolean mayMoveUp() {
        return !isFixed && topMin != 0;
    }

    @Override
    public final void moveUp() {
        move(Board.UP);
        topMin--;
        bottomMax--;
    }

    @Override
    public final boolean overlaps(final Jelly jelly) {
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

    final boolean overlaps(final byte[] aPositions, final int[] aEnd, final int aSegment) {
        for (int segment = 0; segment < end.length; segment++) {
            if (overlaps(positions, end, segment, aPositions, aEnd, aSegment)) {
                return true;
            }
        }
        return false;
    }

    static final boolean overlaps(final byte[] aPositions,
                                  final int[] aEnd,
                                  final int aSegment,
                                  final byte[] bPositions,
                                  final int[] bEnd,
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
    public final boolean overlapsWalls() {
        for (final byte position : positions) {
            if (board.isWall(position)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public final void updateBoard() {
        final int size = end.length;
        if (size == 1) {
            updateBoard(0, end[0], color[0]);
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
        // TODO update with emerging
    }

    private final void updateBoard(final int start, final int end, char c) {
        c = isFixed ? BoardImpl.toFixed(c) : c;
        for (int j = start; j < end; j++) {
            board.setColor(positions[j], c);
        }
    }

    private final static int getStart(final int[] end, final int index) {
        return index == 0 ? 0 : end[index - 1];
    }

    @Override
    public final int getSegmentNb() {
        return color.length;
    }

    @Override
    public final byte[] getPositions() {
        return positions;
    }

    @Override
    public final int getStart(final int segmentIndex) {
        return getStart(end, segmentIndex);
    }

    @Override
    public final int getEnd(final int segmentIndex) {
        return end[segmentIndex];
    }

    @Override
    public final char getColor(final int segmentIndex) {
        return color[segmentIndex];
    }

    @Override
    public char getEmergingColor(final int epIndex) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public byte getEmergingPosition(final int epIndex) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getEmergingPositionNb() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getEmergingIndex(final byte ep) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void markAsEmerged(final int epIndex) {
        // TODO Auto-generated method stub
    }
}
