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

    Jelly clone();

    char getColor();

    void updateBoard(Board board);
}
