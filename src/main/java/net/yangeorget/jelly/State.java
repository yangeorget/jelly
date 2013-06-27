package net.yangeorget.jelly;


public interface State {
    State clone();

    Jelly[] getJellies();

    Board getBoard();

    boolean moveLeft(final int j);

    boolean moveRight(final int j);

    void undoMoveRight();

    void undoMoveLeft();

    void process();

    StringBuilder getSerialization();

    void clearSerialization();

    boolean isSolved();

    void updateFromBoard();

    void updateBoard();

    void explain(int step);
}
