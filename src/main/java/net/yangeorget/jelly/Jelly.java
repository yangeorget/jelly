package net.yangeorget.jelly;


public interface Jelly {
    void moveLeft();

    void moveRight();

    void moveDown();

    void moveUp();

    boolean mayMoveLeft();

    boolean mayMoveRight();

    boolean mayMoveDown();

    boolean overlaps(Jelly j);

    boolean overlaps(boolean[][] walls);

    Jelly clone(State state);

    void updateBoard(Board board);
}
