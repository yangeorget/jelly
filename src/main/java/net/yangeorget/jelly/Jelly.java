package net.yangeorget.jelly;


public interface Jelly {
    boolean hMove(int move);

    boolean vMove(int move);

    boolean overlaps(Jelly j);

    Jelly clone();

    boolean isFixed();

    char getColor();

    void updateBoard(Board board);
}
