package net.yangeorget.jelly;

import java.util.List;
import java.util.Map;

public interface State {
    State clone();

    Map<Character, List<Jelly>> getFloatingJellies();

    Map<Character, List<Jelly>> getFixedJellies();

    State move(Character color, int index, int move, int height, int width);

    boolean moveHorizontally(final Jelly jelly, final int move, final int width);

    char[][] toBoard(int height, int width);

    boolean moveDown(Jelly jelly, int height);

    void moveDown(int height);
}
