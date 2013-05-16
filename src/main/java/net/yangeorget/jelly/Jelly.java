package net.yangeorget.jelly;


public interface Jelly {
    void moveLeft();

    void moveRight();

    void moveDown();

    boolean mayMoveLeft();

    boolean mayMoveRight();

    boolean mayMoveDown();

    boolean overlaps(Jelly j);

    Jelly clone();

    boolean isFixed();

    char getColor();

    void updateBoard(Board board);

    int getHeight();

    int getWidth();

    byte getLeftMin();

    byte getTopMin();

    byte getRightMax();

    byte getBottomMax();

}
