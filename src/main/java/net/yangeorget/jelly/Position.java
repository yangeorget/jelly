package net.yangeorget.jelly;

public class Position {
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
}

