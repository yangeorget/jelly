package net.yangeorget.jelly;

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
}
