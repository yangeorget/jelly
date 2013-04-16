package net.yangeorget.jelly;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameImpl implements Game {
	private static final Logger LOG = LoggerFactory.getLogger(GameImpl.class);
	private int width;
	private int height;
	private int distinctColorsNb;
	private LinkedList<State> states;
	private Set<State> explored;

	public GameImpl(String[] board) {
		this(Boards.toCharMatrix(board));
	}

	public GameImpl(char[][] board) {
		explored = new HashSet<State>();
		states = new LinkedList<State>();
		StateImpl state = new StateImpl(board);
		LOG.debug(state.toString());
		states.add(state);
		Set<Character> colors = new HashSet<Character>();
		colors.addAll(state.getFloatingJellies().keySet());
		colors.addAll(state.getFixedJellies().keySet());
		distinctColorsNb = colors.size();
		width = Boards.getWidth(board);
		height = Boards.getHeight(board);
	}

	public List<State> getStates() {
		return states;
	}
	
	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public boolean solve() {
		for(State state = states.removeFirst(); explored.add(state);) {
			Map<Character, List<Jelly>> map = state.getFloatingJellies();
			for (Character color : map.keySet()) {
				for (int i = map.get(color).size() - 1; i>=0; i--) {
					for (int di = -1; di <= 1; di += 2) {
						if (check(state.move(color, i, 0, di, height, width))) {
							return true;
						}
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

	public String toString() {
		return "distinctColorsNb=" + distinctColorsNb + ";states=" + states + ";explored=" + explored;
	}
}
