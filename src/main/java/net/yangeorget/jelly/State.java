package net.yangeorget.jelly;

import java.util.List;
import java.util.Map;

public interface State {
	State clone();

	Map<Character, List<Jelly>> getFloatingJellies();

	Map<Character, List<Jelly>> getFixedJellies();

	State move(Character color, int index, int di, int dj, int height, int width);

	boolean move(Jelly jelly, int di, int dj, int height, int width);

	char[][] toBoard(int height, int width);
}