package net.yangeorget.jelly;

import java.util.*;

public class Jelly {
	boolean fixed;
	char color;
	Set<Character>  positions; // could be optimized by storing increments between positions

	public Jelly(boolean fixed, char color) {
		this.fixed = fixed;
		this.color = color;
		this.positions = new LinkedHashSet<Character>();
	}
	
	public boolean isFixed() {
		return fixed;
	}
	
	public char getColor() {
		return color;
	}
	
	public Set<Character> getPositions() {
		return positions;
	}

	public void update(int i, int j, char color, char[][] board) {
		if (Boards.sameColors(color, i, j, board)) {
			board[i][j] = 0;
			positions.add(Boards.getPosition(i, j, board));
			if (i > 0) {
				update(i - 1, j, color, board);
			}
			if (i < Boards.getHeight(board) - 1) {
				update(i + 1, j, color, board);
			}
			if (j > 0) {
				update(i, j - 1, color, board);
			}
			if (j < Boards.getWidth(board) - 1) {
				update(i, j + 1, color, board);
			}
		}		
	}
	
	public String toString(int height, int width) {
		StringBuilder builder = new StringBuilder();
		toString(builder, height, width);
		return builder.toString();
	}
	
	private void toString(StringBuilder builder, int height, int width) {
		builder.append(color);
		builder.append(";");
		builder.append(fixed);
		builder.append(";");
		for (char position: positions) {
			builder.append(Boards.positionToString(position, height, width));
			builder.append(" ");
		}
	}
}
