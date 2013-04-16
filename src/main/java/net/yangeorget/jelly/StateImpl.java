package net.yangeorget.jelly;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StateImpl implements State {
	private Map<Character, List<Jelly>> floatingJellies;
	private Map<Character, List<Jelly>> fixedJellies;

	public StateImpl() {
		floatingJellies = new HashMap<Character, List<Jelly>>();
		fixedJellies = new HashMap<Character, List<Jelly>>();
	}

	public StateImpl(char[][] board) {
		this();
		for (int i = 0; i < Boards.getHeight(board); i++) {
			for (int j = 0; j < Boards.getWidth(board); j++) {
				Character color = Character.toUpperCase(board[i][j]);
				if (color != 0 && !Character.isWhitespace(color)) {
					Jelly jelly = new JellyImpl();
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

	@Override
	public State clone() {
		State state = new StateImpl();
		for (Character color : fixedJellies.keySet()) {
			for(Jelly jelly : fixedJellies.get(color)) {
				store(state.getFixedJellies(), color, jelly.clone());
			}			
		}
		for (Character color : floatingJellies.keySet()) {
			for(Jelly jelly : floatingJellies.get(color)) {
				store(state.getFloatingJellies(), color, jelly.clone());
			}	
		}
		return state;
	}

	private static void store(Map<Character, List<Jelly>> map, Character color, Jelly jelly) {
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

	@Override
	public Map<Character, List<Jelly>> getFloatingJellies() {
		return floatingJellies;
	}

	@Override
	public Map<Character, List<Jelly>> getFixedJellies() {
		return fixedJellies;
	}

	@Override
	public State move(Character color, int index, int di, int dj, int height, int width) {
		State state = clone();
		boolean moved = state.move(new HashSet<Jelly>(), state.getFloatingJellies().get(color).get(index), di, dj, height, width);
		if (moved) {
			return state;
		} else {
			return null;
		}
	}

	@Override
	public boolean move(Set<Jelly> moved, Jelly jelly, int di, int dj, int height, int width) {
		if (!jelly.update(di, dj, height, width)) {
			return false;			
		} 
		moved.add(jelly);
		for (Character c : fixedJellies.keySet()) {
			for (Jelly j : fixedJellies.get(c)) {
				if (jelly.overlaps(j)) {					
					return false;
				}
			}
		}
		for (Character c : floatingJellies.keySet()) {
			for (Jelly j : floatingJellies.get(c)) {
				if (!moved.contains(j) && jelly.overlaps(j) && !move(moved, j, di, dj, height, width)) {
					return false;				
				}
			}
		}
		return true;
	}

	public String toString() {
		return "fixed=" + fixedJellies + ";nonFixed=" + floatingJellies;
	}

	@Override
	public char[][] toBoard(int height, int width) {
		char[][] board = new char[height][width];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				board[i][j] = ' ';
			}
		}
		for (Character color : fixedJellies.keySet()) {
			for(Jelly jelly : fixedJellies.get(color)) {
				jelly.updateBoard(board, color);
			}			
		}
		for (Character color : floatingJellies.keySet()) {
			for(Jelly jelly : floatingJellies.get(color)) {
				jelly.updateBoard(board, color);
			}	
		}
		return board;		
	}
}
