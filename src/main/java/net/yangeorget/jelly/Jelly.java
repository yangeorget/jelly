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

    char getColor(int segmentIndex);

    byte[] getPositions();

    char getEmergingColor(int epIndex);

    byte getEmergingPosition(int epIndex);

    int getEmergingPositionNb();

    int getEmergingIndex(byte ep);

    void markAsEmerged(int epIndex);
}
