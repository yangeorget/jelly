package net.yangeorget.jelly;

import java.util.List;

public interface State {
    State clone();

    List<Jelly> getJellies();

    Board toBoard();

    Frame getFrame();

    int getDistinctColorsNb();

    State move(int j, int move);
}
