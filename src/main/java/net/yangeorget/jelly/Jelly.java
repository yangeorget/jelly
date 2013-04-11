package net.yangeorget.jelly;

import java.util.LinkedHashSet;
import java.util.Set;

public class Jelly {
	private Set<Position> positions;
	
	public Jelly() {
		positions = new LinkedHashSet<Position>();
	}	
	
	public void store(int i, int j) {
		positions.add(new Position(i, j));
	}
	
	public String toString() {
		return positions.toString();
	}
}
