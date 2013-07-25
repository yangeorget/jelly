package net.yangeorget.jelly;


public interface Jelly {
    void move(int vec);

    boolean mayMove(final int vec);

    boolean overlaps(Jelly j);

    boolean overlapsWalls();

    Jelly clone();

    void updateBoard();

    int getSegmentNb();

    byte getStart(int segmentIndex);

    byte getEnd(int segmentIndex);

    byte getColor(int segmentIndex);

    byte getPosition(int index);

    int getPositionsNb();

    byte getEmergingColor(int epIndex);

    byte getEmergingPosition(int epIndex);

    int getNotEmergedNb();

    int getEpIndex(byte ep);

    void markAsEmerged(int epIndex);

}
