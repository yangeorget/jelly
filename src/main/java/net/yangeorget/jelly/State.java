package net.yangeorget.jelly;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface State {
	State clone();

	Map<Character, List<Jelly>> getFloatingJellies();

	Map<Character, List<Jelly>> getFixedJellies();

	State move(Character color, int index, int di, int dj,
			int height, int width);

	boolean move(Set<Jelly> moved, Jelly jelly, int di, int dj,
			int height, int width);

	char[][] toBoard(int height, int width);
}