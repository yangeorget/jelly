package net.yangeorget.jelly;


public interface Jelly {
    boolean moveLeft();

    boolean moveRight();

    boolean moveDown();

    boolean overlaps(Jelly j);

    Jelly clone();

    boolean isFixed();

    char getColor();

    void updateBoard(Board board);

    int getHeight();

    int getWidth();
}
