package net.yangeorget.jelly;

import java.util.List;
import java.util.Map;

public interface State {
    State clone();

    Map<Character, List<Jelly>> getFloatingJellies();

    Map<Character, List<Jelly>> getFixedJellies();

    State slide(Character color, int index, int move, int height, int width);

    boolean slide(final Jelly jelly, final int move, final int width);

    char[][] toBoard(int height, int width);

    void gravity(int height, int width);

    boolean gravity(Jelly jelly, int height);

    void join(int height, int width);
}
