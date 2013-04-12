package net.yangeorget.jelly;

import java.util.LinkedHashSet;

public class Jelly {
	private LinkedHashSet<Position> positions;
	
	public Jelly() {
		positions = new LinkedHashSet<Position>();
	}	
	
	public void store(int i, int j) {
		positions.add(new Position(i, j));
	}
	
	public String toString() {
		return positions.toString();
	}
	
	public void move(Position vec) {
		// TODO: check if out of board
		for (Position position : positions) {
			position.move(vec);
		}
	}
}
