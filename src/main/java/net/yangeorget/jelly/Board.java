package net.yangeorget.jelly;

import java.util.List;


public interface Board
        extends Frame {
    Board LEVEL_1 = new BoardImpl("            ",
                                  "            ",
                                  "            ",
                                  "            ",
                                  "       P    ",
                                  "      **    ",
                                  "  G     P B ",
                                  "+B---G @@@@@");

    char get(int i, int j);

    char[][] getMatrix();

    List<Jelly> getJellies();

    boolean cellIsBlank(int i, int j);

    boolean cellIsFixed(int i, int j);

    boolean cellHasColor(int i, int j, char c);

    void toString(final StringBuilder builder, final boolean nl);
}
