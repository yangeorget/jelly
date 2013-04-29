package net.yangeorget.jelly;

import java.util.List;

public interface State {
    State clone();

    List<Jelly> getJellies();

    Jelly getJelly(int index);

    Board toBoard();

    int getDistinctColorsNb();

    Board move(final Jelly jelly, final int move);
}
