package net.yangeorget.jelly;


public interface State {
    State clone();

    Jelly[] getJellies();

    Board getBoard();

    boolean moveLeft(final int j);

    boolean moveRight(final int j);

    void undoMoveRight();

    void undoMoveLeft();

    void gravity();

    String getSerialization();

    boolean isSolved();

    void updateFromBoard();

    void updateBoard();

    void explain(int step);
}
