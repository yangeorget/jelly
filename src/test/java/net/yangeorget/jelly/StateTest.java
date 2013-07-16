package net.yangeorget.jelly;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = "fast")
public class StateTest {
    private static final Logger LOG = LoggerFactory.getLogger(StateTest.class);

    @Test
    public void testUpdateFromBoard() {
        final Board board = new BoardImpl(new String[] { " BB ", "    " });
        final State state = new StateImpl(board);
        final Object ser = state.getSerialization();
        state.updateBoard();
        state.updateFromBoard();
        Assert.assertEquals(state.getSerialization(), ser);
    }

    @Test
    public void testState1() {
        testState(new BoardImpl(new String[] { "AB" }, new byte[] { 0, 1 }), 2);
    }

    @Test
    public void testState2() {
        testState(new BoardImpl(new String[] { "ABA" }, new byte[] { 0, 1 }), 2, 1);
    }

    @Test
    public void testState3() {
        testState(new BoardImpl(new String[] { "A  ", "ACC", "BB " }, new byte[] { 0x10, 0x20 }), 4, 2);
    }

    @Test
    public void testState4() {
        testState(new BoardImpl(new String[] { " YDDY Y ", "  ### # " }, new byte[] { 1, 3, 4 }), 4, 1);
    }

    @Test
    public void testState5() {
        testState(new BoardImpl(new String[] { "           Y",
                                              "       ### #",
                                              "           Y",
                                              "           #",
                                              "            ",
                                              "      YDDYY " }, new byte[] { 0x56, 0x58, 0x59 }), 1, 1, 5);
    }

    @Test
    public void testState6() {
        testState(new BoardImpl(new String[] { "   ", "   ", "FFF", "G G" }, new byte[] { 34, 48, 50 }), 5);
    }

    private void testState(final Board board, final int... jellyLengths) {
        final Jelly[] jellies = new StateImpl(board).getJellies();
        Assert.assertEquals(jellies.length, jellyLengths.length);
        for (int i = 0; i < jellies.length; i++) {
            Assert.assertEquals(((JellyImpl) jellies[i]).positions.length, jellyLengths[i]);
        }
    }

    @Test
    public void testMoveRightKO1() {
        testMoveRightKO(new BoardImpl(new String[] { " BBBB" }), 0);
    }

    @Test
    public void testMoveRightKO2() {
        testMoveRightKO(new BoardImpl(new String[] { " BBBG" }), 0);
    }

    @Test
    public void testMoveRightKO3() {
        testMoveRightKO(new BoardImpl(new String[] { " BB# " }), 0);
    }

    @Test
    public void testMoveRightKO4() {
        testMoveRightKO(new BoardImpl(new String[] { " BG# " }), 0);
    }

    @Test
    public void testMoveRightKO5() {
        testMoveRightKO(new BoardImpl(new String[] { "AA ", " BB" }, new byte[] { 0x01, 0x11 }), 0);
    }

    private void testMoveRightKO(final Board input, final int index) {
        Assert.assertFalse(new StateImpl(input).moveRight(index));
    }

    @Test
    public void testMoveRightOK1() {
        testMoveRightOK(new BoardImpl(new String[] { " BB  " }), 0, new byte[] { 2,
                                                                                Board.SPACE_BYTE,
                                                                                2,
                                                                                'B',
                                                                                1,
                                                                                Board.SPACE_BYTE,
                                                                                Board.SER_DELIM_BYTE,
                                                                                Board.SER_DELIM_BYTE,
                                                                                0 });
    }

    @Test
    public void testMoveRightOK2() {
        testMoveRightOK(new BoardImpl(new String[] { " BB  ", " GBB " }), 1, new byte[] { 2,
                                                                                         Board.SPACE_BYTE,
                                                                                         2,
                                                                                         'B',
                                                                                         3,
                                                                                         Board.SPACE_BYTE,
                                                                                         1,
                                                                                         'G',
                                                                                         2,
                                                                                         'B',
                                                                                         Board.SER_DELIM_BYTE,
                                                                                         Board.SER_DELIM_BYTE,
                                                                                         0 });
    }

    @Test
    public void testMoveRightOK3() {
        testMoveRightOK(new BoardImpl(new String[] { " BBYRR ", " GBB R " }), 0, new byte[] { 2,
                                                                                             Board.SPACE_BYTE,
                                                                                             2,
                                                                                             'B',
                                                                                             1,
                                                                                             'Y',
                                                                                             2,
                                                                                             'R',
                                                                                             1,
                                                                                             Board.SPACE_BYTE,
                                                                                             1,
                                                                                             'G',
                                                                                             1,
                                                                                             Board.SPACE_BYTE,
                                                                                             2,
                                                                                             'B',
                                                                                             1,
                                                                                             Board.SPACE_BYTE,
                                                                                             1,
                                                                                             'R',
                                                                                             Board.SER_DELIM_BYTE,
                                                                                             Board.SER_DELIM_BYTE,
                                                                                             0 });
    }

    @Test
    public void testMoveRightOK4() {
        testMoveRightOK(new BoardImpl(new String[] { " YYGGG ", " GGG B ", "     # " }),
                        2,
                        new byte[] { 8,
                                    Board.SPACE_BYTE,
                                    2,
                                    'Y',
                                    3,
                                    'G',
                                    2,
                                    Board.SPACE_BYTE,
                                    3,
                                    'G',
                                    2,
                                    Board.SPACE_BYTE,
                                    1,
                                    'B',
                                    Board.SER_DELIM_BYTE,
                                    Board.SER_DELIM_BYTE,
                                    0 });
    }

    @Test
    public void testMoveRightOK5() {
        testMoveRightOK(new BoardImpl(new String[] { "AABB ", "#    " }), 0, new byte[] { 6,
                                                                                         Board.SPACE_BYTE,
                                                                                         2,
                                                                                         'A',
                                                                                         2,
                                                                                         'B',
                                                                                         Board.SER_DELIM_BYTE,
                                                                                         Board.SER_DELIM_BYTE,
                                                                                         0 });
    }

    @Test
    public void testMoveRightOK51() {
        testMoveRightOK(new BoardImpl(new String[] { "AABB ", "#    ", "##   " }), 0, new byte[] { 6,
                                                                                                  Board.SPACE_BYTE,
                                                                                                  2,
                                                                                                  'A',
                                                                                                  5,
                                                                                                  Board.SPACE_BYTE,
                                                                                                  2,
                                                                                                  'B',
                                                                                                  Board.SER_DELIM_BYTE,
                                                                                                  Board.SER_DELIM_BYTE,
                                                                                                  0 });
    }

    @Test
    public void testMoveRightOK6() {
        testMoveRightOK(new BoardImpl(new String[] { "AB ", "#  " }, new byte[] { 0, 1 }),
                        0,
                        new byte[] { 4,
                                    Board.SPACE_BYTE,
                                    1,
                                    'A',
                                    1,
                                    'B',
                                    Board.SER_DELIM_BYTE,
                                    18,
                                    17,
                                    17,
                                    18,
                                    Board.SER_DELIM_BYTE,
                                    0 });
    }

    @Test
    public void testMoveRightOK61() {
        testMoveRightOK(new BoardImpl(new String[] { "AB ", "#  ", "## " }, new byte[] { 0, 1 }),
                        0,
                        new byte[] { 4,
                                    Board.SPACE_BYTE,
                                    1,
                                    'A',
                                    1,
                                    'B',
                                    3,
                                    Board.SPACE_BYTE,
                                    Board.SER_DELIM_BYTE,
                                    18,
                                    17,
                                    17,
                                    18,
                                    Board.SER_DELIM_BYTE,
                                    0 });
    }

    private void testMoveRightOK(final Board input, final int index, final byte[] output) {
        final State state = new StateImpl(input);
        state.moveRight(index);
        state.process();
        Assert.assertEquals(state.getSerialization(), output);
    }


    @Test
    public void testProcess1() {
        testProcess(new BoardImpl(new String[] { " BB ", "    ", "    " }), new byte[] { 9,
                                                                                        Board.SPACE_BYTE,
                                                                                        2,
                                                                                        'B',
                                                                                        1,
                                                                                        Board.SPACE_BYTE,
                                                                                        Board.SER_DELIM_BYTE,
                                                                                        Board.SER_DELIM_BYTE,
                                                                                        0 });
    }


    @Test
    public void testProcess2() {
        testProcess(new BoardImpl(new String[] { "  GG ", " BB  ", "     " }), new byte[] { 7,
                                                                                           Board.SPACE_BYTE,
                                                                                           2,
                                                                                           'G',
                                                                                           2,
                                                                                           Board.SPACE_BYTE,
                                                                                           2,
                                                                                           'B',
                                                                                           2,
                                                                                           Board.SPACE_BYTE,
                                                                                           Board.SER_DELIM_BYTE,
                                                                                           Board.SER_DELIM_BYTE,
                                                                                           0 });
    }

    /*
     * @Test public void testProcess3() { testProcess(new BoardImpl(new String[] { "  GG ", " BBG ", "  GG ", "     "
     * }), "7GG2BBG3GG;;0"); }
     * @Test public void testProcess4() { testProcess(new BoardImpl(new String[] { "  ", " B", "##" }, new byte[] { 0x21
     * }, new char[] { 'B' }), "1B1B;;1"); }
     * @Test public void testProcess5() { testProcess(new BoardImpl(new String[] { "  ", " A", "##" }, new byte[] { 0x21
     * }, new char[] { 'B' }), "3A;;0"); }
     * @Test public void testProcess6() { testProcess(new BoardImpl(new String[] { " #", " B", "##" }, new byte[] { 0x21
     * }, new char[] { 'B' }), "3B;;0"); }
     * @Test public void testProcess7() { testProcess(new BoardImpl(new String[] { "  ", " A", " B", "##" }, new byte[]
     * { 0x31 }, new char[] { 'B' }), "1A1B1B;;1"); }
     */
    private void testProcess(final Board input, final byte[] output) {
        final State state = new StateImpl(input);
        state.process();
        Assert.assertEquals(state.getSerialization(), output);
    }
}
