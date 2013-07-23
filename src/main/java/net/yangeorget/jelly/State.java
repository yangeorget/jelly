package net.yangeorget.jelly;


public interface State
        extends JellyCounters {
    Serializer SERIALIZER = new SerializerRLEImpl();

    State clone();

    Jelly[] getJellies();

    Board getBoard();

    boolean move(final int j, final int vec);

    void undoMove(final int vec);

    void process();

    byte[] getSerialization();

    void clearSerialization();

    void updateFromBoard();

    void updateSerialization();

    void updateBoard();

    void explain(int step);

    boolean[] getEmerged();

    boolean isSolved();
}
