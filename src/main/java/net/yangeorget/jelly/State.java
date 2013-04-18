package net.yangeorget.jelly;

import java.util.List;
import java.util.Map;

public interface State {
    State clone();

    Map<Character, List<Jelly>> getFloatingJellies();

    Map<Character, List<Jelly>> getFixedJellies();

    State slide(Character color, int index, int move);

    boolean slide(final Jelly jelly, final int move);

    Board toBoard();

    void gravity();

    boolean gravity(Jelly jelly);

    void join();
}
