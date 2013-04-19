package net.yangeorget.jelly;

import java.util.List;

public interface State {
    State clone();

    List<Jelly> getJellies();

    Board toBoard();

    int getDistinctColorsNb();

    State move(int j, int move);

    boolean slide(final Jelly jelly, final int move);

    void gravity();

    boolean gravity(Jelly jelly);
}
