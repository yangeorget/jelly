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

    private static final byte[] CANDIDATE_SEGMENT_BUF = new byte[Board.MAX_WIDTH * Board.MAX_HEIGHT];
    private static final byte[] CANDIDATE_POS_BUF = new byte[Board.MAX_WIDTH * Board.MAX_HEIGHT];
    private static final byte[] POS_BUF = new byte[Board.MAX_WIDTH * Board.MAX_HEIGHT];
    private static final char[] COL_BUF = new char[Board.MAX_WIDTH * Board.MAX_HEIGHT];
    private static final int[] END_BUF = new int[Board.MAX_WIDTH * Board.MAX_HEIGHT];

    final byte[] positions;
    final char[] color;
    final int[] end;
    final State state;
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

    public JellyImpl(final State state,
                     final boolean isFixed,
                     final byte leftMin,
                     final byte rightMax,
                     final byte topMin,
                     final byte bottomMax,
                     final int[] end,
                     final char[] color,
                     final byte[] positions) {
        this.state = state;
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
        this.state = state;
        topMin = bottomMax = (byte) i;
        leftMin = rightMax = (byte) j;
        final Board board = state.getBoard();
        final int freeSegmentIndex = update(board.getMatrix(),
                                            board.getLinks()[0],
                                            board.getLinks()[1],
                                            board.getHeight() - 1,
                                            board.getWidth() - 1,
                                            i,
                                            j);
        color = Arrays.copyOf(COL_BUF, freeSegmentIndex);
        end = Arrays.copyOf(END_BUF, freeSegmentIndex);
        positions = Arrays.copyOf(POS_BUF, getStart(end, freeSegmentIndex));
    }

    private int update(final char[][] matrix,
                       final byte[] links0,
                       final byte[] links1,
                       final int height1,
                       final int width1,
                       final int si,
                       final int sj) {
        // TODO: avoid empty segments and test
        // TODO: test complex links (loops)
        CANDIDATE_SEGMENT_BUF[0] = value(si, sj);
        int segmentIndex = 0;
        for (int freeSegmentIndex = 1; segmentIndex < freeSegmentIndex; segmentIndex++) {
            END_BUF[segmentIndex] = getStart(END_BUF, segmentIndex);
            CANDIDATE_POS_BUF[0] = CANDIDATE_SEGMENT_BUF[segmentIndex];
            for (int index = 0, freeIndex = 1; index < freeIndex; index++) {
                final byte pos = CANDIDATE_POS_BUF[index];
                final byte i = (byte) getI(pos);
                final byte j = (byte) getJ(pos);
                final char c = matrix[i][j];
                if (c != Board.BLANK_CHAR) {
                    if (isEmpty(END_BUF, segmentIndex)) {
                        COL_BUF[segmentIndex] = BoardImpl.toFloating(c);
                    }
                    if (BoardImpl.toFloating(c) == COL_BUF[segmentIndex]) {
                        matrix[i][j] = Board.BLANK_CHAR;
                        isFixed |= BoardImpl.isFixed(c);
                        insertInSortedSegment(segmentIndex, pos);
                        final int idx = Arrays.binarySearch(links0, pos);
                        if (idx >= 0) {
                            final byte linkedPos = links1[idx];
                            if (Arrays.binarySearch(POS_BUF, 0, freeIndex, linkedPos) < 0
                                && !contains(CANDIDATE_SEGMENT_BUF, 0, freeSegmentIndex, linkedPos)) {
                                CANDIDATE_SEGMENT_BUF[freeSegmentIndex++] = linkedPos;
                            }
                        }
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
        }
        return segmentIndex;
    }

    @Override
    public JellyImpl clone(final State state) {
        return new JellyImpl(state, isFixed, leftMin, rightMax, topMin, bottomMax, end, color, positions);
    }

    @Override
    public String toString() {
        return "positions="
               + Arrays.toString(positions)
               + ";color="
               + Arrays.toString(color)
               + ";end="
               + Arrays.toString(end);
    }

    private void move(final int vec) {
        for (int index = positions.length; --index >= 0;) {
            positions[index] += vec;
        }
    }

    @Override
    public boolean mayMoveLeft() {
        return !isFixed && leftMin != 0;
    }

    @Override
    public void moveLeft() {
        move(Board.LEFT);
        leftMin--;
        rightMax--;
    }

    @Override
    public boolean mayMoveRight() {
        return !isFixed && rightMax != state.getBoard()
                                            .getWidth() - 1;
    }

    @Override
    public void moveRight() {
        move(Board.RIGHT);
        leftMin++;
        rightMax++;
    }

    @Override
    public boolean mayMoveDown() {
        return !isFixed && bottomMax != state.getBoard()
                                             .getHeight() - 1;
    }

    @Override
    public void moveDown() {
        move(Board.DOWN);
        topMin++;
        bottomMax++;
    }

    @Override
    public void moveUp() {
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
    public boolean overlaps(final Jelly jelly) {
        final JellyImpl j = (JellyImpl) jelly;
        if (j.getRightMax() < leftMin
            || rightMax < j.getLeftMin()
            || j.getBottomMax() < topMin
            || bottomMax < j.getTopMin()) {
            return false;
        }
        final byte[] jPositions = j.positions;
        final int size = positions.length;
        final int jSize = jPositions.length;
        int index = 0;
        int jIndex = 0;
        while (true) {
            final byte jPosition = jPositions[jIndex];
            while (positions[index] < jPosition) {
                if (++index == size) {
                    return false;
                }
            }
            final byte position = positions[index];
            if (position == jPosition) {
                return true;
            }
            while (position > jPositions[jIndex]) {
                if (++jIndex == jSize) {
                    return false;
                }
            }
        }
    }

    @Override
    public boolean overlapsWalls() {
        final boolean[][] walls = state.getBoard()
                                       .getWalls();
        for (final byte position : positions) {
            if (walls[getI(position)][getJ(position)]) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int updateBoard(int index) {
        final Board board = state.getBoard();
        final char[][] matrix = board.getMatrix();
        final byte[][] links = board.getLinks();
        for (int i = 0; i < end.length; i++) {
            if (i == 0) {
                updateBoard(matrix, 0, end[0], color[0]);
            } else {
                final int start = getStart(end, i);
                storeLink(links, index, positions[start - 1], positions[start]);
                index += 2;
                updateBoard(matrix, start, end[i], color[i]);
            }
        }
        return index;
    }

    private void storeLink(final byte[][] links, final int index, final byte start, final byte end) {
        links[0][index] = links[1][index + 1] = start;
        links[0][index + 1] = links[1][index] = end;
    }

    private void updateBoard(final char[][] matrix, final int start, final int end, char c) {
        c = isFixed ? BoardImpl.toFixed(c) : c;
        for (int j = start; j < end; j++) {
            final byte position = positions[j];
            matrix[getI(position)][getJ(position)] = c;
        }
    }

    private boolean isEmpty(final int[] end, final int segmentIndex) {
        return getStart(end, segmentIndex) == end[segmentIndex];
    }

    private int getStart(final int[] end, final int index) {
        return index == 0 ? 0 : end[index - 1];
    }

    /**
     * Let's insert pos in POS_BUF while keeping the segment sorted.
     */
    private void insertInSortedSegment(final int segmentIndex, final byte pos) {
        final int end = END_BUF[segmentIndex];
        final int insertionPoint = -1 - Arrays.binarySearch(POS_BUF, getStart(END_BUF, segmentIndex), end, pos);
        POS_BUF[end] = POS_BUF[insertionPoint];
        POS_BUF[insertionPoint] = pos;
        END_BUF[segmentIndex]++;
    }

    private boolean contains(final byte[] positions, final int from, final int to, final byte pos) {
        for (int i = from; i < to; i++) {
            if (positions[i] == pos) {
                return true;
            }
        }
        return false;
    }

    byte getLeftMin() {
        return leftMin;
    }

    byte getTopMin() {
        return topMin;
    }

    byte getRightMax() {
        return rightMax;
    }

    byte getBottomMax() {
        return bottomMax;
    }
}
