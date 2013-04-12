package net.yangeorget.jelly;

public class Position {
	public static final int LEFT = -1;
	public static final int RIGHT = -1;
	public static final int UP = -16;
	public static final int DOWN = 16;
	
	private int value; // TODO: use a char?

	public Position(int i, int j) {
		this((i << 4) + j);
	}

	public Position(int value) {
		this.value = value;
	}
	public String toString() {
		return "(" + (value >> 4) + "," + (value & 0xF) + ")";
	}

	public int intValue() {
		return value;
	}

	public void move(Position vec) {
		value += vec.intValue();
	}
}

