package net.yangeorget.jelly;

import java.util.Set;

public interface Jelly {
    void store(int i, int j);

    boolean contains(Position p);

    boolean hMove(int move, int width);

    boolean vMove(int move, int height);

    boolean overlaps(Jelly j);

    boolean adjacentTo(Jelly j, int height, int width);

    void updateBoard(char[][] board, Character color);

    Jelly clone();

    void restore(Jelly j);

    int size();

    Set<Position> getPositions();

    void merge(Jelly je);

}
