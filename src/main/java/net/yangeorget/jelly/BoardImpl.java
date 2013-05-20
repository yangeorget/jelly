package net.yangeorget.jelly;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class BoardImpl
        implements Board {
    private final char[][] matrix;
    private final int height;
    private final int width;
    private final Jelly[] jelliesBuffer;
    private Jelly[] walls;
    private int jellyColorNb;

    private BoardImpl(final int height, final int width) {
        this.height = height;
        this.width = width;
        jelliesBuffer = new Jelly[height * width];
        matrix = new char[height][];
    }

    public BoardImpl(final State state) {
        this(state.getBoard());
        apply(state.getJellies());
    }

    private BoardImpl(final Board board) {
        this(board.getHeight(), board.getWidth());
        for (int i = 0; i < height; i++) {
            matrix[i] = new char[width];
            System.arraycopy(board.getMatrix()[i], 0, matrix[i], 0, width);
        }
        final Jelly[] boardWalls = board.getWalls();
        final int wallsSize = boardWalls.length;
        walls = new Jelly[wallsSize];
        System.arraycopy(boardWalls, 0, walls, 0, wallsSize);
        computeJellyColorNb();
    }

    public BoardImpl(final String... strings) {
        this(strings.length, strings[0].length());
        for (int i = 0; i < height; i++) {
            matrix[i] = strings[i].toCharArray();
        }
        int wallsSize = 0;
        for (byte i = 0; i < height; i++) {
            for (byte j = 0; j < width; j++) {
                final char color = matrix[i][j];
                if (color != BLANK_CHAR && color < A_CHAR) {
                    jelliesBuffer[wallsSize++] = new JellyImpl(this, color, i, j);
                }
            }
        }
        walls = new Jelly[wallsSize];
        System.arraycopy(jelliesBuffer, 0, walls, 0, wallsSize);
        computeJellyColorNb();
    }

    private void computeJellyColorNb() {
        final Set<Character> colors = new HashSet<>();
        for (byte i = 0; i < height; i++) {
            for (byte j = 0; j < width; j++) {
                final char color = matrix[i][j];
                if (color != Board.BLANK_CHAR) {
                    colors.add(BoardImpl.toFloating(color));
                }
            }
        }
        jellyColorNb = colors.size();
    }

    @Override
    public int getJellyColorNb() {
        return jellyColorNb;
    }

    @Override
    public Board clone() {
        return new BoardImpl(this);
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        toString(builder);
        return builder.toString();
    }

    private void toString(final StringBuilder builder) {
        final int height1 = height - 1;
        for (int i = 0; i < height1; i++) {
            builder.append(matrix[i]);
            builder.append('\n');
        }
        builder.append(matrix[height1]);
    }

    @Override
    public String serialize() {
        final StringBuilder builder = new StringBuilder();
        serialize(builder);
        String serialization = builder.toString();
        int i = 0;
        while (serialization.charAt(i) == Board.BLANK_CHAR) {
            i++;
        }
        serialization = serialization.substring(i);
        return serialization;
    }

    private void serialize(final StringBuilder builder) {
        for (int i = 0; i < height; i++) {
            builder.append(matrix[i]);
        }
    }

    @Override
    public boolean equals(final Object o) {
        return Arrays.deepEquals(matrix, ((Board) o).getMatrix());
    }

    @Override
    public Jelly[] getWalls() {
        return walls;
    }

    @Override
    public Jelly[] extractJellies() {
        int nb = 0;
        for (byte i = 0; i < height; i++) {
            for (byte j = 0; j < width; j++) {
                final char color = matrix[i][j];
                if (color >= A_CHAR) {
                    jelliesBuffer[nb++] = new JellyImpl(this, color, i, j);
                }
            }
        }
        final Jelly[] jellies = new Jelly[nb];
        System.arraycopy(jelliesBuffer, 0, jellies, 0, nb);
        return jellies;
    }

    @Override
    public void apply(final Jelly[] jellies) {
        for (final Jelly jelly : jellies) {
            jelly.updateBoard(this);
        }
    }

    public static boolean isFixed(final char c) {
        return (c & FIXED_FLAG) != 0;
    }

    public static char toFloating(final char c) {
        return c < A_CHAR ? c : (char) (c & ~FIXED_FLAG);
    }

    @Override
    public char[][] getMatrix() {
        return matrix;
    }
}
