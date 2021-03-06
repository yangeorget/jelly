package net.yangeorget.jelly;

/**
 * @author y.georget
 */
public interface PuzzleData {
    /**
     * The levels as defined in Jelly No Puzzle.
     */
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
                                        "### ########" }, new byte[] { 0x76 }, new char[] { 'R' }),

            // board 22
            new BoardImpl(new String[] { "            ",
                                        "            ",
                                        "            ",
                                        "            ",
                                        "    G  BGR  ",
                                        " # ##  ### #",
                                        "B#          ",
                                        "############" }, new byte[] { 0x75 }, new char[] { 'r' }),

            // board 23
            new BoardImpl(new String[] { "            ",
                                        "            ",
                                        "    G       ",
                                        "    B       ",
                                        "    #    R  ",
                                        "        ##  ",
                                        " B          ",
                                        "### R ### #G" }, new byte[] { 0x77 }, new char[] { 'R' }),

            // board 24
            new BoardImpl(new String[] { "G   B     ##",
                                        "R   G     ##",
                                        "y   B Y   Y#",
                                        "#   # #   ##",
                                        "###       ##",
                                        "###       ##",
                                        "##### ######",
                                        "#####g######" }, new byte[] { 0x63, 0x68 }, new char[] { 'G', 'r' }),

            // board 25
            new BoardImpl(new String[] { "#######  #  ",
                                        "#######  r  ",
                                        "#######     ",
                                        "####     R  ",
                                        "#BBB    DDD ",
                                        " BBB    DDD ",
                                        " G        # ",
                                        "############" }, new byte[] { 0x73, 0x76, 0x79 }, new char[] { 'G', 'G', 'G' }),

            // board 26
            new BoardImpl(new String[] { "#        ###",
                                        "#  R     ###",
                                        "#BBBBBBBB###",
                                        "#     R   ##",
                                        "#DDDDDDDD ##",
                                        "#  R      ##",
                                        "#FFFFFFFF###",
                                        "#     R  ###",
                                        "############" }, // extra wall line (for containing an emerging jelly)
                          new byte[] { 0x26, 0x43, 0x66, (byte) 0x83 },
                          new char[] { 'R', 'R', 'R', 'R' }),

            // board 27
            new BoardImpl(new String[] { "####g#y#r###",
                                        "####     ###",
                                        "BYGD     R  ",
                                        "####     ## ",
                                        "####FFFFF## ",
                                        "####FFFFF # ",
                                        "### FFFFFb# ",
                                        "###   B     ",
                                        "############" }, new byte[] { (byte) 0x85, (byte) 0x87 }, new char[] { 'B',
                                                                                                               'B' }),

            // board 28
            new BoardImpl(new String[] { "### #  # ###",
                                        "## GB  GB ##",
                                        "#  ##  ##  #",
                                        "#   b  g   #",
                                        "#          #",
                                        "##        ##",
                                        "###G    B###",
                                        "############" }, new byte[] { 0x74, 0x77 }, new char[] { 'b', 'g' }),

            // board 29
            new BoardImpl(new String[] { "## YYRR ####",
                                        "## YYRR ####",
                                        "#  BBGG  ###",
                                        "#  BBGG  ###",
                                        "#  GGBB  ###",
                                        "#  GGBB  ###",
                                        "## RRYY ####",
                                        "## RRYY ####" }),

            // board 30
            new BoardImpl(new String[] { " " }),// TODO: jelly can emerge in any direction!

            // board 31
            new BoardImpl(new String[] { " " }),

            // board 32
            new BoardImpl(new String[] { "G   Y   #RDB",
                                        "F   M    YSG",
                                        "B   ROO    #",
                                        "#   ###   ##",
                                        "           #",
                                        "       ##  #",
                                        "#          #",
                                        "##        ##" },
                          new byte[] { 0, 16, 32 },
                          new byte[] { 4, 20, 36 },
                          new byte[] { 9, 10, 11 },
                          new byte[] { 25, 26, 27 }),

            // board 33
            new BoardImpl(new String[] { " " }),

            // board 34
            new BoardImpl(new String[] { " " }),

            // board 35
            new BoardImpl(new String[] { "DD    BBBBBR",
                                        "DB        BY",
                                        "DD        BY",
                                        "##YYY     BY",
                                        "#rFBF     ##",
                                        "# FFF     ##",
                                        "####      ##",
                                        "#######   ##" }, new byte[] { 1, 17 }, new byte[] { 66, 67 }),

            // board 36
            new BoardImpl(new String[] { "    BRGRBG  ",
                                        "  ##FFFFFF##",
                                        "  ##FYFFRF##",
                                        "    FFFFDD  ",
                                        "    FFDDDD  ",
                                        "    DDDDDD  ",
                                        "    DDDDDD  ",
                                        "    DDDDDD  ",
                                        "############" }, new byte[] { (byte) 0x83 }, new char[] { 'R' }),

            // board 37
            new BoardImpl(new String[] { " " }),

            // board 38
            new BoardImpl(new String[] { "ggg##YBR    ",
                                        "   ##BYB    ",
                                        "GGG#######  ",
                                        "FFF##       ",
                                        "#F###       ",
                                        "#      ## ##",
                                        "#       ####",
                                        "#### #######" },
                          new byte[] { 0x71 },
                          new char[] { 'R' },
                          new byte[] { 32, 48 }),


            // board 39
            new BoardImpl(new String[] { "####    ####",
                                        "###  FFFF###",
                                        "####    ####",
                                        "#R#      #G#",
                                        "#B        B#",
                                        "YR        GY",
                                        "###      ###",
                                        "#### ## ####" }, new byte[] { 0x62, 0x69 }, new char[] { 'G', 'R' }),

            // board 40
            new BoardImpl(new String[] { "      R R R ",
                                        "#FY#### # # ",
                                        "#FFr  # # R ",
                                        "#RY Y # # # ",
                                        "#DD # # # R ",
                                        "#DD       # ",
                                        "#D          ",
                                        "###     #   " }, new byte[] { 0x72 }, new char[] { 'Y' }), };
}
