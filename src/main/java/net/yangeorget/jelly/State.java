package net.yangeorget.jelly;

import java.util.LinkedList;
import java.util.List;

public class State {
	int width;
	int height;
	List<Jelly> jellies;

	public State(String[] board) {
		this(Boards.toCharMatrix(board));
	}
	
	public State(char[][] board) {
		width = Boards.getWidth(board);
		height = Boards.getHeight(board);
		jellies = new LinkedList<Jelly>();
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (Boards.isColored(i, j, board)) {
					Jelly jelly = getJelly(i, j, board);
					System.out.println(jelly.toString(height, width));
					jellies.add(jelly);
				}	
			}
		}
	}
	
	private Jelly getJelly(int i, int j, char[][] board) {
		char color = board[i][j];
		Jelly jelly = new Jelly(false, color);
		jelly.update(i, j, color, board);
		return jelly;
	}

	public List<Jelly> getJellies() {
		return jellies;
	}
}
