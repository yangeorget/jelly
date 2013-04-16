package net.yangeorget.jelly;

import java.util.Set;

public interface Jelly {
    void store(int i, int j);

    boolean contains(Position p);

    boolean moveHorizontally(int move, int width);

    boolean overlaps(Jelly j);

    void updateBoard(char[][] board, Character color);

    Jelly clone();

    int size();

    boolean moveDown(int height);

    void restore(Jelly j);

    Set<Position> getPositions();

}
