package net.yangeorget.jelly;


public interface Board {
    int MAX_WIDTH = 16;
    int MAX_HEIGHT = 16;
    char FIXED_FLAG = (char) 32;
    char BLANK_CHAR = ' ';
    char WALL_CHAR = '#';

    char[][] getMatrix();

    int getHeight();

    int getWidth();

    Jelly[] extractJellies();

    boolean[][] getWalls();

    void apply(Jelly[] jellies);

    String serialize();

    int getJellyColorNb();

    Board[] LEVELS = { // board 1
            new BoardImpl("            ",
                          "            ",
                          "            ",
                          "            ",
                          "       P    ",
                          "      ##    ",
                          "  G     P B ",
                          "#B###G #####"),
            // board 2
            new BoardImpl("            ",
                          "            ",
                          "            ",
                          "            ",
                          "            ",
                          "     Y   Y  ",
                          "   R R   R  ",
                          "#### # # ###"),
            // board 3
            new BoardImpl("            ",
                          "            ",
                          "            ",
                          "            ",
                          "   BY  # Y  ",
                          "## ###R###  ",
                          "      B     ",
                          "## ###R#####"),
            // board 4
            new BoardImpl("            ",
                          "       R    ",
                          "       B    ",
                          "       #    ",
                          " B R        ",
                          " B R      B ",
                          "## #      ##",
                          "#### #######"),
            // board 5
            new BoardImpl("            ",
                          "            ",
                          "            ",
                          "RG  GG      ",
                          "## #### ##  ",
                          "RG          ",
                          "####  ##   #",
                          "##### ##  ##"),
            // board 6
            new BoardImpl("######      ",
                          "###### G    ",
                          "       ##   ",
                          " R   B      ",
                          " # ### # G  ",
                          "         # B",
                          "       R ###",
                          "   #########"),
            // board 7
            new BoardImpl("            ",
                          "          P ",
                          "          # ",
                          "     B   B  ",
                          "     #  PP  ",
                          "         #  ",
                          " p  b# # #  ",
                          " #  ## # #  "),
            // board 8
            new BoardImpl("### #  # ###",
                          "##  G  B  ##",
                          "#   #  #   #",
                          "#   b  g   #",
                          "#G        B#",
                          "##G      B##",
                          "###      ###",
                          "############"),
            // board 9
            new BoardImpl("            ",
                          "            ",
                          "            ",
                          "            ",
                          "          RB",
                          "    #     ##",
                          "B        DD#",
                          "#  r#  # ###"),
            // board 10
            new BoardImpl("   GR       ",
                          "   DD B     ",
                          "    # # ####",
                          "            ",
                          "  #  #      ",
                          "        #  r",
                          "#   #     g#",
                          "          ##"),
            null,
            null,
            null,
            null,
            null,
            null,
            // board 17
            new BoardImpl("###NNN###GB ",
                          "###N     BG ",
                          "###N    DD##",
                          "###NNN######",
                          " FFF  ######",
                          "###     ##g#",
                          "###   G    b",
                          "###   #     ") };

}
