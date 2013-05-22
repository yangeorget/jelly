package net.yangeorget.jelly;

import java.util.HashSet;
import java.util.Set;


public class BoardImpl
        implements Board {
    private final int height;
    private final int width;
    private final char[][] matrix;
    private boolean[][] walls;
    private int jellyColorNb;
    private final Jelly[] jelliesBuffer;

    private BoardImpl(final int height, final int width) {
        this.height = height;
        this.width = width;
        jelliesBuffer = new Jelly[height * width];
        matrix = new char[height][width];
    }

    private BoardImpl(final Board board) {
        this(board.getHeight(), board.getWidth());
        walls = board.getWalls();
        for (byte i = 0; i < height; i++) {
            for (byte j = 0; j < width; j++) {
                matrix[i][j] = walls[i][j] ? Board.WALL_CHAR : Board.BLANK_CHAR;
            }
        }
    }

    public BoardImpl(final State state) {
        this(state.getBoard());
        apply(state.getJellies());
        computeJellyColorNb();
    }

    public BoardImpl(final String... strings) {
        this(strings.length, strings[0].length());
        for (int i = 0; i < height; i++) {
            matrix[i] = strings[i].toCharArray();
        }
        computeWalls();
        computeJellyColorNb();
    }

    private void computeWalls() {
        walls = new boolean[height][width];
        for (byte i = 0; i < height; i++) {
            for (byte j = 0; j < width; j++) {
                walls[i][j] = matrix[i][j] == WALL_CHAR;
            }
        }
    }

    private void computeJellyColorNb() {
        final Set<Character> colors = new HashSet<>();
        for (byte i = 0; i < height; i++) {
            for (byte j = 0; j < width; j++) {
                final char color = matrix[i][j];
                if (color != Board.BLANK_CHAR && color != Board.WALL_CHAR) {
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
    public boolean[][] getWalls() {
        return walls;
    }

    @Override
    public Jelly[] extractJellies() {
        int nb = 0;
        for (byte i = 0; i < height; i++) {
            for (byte j = 0; j < width; j++) {
                final char color = matrix[i][j];
                if (color != BLANK_CHAR && color != WALL_CHAR) {
                    final Jelly jelly = new JellyImpl(this, i, j);
                    jelliesBuffer[nb++] = jelly;
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
        return (char) (c & ~FIXED_FLAG);
    }

    @Override
    public char[][] getMatrix() {
        return matrix;
    }
}
