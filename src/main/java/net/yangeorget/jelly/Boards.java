package net.yangeorget.jelly;

import org.testng.Assert;

public class Boards {
    // TODO: use different w's or use a reserved char and a counter
    public static String[] BOARD1 = new String[] { "            ",
                                                  "            ",
                                                  "            ",
                                                  "            ",
                                                  "       P    ",
                                                  "      ww    ",
                                                  "  G     P B ",
                                                  "wBwwwG wwwww" };

    public static int getHeight(final char[][] board) {
        return board.length;
    }

    public static int getWidth(final char[][] board) {
        return board[0].length;
    }

    public static String toString(final char[][] board) {
        final StringBuilder builder = new StringBuilder();
        toString(builder, board);
        return builder.toString();
    }

    private static void toString(final StringBuilder builder, final char[][] board) {
        for (int i = 0; i < getHeight(board); i++) {
            for (int j = 0; j < getWidth(board); j++) {
                builder.append(board[i][j]);
            }
            if (i < getHeight(board) - 1) {
                builder.append("\n");
            }
        }
    }

    public static char[][] toCharMatrix(final String[] board) {
        final int height = board.length;
        final char[][] m = new char[height][];
        for (int i = 0; i < height; i++) {
            m[i] = board[i].toCharArray();
        }
        return m;
    }

    public static void assertEquals(final char[][] b1, final char[][] b2) {
        if (b1.length != b2.length) {
            Assert.fail();
        }
        for (int i = 0; i < b1.length; i++) {
            if (b1[i].length != b2[i].length) {
                Assert.fail();
            }
            for (int j = 0; j < b1[i].length; j++) {
                Assert.assertEquals(b1[i][j], b2[i][j]);
            }
        }
    }
}
