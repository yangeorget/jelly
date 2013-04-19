package net.yangeorget.jelly;

import java.util.Set;

public interface Jelly {
    boolean contains(Position p);

    boolean hMove(int move);

    boolean vMove(int move);

    boolean overlaps(Jelly j);

    Jelly clone();

    int size();

    Set<Position> getPositions();

    boolean isFixed();

    char getColor();
}
