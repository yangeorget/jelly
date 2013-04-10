package net.yangeorget.jelly;

public class Boards {
	public static String[] BOARD1 = new String[] {
		"            ",
		"            ",
		"            ",
		"            ",
		"       P    ",
		"      WW    ",
		"  G     P B ",
		"WBWWWG WWWWW"
	};
	
	public static int getHeight(char[][] board) {
		return board.length;
	}

	public static int getWidth(char[][] board) {
		return board[0].length;
	}

	public static char[][] toCharMatrix(String[] a) {
		// TODO: set border here if needed
		int height = a.length;
		char[][] m = new char[height][];
		for (int i = 0; i < height; i++) {
			m[i] = a[i].toCharArray();
		}
		return m;
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

	public static boolean isColored(int i, int j, char[][] board) {
		char c = board[i][j];
		return c != 0 && !Character.isWhitespace(c);
	}
	
	public static boolean sameColors(char color, int i, int j, char[][] board) {
		return board[i][j] == color;
	}
	
	public static char getPosition(int i, int j, char[][] board) {
		return (char) (j + i * getWidth(board));
	}

	public static String positionToString(char position, int height, int width) {
		return "(" + (position / width) + "," + (position % height) + ")";
	}
}
