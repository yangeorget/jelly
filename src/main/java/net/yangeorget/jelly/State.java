package net.yangeorget.jelly;


public interface State {
    State clone();

    Jelly[] getJellies();

    Board getBoard();

    Board toBoard();

    int getDistinctColorsNb();

    String move(final Jelly jelly, final int move);
}
