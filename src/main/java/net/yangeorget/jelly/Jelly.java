package net.yangeorget.jelly;

public interface Jelly {
    void store(int i, int j);

    boolean contains(Position p);

    boolean moveHorizontally(int move, int width);

    boolean overlaps(Jelly j);

    void updateBoard(char[][] board, Character color);

    Jelly clone();

    int size();

}
