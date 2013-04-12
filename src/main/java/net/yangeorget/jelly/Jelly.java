package net.yangeorget.jelly;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class Jelly {
	private Set<Position> positions;
	
	public Jelly() {
		this(Collections.<Position> emptySet());
	}
	
	public Jelly(Collection<Position> col) {
		positions = new HashSet<Position>();
		for (Position position : col) {
			positions.add(new Position(position));
		}
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
	
	public int size() {
		return positions.size();
	}
	
	public boolean update(int di, int dj, int height, int width) {
		for (Position position : positions) {
			if (!position.move(di, dj, height, width)) {
				return false;
			}
		}
		return true;
	}

	public boolean overlaps(Jelly j) {
		if (size() > j.size()) {
			return j.overlaps(this);
		}
		for (Position p : positions) {
			System.out.println("checking " + p + " with " + j.positions);
			if (j.positions.contains(p)) {
				System.out.println(this + " overlaps " + j);
				return true;
			}
		}
		System.out.println(this + " does not overlaps " + j);
		return false;
	}
}
