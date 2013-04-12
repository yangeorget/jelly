package net.yangeorget.jelly;

import java.util.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Game {
	private int width;
	private int height;
	private int distinctColorsNb;
	private LinkedList<State> states;
	private Set<State> explored;

	public Game(String[] board) {
		this(toCharMatrix(board));
	}

	public Game(char[][] board) {
		explored = new HashSet<State>();
		states = new LinkedList<State>();
		State state = new State(board);
		states.add(state);
		Set<Character> colors = new HashSet<Character>();
		colors.addAll(state.getFloatingJellies().keySet());
		colors.addAll(state.getFixedJellies().keySet());
		distinctColorsNb = colors.size();
		width = Boards.getWidth(board);
		height = Boards.getHeight(board);
	}

	public boolean solve() {
		for(State state = states.removeFirst(); explored.add(state);) {
			Map<Character, List<Jelly>> map = state.getFloatingJellies();
			for (Character color : map.keySet()) {
				for (int i = map.get(color).size() - 1; i>=0; i--) {
					if (check(state.move(color, i, 0, -1, height, width))) {
						return true;
					}
					if (check(state.move(color, i, 0, 1, height, width))) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean check(State state) {
		if (state != null && !explored.contains(state)) {			
			if (state.getFloatingJellies().size() + state.getFixedJellies().size() == distinctColorsNb) {
				return true;
			} 
			states.addLast(state);			
		}
		return false;
	}

	private static char[][] toCharMatrix(String[] a) {
		// TODO: set border here if needed
		int height = a.length;
		char[][] m = new char[height][];
		for (int i = 0; i < height; i++) {
			m[i] = a[i].toCharArray();
		}
		return m;
	}

	public List<State> getStates() {
		return states;
	}
	
	public String toString() {
		return "distinctColorsNb=" + distinctColorsNb + ";states=" + states + ";explored=" + explored;
	}
}
