package net.yangeorget.jelly;

import java.util.List;


public interface Board {
    Board[] LEVELS = { new BoardImpl("            ",
                                     "            ",
                                     "            ",
                                     "            ",
                                     "       P    ",
                                     "      **    ",
                                     "  G     P B ",
                                     "+B---G @@@@@"),

                      new BoardImpl("            ",
                                    "            ",
                                    "            ",
                                    "            ",
                                    "            ",
                                    "     Y   Y  ",
                                    "   R R   R  ",
                                    "&&&& + = ///"),

                      new BoardImpl("            ",
                                    "            ",
                                    "            ",
                                    "            ",
                                    "   BY  $ Y  ",
                                    "== +++R$$$  ",
                                    "      B     ",
                                    "&& ###R@@@@@"),
                      new BoardImpl("            ",
                                    "       R    ",
                                    "       B    ",
                                    "       _    ",
                                    " B R        ",
                                    " B R      B ",
                                    "&& &      ##",
                                    "&&&& #######"),
                      new BoardImpl("            ",
                                    "            ",
                                    "            ",
                                    "RG  GG      ",
                                    "++ ==== --  ",
                                    "RG          ",
                                    "&&&&  ##   (",
                                    "&&&&& ##  (("),
                      new BoardImpl("@@@@@@      ",
                                    "@@@@@@ G    ",
                                    "       ??   ",
                                    " R   B      ",
                                    " + === - G  ",
                                    "         & B",
                                    "       R &&&",
                                    "   &&&&&&&&&") };

    int MAX_WIDTH = 16;
    int MAX_HEIGHT = 16;

    char get(int i, int j);

    int getHeight();

    int getWidth();

    List<Jelly> getJellies();

    boolean cellIsFixed(int i, int j);

    boolean cellHasColor(int i, int j, char c);

    void clear();

    void set(byte i, byte j, char color);
}
