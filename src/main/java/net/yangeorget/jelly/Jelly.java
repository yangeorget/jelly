package net.yangeorget.jelly;

import java.util.Set;

public interface Jelly {
    void store(int i, int j);

    boolean contains(Position p);

    boolean hMove(int move);

    boolean vMove(int move);

    boolean overlaps(Jelly j);

    boolean adjacentTo(Jelly j);

    void merge(Jelly j);

    void restore(Jelly j);

    Jelly clone();

    int size();

    Set<Position> getPositions();


}
