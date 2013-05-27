package net.yangeorget.jelly;


public interface Board {
    int COORDINATE_MASK = 0xF;
    int MAX_COORDINATE = 16;
    int MAX_COORDINATE_LOG2 = 4;
    int MAX_WIDTH = MAX_COORDINATE;
    int MAX_HEIGHT = MAX_COORDINATE;

    byte LEFT = -1;
    byte RIGHT = 1;
    byte UP = -MAX_WIDTH;
    byte DOWN = MAX_WIDTH;

    char FIXED_FLAG = (char) 32;
    char BLANK_CHAR = ' ';
    char WALL_CHAR = '#';

    char[][] getMatrix();

    int getHeight();

    int getWidth();

    boolean[][] getWalls();

    void apply(Jelly[] jellies);

    String serialize();

    int getJellyColorNb();

    Board[] LEVELS = { // board 1
            new BoardImpl(new String[] { "            ",
                                        "            ",
                                        "            ",
                                        "            ",
                                        "       P    ",
                                        "      ##    ",
                                        "  G     P B ",
                                        "#B###G #####" }),
            // board 2
            new BoardImpl(new String[] { "            ",
                                        "            ",
                                        "            ",
                                        "            ",
                                        "            ",
                                        "     Y   Y  ",
                                        "   R R   R  ",
                                        "#### # # ###" }),
            // board 3
            new BoardImpl(new String[] { "            ",
                                        "            ",
                                        "            ",
                                        "            ",
                                        "   BY  # Y  ",
                                        "## ###R###  ",
                                        "      B     ",
                                        "## ###R#####" }),
            // board 4
            new BoardImpl(new String[] { "            ",
                                        "       R    ",
                                        "       B    ",
                                        "       #    ",
                                        " B R        ",
                                        " B R      B ",
                                        "## #      ##",
                                        "#### #######" }),
            // board 5
            new BoardImpl(new String[] { "            ",
                                        "            ",
                                        "            ",
                                        "RG  GG      ",
                                        "## #### ##  ",
                                        "RG          ",
                                        "####  ##   #",
                                        "##### ##  ##" }),
            // board 6
            new BoardImpl(new String[] { "######      ",
                                        "###### G    ",
                                        "       ##   ",
                                        " R   B      ",
                                        " # ### # G  ",
                                        "         # B",
                                        "       R ###",
                                        "   #########" }),
            // board 7
            new BoardImpl(new String[] { "            ",
                                        "          P ",
                                        "          # ",
                                        "     B   B  ",
                                        "     #  PP  ",
                                        "         #  ",
                                        " p  b# # #  ",
                                        " #  ## # #  " }),
            // board 8
            new BoardImpl(new String[] { "### #  # ###",
                                        "##  G  B  ##",
                                        "#   #  #   #",
                                        "#   b  g   #",
                                        "#G        B#",
                                        "##G      B##",
                                        "###      ###",
                                        "############" }),
            // board 9
            new BoardImpl(new String[] { "            ",
                                        "            ",
                                        "            ",
                                        "            ",
                                        "          RB",
                                        "    #     ##",
                                        "B        DD#",
                                        "#  r#  # ###" }),
            // board 10
            new BoardImpl(new String[] { "   GR       ",
                                        "   DD B     ",
                                        "    # # ####",
                                        "            ",
                                        "  #  #      ",
                                        "        #  r",
                                        "#   #     g#",
                                        "          ##" }),
            // board 11
            new BoardImpl(new String[] { "      YDDY Y",
                                        "       ### #",
                                        "           Y",
                                        "BB         #",
                                        "##          ",
                                        "       Y    ",
                                        "   # ###   y",
                                        "   ###### ##" }, new byte[][] { { 6, 7 }, { 8, 9 } }),
            null,
            null,
            null,
            null,
            null,
            null,
    /*
     * // board 17 new BoardImpl("###NNN###GB ", "###N     BG ", "###N    DD##", "###NNN######", " FFF  ######",
     * "###     ##g#", "###   G    b", "###   #     ")
     */

    };
}
