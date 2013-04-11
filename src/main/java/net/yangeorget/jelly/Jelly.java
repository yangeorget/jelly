package net.yangeorget.jelly;

import java.util.*;

public class Jelly {
	private boolean fixed;
	private char color;
	private Set<Position> positions; // could be optimized by storing increments between positions

	public Jelly(int i, int j, char[][] board) {
		this.color = Character.toUpperCase(board[i][j]);
		this.positions = new LinkedHashSet<Position>();
		update(i, j, board);
	}
	
	public boolean isFixed() {
		return fixed;
	}

	public char getColor() {
		return color;
	}

	public Set<Position> getPositions() {
		return positions;
	}
	
	private void update(int i, int j, char[][] board) {
		if (color == Character.toUpperCase(board[i][j])) {
			fixed |= Character.isLowerCase(board[i][j]);
			board[i][j] = 0;
			positions.add(new Position(i, j));
			if (i > 0) {
				update(i - 1, j, board);
			}
			if (i < Boards.getHeight(board) - 1) {
				update(i + 1, j, board);
			}
			if (j > 0) {
				update(i, j - 1, board);
			}
			if (j < Boards.getWidth(board) - 1) {
				update(i, j + 1, board);
			}
		}		
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		toString(builder);
		return builder.toString();
	}

	private void toString(StringBuilder builder) {
		builder.append(color);
		builder.append(";");
		builder.append(fixed);
		builder.append(";");
		builder.append(positions.toString());		
	}
}
