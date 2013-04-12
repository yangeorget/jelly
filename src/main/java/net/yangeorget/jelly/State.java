package net.yangeorget.jelly;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class State {
	private Map<Character, List<Jelly>> floatingJellies;
	private Map<Character, List<Jelly>> fixedJellies;

	public State() {
		floatingJellies = new HashMap<Character, List<Jelly>>();
		fixedJellies = new HashMap<Character, List<Jelly>>();
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

	public State clone() {
		State state = new State();
		for (Character color : fixedJellies.keySet()) {
			for(Jelly jelly : fixedJellies.get(color)) {
				state.store(state.getFixedJellies(), color, jelly.clone());
			}			
		}
		for (Character color : floatingJellies.keySet()) {
			for(Jelly jelly : floatingJellies.get(color)) {
				state.store(state.getFloatingJellies(), color, jelly.clone());
			}	
		}
		return state;
	}

	private void store(Map<Character, List<Jelly>> map,
					   Character color, 
					   Jelly jelly) {
		List<Jelly> list = map.get(color);
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

	public Map<Character, List<Jelly>> getFloatingJellies() {
		return floatingJellies;
	}
	
	public Map<Character, List<Jelly>> getFixedJellies() {
		return fixedJellies;
	}

	public State move(Character color, int index, int di, int dj, int height, int width) {
		State state = clone();
		return null;
	}
	
	public String toString() {
		return "fixed=" + fixedJellies + ";nonFixed=" + floatingJellies;
	}
}
