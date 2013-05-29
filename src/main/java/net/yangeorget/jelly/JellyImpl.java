package net.yangeorget.jelly;

import java.util.Arrays;

/**
 * @author y.georget
 */
public class JellyImpl
        implements Jelly {
    private static final byte[] CANDIDATE_SEGMENT_BUFFER = new byte[Board.MAX_WIDTH * Board.MAX_HEIGHT];
    private static final byte[] CANDIDATE_POSITIONS_BUFFER = new byte[Board.MAX_WIDTH * Board.MAX_HEIGHT];
    private static final byte[] POSITIONS_BUFFER = new byte[Board.MAX_WIDTH * Board.MAX_HEIGHT];
    private static final char[] COLOR_BUFFER = new char[Board.MAX_WIDTH * Board.MAX_HEIGHT];
    private static final int[] END_BUFFER = new int[Board.MAX_WIDTH * Board.MAX_HEIGHT];

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
        CANDIDATE_SEGMENT_BUFFER[0] = value(i, j);
        final int freeSegmentIndex = update();
        color = Arrays.copyOf(COLOR_BUFFER, freeSegmentIndex);
        end = Arrays.copyOf(END_BUFFER, freeSegmentIndex);
        positions = Arrays.copyOf(POSITIONS_BUFFER, getEnd(freeSegmentIndex - 1));
        Arrays.sort(positions);
    }

    private int update() {
        int segmentIndex = 0;
        for (final int freeSegmentIndex = 1; segmentIndex < freeSegmentIndex; segmentIndex++) {
            END_BUFFER[segmentIndex] = 0;
            CANDIDATE_POSITIONS_BUFFER[0] = CANDIDATE_SEGMENT_BUFFER[segmentIndex];
            for (int index = 0, freeIndex = 1; index < freeIndex; index++) {
                final int pos = CANDIDATE_POSITIONS_BUFFER[index];
                final byte i = (byte) getI(pos);
                final byte j = (byte) getJ(pos);
                final Board board = state.getBoard(); // TODO : pass these as params
                final char[][] matrix = board.getMatrix();
                final char c = matrix[i][j];
                if (segmentEmpty(segmentIndex)) {
                    COLOR_BUFFER[segmentIndex] = BoardImpl.toFloating(c);
                }
                final byte[][] links = board.getLinks();
                final int idx = Arrays.binarySearch(links[0], (byte) pos);
                if (idx != -1) {
                    // final byte linkedPos = links[1][idx];
                }
                if (BoardImpl.toFloating(c) == COLOR_BUFFER[segmentIndex]) {
                    isFixed |= BoardImpl.isFixed(c);
                    matrix[i][j] = Board.BLANK_CHAR;
                    POSITIONS_BUFFER[END_BUFFER[segmentIndex]++] = (byte) pos;
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
                        CANDIDATE_POSITIONS_BUFFER[freeIndex++] = (byte) (pos + Board.UP);
                    }
                    if (i < board.getHeight() - 1) {
                        CANDIDATE_POSITIONS_BUFFER[freeIndex++] = (byte) (pos + Board.DOWN);
                    }
                    if (j > 0) {
                        CANDIDATE_POSITIONS_BUFFER[freeIndex++] = (byte) (pos + Board.LEFT);
                    }
                    if (j < board.getWidth() - 1) {
                        CANDIDATE_POSITIONS_BUFFER[freeIndex++] = (byte) (pos + Board.RIGHT);
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
        return ";positions="
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
    public void updateBoard() {
        final char[][] matrix = state.getBoard()
                                     .getMatrix();
        for (int i = 0; i < end.length; i++) {
            char c = color[i];
            c = isFixed ? BoardImpl.toFixed(c) : c;
            for (int j = getStart(i); j < getEnd(i); j++) {
                final byte position = positions[j];
                matrix[getI(position)][getJ(position)] = c;
            }
        }
    }

    private int getStart(final int index) {
        return index == 0 ? 0 : end[index - 1];
    }

    private int getEnd(final int index) {
        return end[index];
    }

    private boolean segmentEmpty(final int segmentIndex) {
        if (segmentIndex == 0) {
            return END_BUFFER[0] == 0;
        } else {
            return END_BUFFER[segmentIndex - 1] == END_BUFFER[segmentIndex];
        }
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
