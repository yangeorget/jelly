package net.yangeorget.jelly;

import org.testng.Assert;

public class Boards {
	// TODO: use different w's or use a reserved char and a counter  
	public static String[] BOARD1 = new String[] {
		"            ",
		"            ",
		"            ",
		"            ",
		"       P    ",
		"      ww    ",
		"  G     P B ",
		"wBwwwG wwwww"
	};
	
	public static int getHeight(char[][] board) {
		return board.length;
	}

	public static int getWidth(char[][] board) {
		return board[0].length;
	}

	public static String toString(char[][] board) {
		StringBuilder builder = new StringBuilder();
		toString(builder, board);
		return builder.toString();
	}

	private static void toString(StringBuilder builder, char[][] board) {
		for (int i = 0; i < getHeight(board); i++) {
			for (int j = 0; j < getWidth(board); j++) {
				builder.append(board[i][j]);
			}
			if (i < getHeight(board) -1) {
				builder.append("\n");
			}
		}
	}
	
	public static char[][] toCharMatrix(String[] board) {
		int height = board.length;
		char[][] m = new char[height][];
		for (int i = 0; i < height; i++) {
			m[i] = board[i].toCharArray();
		}
		return m;
	}

	public static void assertEquals(char[][] b1, char[][] b2) {
		if (b1.length != b2.length) {
			Assert.fail();
		}
		for (int i = 0; i < b1.length; i++) {
			Assert.assertEquals(b1[i], b2[i]);
		}
	}
}
