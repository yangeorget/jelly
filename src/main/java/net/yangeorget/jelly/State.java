package net.yangeorget.jelly;


public interface State {
    State clone();

    Jelly[] getJellies();

    Board getBoard();

    int getDistinctColorsNb();

    String moveLeft(final int j);

    String moveRight(final int j);
}
