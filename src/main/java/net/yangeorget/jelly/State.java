package net.yangeorget.jelly;

import java.util.List;

public interface State {
    State clone();

    List<Jelly> getJellies();

    Board toBoard();

    Frame getFrame();

    int getDistinctColorsNb();

    boolean move(final Jelly jelly, final int move);

    String serialize();
}
