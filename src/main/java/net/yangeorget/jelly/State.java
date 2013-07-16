package net.yangeorget.jelly;


public interface State
        extends JellyCounters {
    Serializer SERIALIZER = new SerializerCountImpl();

    State clone();

    Jelly[] getJellies();

    Board getBoard();

    boolean moveLeft(final int j);

    boolean moveRight(final int j);

    void undoMoveRight();

    void undoMoveLeft();

    void process();

    byte[] getSerialization();

    void clearSerialization();

    void updateFromBoard();

    void updateBoard();

    void explain(int step);

    boolean[] getEmerged();

    boolean isSolved();
}
