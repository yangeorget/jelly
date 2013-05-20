package net.yangeorget.jelly;


public interface Board {
    int MAX_WIDTH = 16;
    int MAX_HEIGHT = 16;
    char FIXED_FLAG = (char) 32;
    char BLANK_CHAR = ' ';
    char A_CHAR = 'A';

    Board[] LEVELS = { // board 1
            new BoardImpl("            ",
                          "            ",
                          "            ",
                          "            ",
                          "       P    ",
                          "      00    ",
                          "  G     P B ",
                          "1B222G 33333"),
            // board 2
            new BoardImpl("            ",
                          "            ",
                          "            ",
                          "            ",
                          "            ",
                          "     Y   Y  ",
                          "   R R   R  ",
                          "0000 1 2 333"),
            // board 3
            new BoardImpl("            ",
                          "            ",
                          "            ",
                          "            ",
                          "   BY  0 Y  ",
                          "11 222R000  ",
                          "      B     ",
                          "33 444R55555"),
            // board 4
            new BoardImpl("            ",
                          "       R    ",
                          "       B    ",
                          "       0    ",
                          " B R        ",
                          " B R      B ",
                          "11 1      22",
                          "1111 2222222"),
            // board 5
            new BoardImpl("            ",
                          "            ",
                          "            ",
                          "RG  GG      ",
                          "00 1111 22  ",
                          "RG          ",
                          "3333  44   5",
                          "33333 44  55"),
            // board 6
            new BoardImpl("000000      ",
                          "000000 G    ",
                          "       11   ",
                          " R   B      ",
                          " 2 333 4 G  ",
                          "         5 B",
                          "       R 555",
                          "   555555555"),
            // board 7
            new BoardImpl("            ",
                          "          P ",
                          "          0 ",
                          "     B   B  ",
                          "     1  PP  ",
                          "         2  ",
                          " p  b4 5 2  ",
                          " 3  44 5 2  "),
            // board 8
            new BoardImpl("000 1  2 000",
                          "00  G  B  00",
                          "0   3  4   0",
                          "0   b  g   0",
                          "0G        B0",
                          "00G      B00",
                          "000      000",
                          "000000000000"),
            // board 9
            new BoardImpl("            ",
                          "            ",
                          "            ",
                          "            ",
                          "          RB",
                          "    0     11",
                          "B        DD1",
                          "2  r3  4 111"),
            // board 10
            new BoardImpl("   GR       ",
                          "   DD B     ",
                          "    0 1 2222",
                          "            ",
                          "  3  4      ",
                          "        5  r",
                          "6   7     g8",
                          "          88"),
            null,
            null,
            null,
            null,
            null,
            null,
    // board 17
    /*
     * new BoardImpl("000NNN111GB ", "000N     BG ", "000N    DD22", "000NNN222222", " FFF  222222", "333     22g2",
     * "333   G    b", "333   4     ")
     */};

    char[][] getMatrix();

    int getHeight();

    int getWidth();

    Jelly[] extractJellies();

    Jelly[] getWalls();

    Board clone();

    void apply(Jelly[] jellies);

    String serialize();
}
