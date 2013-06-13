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
    char[] color;
    int[] end;
    private final Board board;
    private final byte height1;
    private final byte width1;
    private boolean isFixed;
    // bounding box
    private byte leftMin;
    private byte rightMax;
    private byte topMin;
    private byte bottomMax;

    JellyImpl(final State state,
              final boolean isFixed,
              final byte leftMin,
              final byte rightMax,
              final byte topMin,
              final byte bottomMax,
              final char color,
              final byte... positions) {
        this(state,
             isFixed,
             leftMin,
             rightMax,
             topMin,
             bottomMax,
             new int[] { positions.length },
             new char[] { color },
             positions);
    }

    private JellyImpl(final State state) {
        this.board = state.getBoard();
        this.height1 = (byte) (board.getHeight() - 1);
        this.width1 = (byte) (board.getWidth() - 1);
    }

    private JellyImpl(final State state,
                      final boolean isFixed,
                      final byte leftMin,
                      final byte rightMax,
                      final byte topMin,
                      final byte bottomMax,
                      final int[] end,
                      final char[] color,
                      final byte[] positions) {
        this(state);
        this.isFixed = isFixed;
        this.leftMin = leftMin;
        this.rightMax = rightMax;
        this.topMin = topMin;
        this.bottomMax = bottomMax;
        this.positions = Arrays.copyOf(positions, positions.length);
        this.color = Arrays.copyOf(color, color.length);
        this.end = Arrays.copyOf(end, end.length);
    }

    public JellyImpl(final State state, final int i, final int j) {
        this(state);
        topMin = bottomMax = (byte) i;
        leftMin = rightMax = (byte) j;
        final Board board = state.getBoard();
        update(board.getMatrix(), board.getLinks(0), board.getLinks(1), i, j);
        color = Arrays.copyOf(COL_BUF, segmentIndex);
        end = Arrays.copyOf(END_BUF, segmentIndex);
        positions = Arrays.copyOf(POS_BUF, getStart(end, segmentIndex));
    }

    private final void update(final char[][] matrix,
                              final byte[] links0,
                              final byte[] links1,
                              final int si,
                              final int sj) {
        // we have one single candidate segment for now
        CANDIDATE_SEGMENT_BUF[0] = value(si, sj);
        segmentIndex = 0;
        // we don't have any empty segment yet
        freeSegmentIndex = 1;
        emptySegmentNb = 0;
        for (; segmentIndex + emptySegmentNb < freeSegmentIndex;) {
            // the current segment ends where it starts (it is empty)
            END_BUF[segmentIndex] = getStart(END_BUF, segmentIndex);
            // let's init the candidate positions with the current candidate segment
            CANDIDATE_POS_BUF[0] = CANDIDATE_SEGMENT_BUF[segmentIndex];
            for (int index = 0, freeIndex = 1; index < freeIndex; index++) {
                final byte pos = CANDIDATE_POS_BUF[index];
                final byte i = (byte) getI(pos);
                final byte j = (byte) getJ(pos);
                final char c = matrix[i][j];
                if (c != Board.BLANK_CHAR) {
                    // let's store the color of the current segment if not yet done
                    if (isEmpty(END_BUF, segmentIndex)) {
                        COL_BUF[segmentIndex] = BoardImpl.toFloating(c);
                    }
                    // has to be true because we want to treat the current segment only here
                    if (BoardImpl.toFloating(c) == COL_BUF[segmentIndex]) {
                        matrix[i][j] = Board.BLANK_CHAR;
                        isFixed |= BoardImpl.isFixed(c);
                        insertPositionInSortedSegment(pos);
                        handleLinkedPosition(links0, links1, pos);
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
                        if (i < height1) {
                            CANDIDATE_POS_BUF[freeIndex++] = (byte) (pos + Board.DOWN);
                        }
                        if (j > 0) {
                            CANDIDATE_POS_BUF[freeIndex++] = (byte) (pos + Board.LEFT);
                        }
                        if (j < width1) {
                            CANDIDATE_POS_BUF[freeIndex++] = (byte) (pos + Board.RIGHT);
                        }
                    }
                }
            }
            if (isEmpty(END_BUF, segmentIndex)) {
                emptySegmentNb++;
            } else {
                segmentIndex++;
            }
        }
    }

    /**
     * Let's insert pos in POS_BUF while keeping the segment sorted.
     */
    private final void insertPositionInSortedSegment(final byte pos) {
        final int end = END_BUF[segmentIndex];
        final int insertionPoint = -1 - Arrays.binarySearch(POS_BUF, getStart(END_BUF, segmentIndex), end, pos);
        for (int i = end; i > insertionPoint; i--) {
            POS_BUF[i] = POS_BUF[i - 1];
        }
        POS_BUF[insertionPoint] = pos;
        END_BUF[segmentIndex]++;
    }

    private final void handleLinkedPosition(final byte[] links0, final byte[] links1, final byte pos) {
        final int idx = contains(links0, 0, links0.length, pos);
        if (idx >= 0) {
            final byte linkedPos = links1[idx];
            if (contains(POS_BUF, 0, END_BUF[segmentIndex], linkedPos) < 0
                && contains(CANDIDATE_SEGMENT_BUF, 0, freeSegmentIndex, linkedPos) < 0) {
                CANDIDATE_SEGMENT_BUF[freeSegmentIndex++] = linkedPos;
            }
        }
    }

    @Override
    public final JellyImpl clone(final State state) {
        return new JellyImpl(state, isFixed, leftMin, rightMax, topMin, bottomMax, end, color, positions);
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
        return !isFixed && rightMax != width1;
    }

    @Override
    public final void moveRight() {
        move(Board.RIGHT);
        leftMin++;
        rightMax++;
    }

    @Override
    public final boolean mayMoveDown() {
        return !isFixed && bottomMax != height1;
    }

    @Override
    public final void moveDown() {
        move(Board.DOWN);
        topMin++;
        bottomMax++;
    }

    @Override
    public final void moveUp() {
        move(Board.UP);
        topMin--;
        bottomMax--;
    }

    final static int getI(final int pos) {
        return (pos >> Board.MAX_COORDINATE_LOG2) & Board.COORDINATE_MASK;
    }

    final static int getJ(final int pos) {
        return pos & Board.COORDINATE_MASK;
    }

    final static byte value(final int i, final int j) {
        return (byte) ((i << Board.MAX_COORDINATE_LOG2) | j);
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
        final boolean[][] walls = board.getWalls();
        for (final byte position : positions) {
            if (walls[getI(position)][getJ(position)]) {
                return true;
            }
        }
        return false;
    }

    @Override
    public final void updateBoard() {
        final char[][] matrix = board.getMatrix();
        final int size = end.length;
        if (size == 1) {
            updateBoard(matrix, 0, end[0], color[0]);
        } else {
            int ls = getStart(end, size - 1);
            int le;
            for (int i = 0; i < size; i++) {
                le = getStart(end, i);
                updateBoard(matrix, le, end[i], color[i]);
                board.storeLink(positions[ls], positions[le]);
                ls = le;
            }
        }
    }

    private final void updateBoard(final char[][] matrix, final int start, final int end, char c) {
        c = isFixed ? BoardImpl.toFixed(c) : c;
        for (int j = start; j < end; j++) {
            final byte position = positions[j];
            matrix[getI(position)][getJ(position)] = c;
        }
    }

    private final static boolean isEmpty(final int[] end, final int segmentIndex) {
        return getStart(end, segmentIndex) == end[segmentIndex];
    }

    private final static int getStart(final int[] end, final int index) {
        return index == 0 ? 0 : end[index - 1];
    }

    private final static int contains(final byte[] tab, final int from, final int to, final byte val) {
        for (int i = from; i < to; i++) {
            if (tab[i] == val) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public final int getSegmentNb() {
        return color.length;
    }
}
