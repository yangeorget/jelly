package net.yangeorget.jelly;


public interface Board {
    int MAX_EMERGING = 32;
    int COORDINATE_MASK = 0xF;
    int MAX_COORDINATE = 16;
    int MAX_COORDINATE_LOG2 = 4;
    int MAX_WIDTH = MAX_COORDINATE;
    int MAX_HEIGHT = MAX_COORDINATE;
    int MAX_SIZE = MAX_WIDTH * MAX_HEIGHT;

    int LEFT = -1;
    int RIGHT = 1;
    int UP = -MAX_WIDTH;
    int DOWN = MAX_WIDTH;

    char FIXED_FLAG = (char) 32;
    char BLANK_CHAR = ' ';
    char WALL_CHAR = '#';

    int getHeight();

    int getWidth();

    int getHeight1();

    int getWidth1();

    char[][] getMatrix();

    boolean[][] getWalls();

    byte[] getLinkStarts();

    byte[] getLinkEnds();

    char[] getEmergingColors();

    byte[] getEmergingPositions();

    int getJellyColorNb();

    void clearLinks();

    void updateLinks();

    void storeLink(byte start, byte end);

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
                                        "   ###### ##" }, new byte[] { 6, 8, 9 }),
            // board 12
            new BoardImpl(new String[] { "#R RR  RR R#",
                                        "##  #  #  ##",
                                        "            ",
                                        "B          b",
                                        "#          #",
                                        "            ",
                                        "            ",
                                        "   ######   " }),
            // board 13
            new BoardImpl(new String[] { "############",
                                        "############",
                                        "#### YR ####",
                                        "#### RB ####",
                                        "#### YR ####",
                                        "#### BY ####",
                                        "############",
                                        "############" }),
            // board 14
            new BoardImpl(new String[] { "########   R",
                                        "########   G",
                                        "########   G",
                                        "DDNN       G",
                                        "DDNN       G",
                                        "BBFF      ##",
                                        "BBFF      ##",
                                        "#r # g### ##" }),
            // board 15
            new BoardImpl(new String[] { "R R R      R",
                                        "G # #      G",
                                        "b          B",
                                        "####     ###",
                                        "#####   ####",
                                        "#####   ####",
                                        "#####ggg####",
                                        "#####   ####" }),
            // board 16
            new BoardImpl(new String[] { "#   AAABCDDR",
                                        "#   AEBBCDD#",
                                        "#   EEEBCC##",
                                        "#     ######",
                                        "r     ######",
                                        "#     ######",
                                        "#     ######",
                                        "#     ######" }),
            // board 17
            new BoardImpl(new String[] { "###NNN###GB ",
                                        "###N     BG ",
                                        "###N    DD##",
                                        "###NNN######",
                                        " FFF  ######",
                                        "###     ##g#",
                                        "###   G    b",
                                        "###   #     " }),
            // board 18
            new BoardImpl(new String[] { "            ",
                                        "BNF         ",
                                        "BNYY     Y  ",
                                        "BNDS     YMb",
                                        "#### Y   ###",
                                        "#### YY  ###",
                                        "#### YYY ###",
                                        "#### YYYY###" }),
            // board 19
            new BoardImpl(new String[] { "GB    GDG#  ",
                                        " NG    D #  ",
                                        "FFF    S #  ",
                                        "G G   GGG   ",
                                        "##     ###  ",
                                        "##     ###  ",
                                        "##     ###  ",
                                        "##          " },
                          new byte[] { 0x00, 0x01 },
                          new byte[] { 0x06, 0x07, 0x08 },
                          new byte[] { 0x11, 0x12 },
                          new byte[] { 0x20, 0x30, 0x32 },
                          new byte[] { 0x27, 0x37 }),
            // board 20
            new BoardImpl(new String[] { "RRRR   RGG##",
                                        "##B    #####",
                                        "###       #b",
                                        "#           ",
                                        "#           ",
                                        "#     #     ",
                                        "# #         ",
                                        "#        #  " }),

            // board 21
            new BoardImpl(new String[] { "      #     ",
                                        "      #     ",
                                        "      #     ",
                                        "      g     ",
                                        "        GB  ",
                                        "###     ##  ",
                                        "##R B     R ",
                                        "### ########" }, new byte[] { 0x66 }, new char[] { 'R' }),

            // board 22
            new BoardImpl(new String[] { "            ",
                                        "            ",
                                        "            ",
                                        "            ",
                                        "    G  BGR  ",
                                        " # ##  ### #",
                                        "B#          ",
                                        "############" }, new byte[] { 0x65 }, new char[] { 'r' }),

            // board 23
            new BoardImpl(new String[] { "            ",
                                        "            ",
                                        "    G       ",
                                        "    B       ",
                                        "    #    R  ",
                                        "        ##  ",
                                        " B          ",
                                        "### R ### #G" }, new byte[] { 0x67 }, new char[] { 'R' }),

            // board 24
            new BoardImpl(new String[] { "G   B     ##",
                                        "R   G     ##",
                                        "y   B Y   Y#",
                                        "#   # #   ##",
                                        "###       ##",
                                        "###       ##",
                                        "##### ######",
                                        "#####g######" }, new byte[] { 0x53, 0x58 }, new char[] { 'G', 'r' }),

            // board 25
            new BoardImpl(new String[] { "#######  #  ",
                                        "#######  r  ",
                                        "#######     ",
                                        "####     R  ",
                                        "#BBB    DDD ",
                                        " BBB    DDD ",
                                        " G        # ",
                                        "############" }, new byte[] { 0x63, 0x66, 0x69 }, new char[] { 'G', 'G', 'G' }),

            // board 26
            new BoardImpl(new String[] { "#        ###",
                                        "#  R     ###",
                                        "#BBBBBBBB###",
                                        "#     R   ##",
                                        "#DDDDDDDD ##",
                                        "#  R      ##",
                                        "#FFFFFFFF###",
                                        "#     R  ###" }, new byte[] { 0x16, 0x33, 0x56, 0x73 }, new char[] { 'R',
                                                                                                             'R',
                                                                                                             'R',
                                                                                                             'R' }) };
}
