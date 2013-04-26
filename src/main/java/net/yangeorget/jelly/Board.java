package net.yangeorget.jelly;

import java.util.List;


public interface Board {
    Board LEVEL_1 = new BoardImpl("            ",
                                  "            ",
                                  "            ",
                                  "            ",
                                  "       P    ",
                                  "      **    ",
                                  "  G     P B ",
                                  "+B---G @@@@@");

    Board LEVEL_2 = new BoardImpl("            ",
                                  "            ",
                                  "            ",
                                  "            ",
                                  "            ",
                                  "     Y   Y  ",
                                  "   R R   R  ",
                                  "&&&& + = ///");

    Board LEVEL_3 = new BoardImpl("            ",
                                  "            ",
                                  "            ",
                                  "            ",
                                  "   BY  $ Y  ",
                                  "== +++R$$$  ",
                                  "      B     ",
                                  "&& ###R@@@@@");

    Board LEVEL_4 = new BoardImpl("            ",
                                  "       R    ",
                                  "       B    ",
                                  "       _    ",
                                  " B R        ",
                                  " B R      B ",
                                  "&& &      ##",
                                  "&&&& #######");

    char get(int i, int j);

    int getHeight();

    int getWidth();

    List<Jelly> getJellies();

    boolean cellIsFixed(int i, int j);

    boolean cellHasColor(int i, int j, char c);
}
