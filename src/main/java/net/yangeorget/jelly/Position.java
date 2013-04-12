package net.yangeorget.jelly;

public class Position implements Comparable<Position> {
	private int value; // TODO: use a char?

	public Position(int i, int j) {
		this(intValue(i, j));
	}

	public Position(Position p) {
		this(p.getValue());
	}

	public Position(int value) {
		this.value = value;
	}

	public String toString() {
		return "(" + getI() + "," + getJ() + ")";
	}

	int getI() {
		return value >> 4;
	}

	int getJ() {
		return value & 0xF;
	}

	public int getValue() {
		return value;
	}

	private static int intValue(int i, int j) {
		return (i << 4) + j;
	}

	public boolean move(int di, int dj, int height, int width) {
		int i = getI() + di;
		if (i < 0 || i >= height) {
			return false;
		}
		int j = getJ() + dj;
		if (j < 0 || j >= width) {
			return false;
		}
		value = intValue(i, j);
		return true;
	}

	@Override
	public int compareTo(Position o) {
		return value - o.getValue();				
	}
}

