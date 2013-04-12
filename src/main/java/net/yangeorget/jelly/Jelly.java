package net.yangeorget.jelly;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;

public class Jelly {
	private LinkedHashSet<Position> positions;
	
	public Jelly() {
		this(Collections.EMPTY_SET);
	}
	
	public Jelly(Collection<Position> col) {
		positions = new LinkedHashSet<Position>(col);
	}
	
	public Jelly clone() {
		return new Jelly(positions);
	}
	
	public void store(int i, int j) {
		positions.add(new Position(i, j));
	}
	
	public String toString() {
		return positions.toString();
	}
	
	public boolean move(int di, int dj, int height, int width) {
		for (Position position : positions) {
			if (!position.move(di, dj, height, width)) {
				return false;
			}
		}
		return true;
	}
	
}
