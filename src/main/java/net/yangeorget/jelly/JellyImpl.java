package net.yangeorget.jelly;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public class JellyImpl implements Jelly {
	private Set<Position> positions;
	
	public JellyImpl() {
		this(Collections.<Position> emptySet());
	}
	
	public JellyImpl(Collection<Position> col) {
		positions = new TreeSet<Position>();
		for (Position position : col) {
			positions.add(new Position(position));
		}
	}
	
	public Jelly clone() {
		return new JellyImpl(positions);
	}

	@Override
	public void store(int i, int j) {
		positions.add(new Position(i, j));
	}
	
	public String toString() {
		return positions.toString();
	}
	
	@Override
    public int size() {
		return positions.size();
	}
	
	@Override
	public boolean contains(Position p) {
		return positions.contains(p);
	}

	@Override
	public boolean update(int di, int dj, int height, int width) {
		for (Position position : positions) {
			if (!position.move(di, dj, height, width)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean overlaps(Jelly j) {
		if (size() > j.size()) {
			return j.overlaps(this);
		}
		for (Position p : positions) {
			if (j.contains(p)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void updateBoard(char[][] board, Character color) {
		for (Position position : positions) {
			board[position.getI()][position.getJ()] = color;
		}
	}
}
