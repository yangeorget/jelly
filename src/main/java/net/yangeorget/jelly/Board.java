package net.yangeorget.jelly;


public interface Board {
    int MAX_WIDTH = 16;
    int MAX_HEIGHT = 16;
    char FIXED_FLAG = (char) 32;
    char BLANK_CHAR = ' ';
    char A_CHAR = 'A';

    Board[] LEVELS = { new BoardImpl("            ",
                                     "            ",
                                     "            ",
                                     "            ",
                                     "       P    ",
                                     "      00    ",
                                     "  G     P B ",
                                     "1B222G 33333"),

                      new BoardImpl("            ",
                                    "            ",
                                    "            ",
                                    "            ",
                                    "            ",
                                    "     Y   Y  ",
                                    "   R R   R  ",
                                    "0000 1 2 333"),

                      new BoardImpl("            ",
                                    "            ",
                                    "            ",
                                    "            ",
                                    "   BY  0 Y  ",
                                    "11 222R000  ",
                                    "      B     ",
                                    "33 444R55555"),
                      new BoardImpl("            ",
                                    "       R    ",
                                    "       B    ",
                                    "       0    ",
                                    " B R        ",
                                    " B R      B ",
                                    "11 1      22",
                                    "1111 2222222"),
                      new BoardImpl("            ",
                                    "            ",
                                    "            ",
                                    "RG  GG      ",
                                    "00 1111 22  ",
                                    "RG          ",
                                    "3333  44   5",
                                    "33333 44  55"),
                      new BoardImpl("000000      ",
                                    "000000 G    ",
                                    "       11   ",
                                    " R   B      ",
                                    " 2 333 4 G  ",
                                    "         5 B",
                                    "       R 555",
                                    "   555555555"),
                      null,
                      null,
                      null,
                      null,
                      null,
                      null,
                      null,
                      null,
                      null,
                      null,
                      new BoardImpl("000NNN111GB ",
                                    "000N     BG ",
                                    "000N    DD22",
                                    "000NNN222222",
                                    " FFF  222222",
                                    "333     22g2",
                                    "333   G    b",
                                    "333   4     ") };

    char[][] getMatrix();

    int getHeight();

    int getWidth();

    Jelly[] extractJellies();

    Jelly[] getWalls();

    Board clone();

    void apply(Jelly[] jellies);
}
