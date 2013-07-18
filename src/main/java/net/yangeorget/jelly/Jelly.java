package net.yangeorget.jelly;


public interface Jelly {
    void moveLeft();

    void moveRight();

    void moveDown();

    void moveUp();

    boolean mayMoveLeft();

    boolean mayMoveRight();

    boolean mayMoveDown();

    boolean mayMoveUp();

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

    boolean allEmerged();

    int getEpIndex(byte ep);

    void markAsEmerged(int epIndex);

}
