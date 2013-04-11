package net.yangeorget.jelly;

import java.util.LinkedList;
import java.util.List;

public class Game {
	private List<State> states;

	public Game(String[] board) {
		this(toCharMatrix(board));
	}
	
	public Game(char[][] board) {
		states = new LinkedList<State>();
		states.add(new State(board));
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
}
