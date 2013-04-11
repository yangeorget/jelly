package net.yangeorget.jelly;

import java.util.LinkedList;
import java.util.List;

public class State {
	private List<Jelly> jellies;
	
	public State(char[][] board) {
		jellies = new LinkedList<Jelly>();
		for (int i = 0; i < Boards.getHeight(board); i++) {
			for (int j = 0; j < Boards.getWidth(board); j++) {
				if (Boards.isColored(i, j, board)) {
					Jelly jelly = new Jelly(i, j, board);
					System.out.println(jelly.toString());
					jellies.add(jelly);
				}	
			}
		}
	}

	public List<Jelly> getJellies() {
		return jellies;
	}
}
