package net.yangeorget.jelly;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class State {
	private Map<Character, LinkedList<Jelly>> floatingJellies;
	private Map<Character, LinkedList<Jelly>> fixedJellies;

	public State() {
		floatingJellies = new HashMap<Character, LinkedList<Jelly>>();
		fixedJellies = new HashMap<Character, LinkedList<Jelly>>();
	}

	public State(char[][] board) {
		this();
		for (int i = 0; i < Boards.getHeight(board); i++) {
			for (int j = 0; j < Boards.getWidth(board); j++) {
				Character color = Character.toUpperCase(board[i][j]);
				if (color != 0 && !Character.isWhitespace(color)) {
					Jelly jelly = new Jelly();
					boolean fixed = update(jelly, color, i, j, board);
					if (fixed) {
						store(fixedJellies, color, jelly);
					} else {
						store(floatingJellies, color, jelly);
					}
				}	
			}
		}
	}


	private void store(Map<Character, LinkedList<Jelly>> map,
					   Character color, 
					   Jelly jelly) {
		LinkedList<Jelly> list = map.get(color);
		if (list == null) {
			list = new LinkedList<Jelly>();
			map.put(color, list);
		}
		list.add(jelly);

	}

	private boolean update(Jelly jelly, char color, int i, int j, char[][] board) {
		boolean fixed = false;
		if (color == Character.toUpperCase(board[i][j])) {
			fixed |= Character.isLowerCase(board[i][j]);
			board[i][j] = 0;
			jelly.store(i, j);
			if (i > 0) {
				update(jelly, color, i - 1, j, board);
			}
			if (i < Boards.getHeight(board) - 1) {
				update(jelly, color, i + 1, j, board);
			}
			if (j > 0) {
				update(jelly, color, i, j - 1, board);
			}
			if (j < Boards.getWidth(board) - 1) {
				update(jelly, color, i, j + 1, board);
			}
		}		
		return fixed;
	}

	public Map<Character, LinkedList<Jelly>> getFloatingJellies() {
		return floatingJellies;
	}
	
	public Map<Character, LinkedList<Jelly>> getFixedJellies() {
		return fixedJellies;
	}

	public State moveLeft(Jelly jelly) {
		return null;
	}

	public State moveRight(Jelly jelly) {
		return null;
	}
	
	public String toString() {
		return "fixed=" + fixedJellies + ";nonFixed=" + floatingJellies;
	}
}
