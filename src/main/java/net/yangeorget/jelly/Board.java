package net.yangeorget.jelly;

import java.util.List;


public interface Board
        extends Frame {
    // TODO: use different w's or use a reserved char and a counter
    Board LEVEL_1 = new BoardImpl("            ",
                                 "            ",
                                 "            ",
                                 "            ",
                                 "       P    ",
                                 "      ww    ",
                                 "  G     P B ",
                                 "wBwwwG wwwww");

    char get(final int i, final int j);

    List<Jelly> getJellies();
}
