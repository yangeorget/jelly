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

    int getStart(int segmentIndex);

    int getEnd(int segmentIndex);

    byte getColor(int segmentIndex);

    byte[] getPositions();

    byte getEmergingColor(int epIndex);

    byte getEmergingPosition(int epIndex);

    int getNotEmergedNb();

    int getEpIndex(byte ep);

    void markAsEmerged(int epIndex);
}
