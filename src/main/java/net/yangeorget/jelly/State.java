package net.yangeorget.jelly;


public interface State {
    State clone();

    Jelly[] getJellies();

    Board getBoard();

    int getDistinctColorsNb();

    boolean moveLeft(final int j);

    boolean moveRight(final int j);

    void gravity();

    String getSerialization();
}
