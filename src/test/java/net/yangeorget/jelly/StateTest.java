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
        final String ser = state.getSerialization()
                                .toString();
        state.updateBoard();
        state.updateFromBoard();
        Assert.assertEquals(state.getSerialization()
                                 .toString(), ser);
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
        testMoveRightOK(new BoardImpl(new String[] { " BB  " }), 0, "2BB;;");
    }

    @Test
    public void testMoveRightOK2() {
        testMoveRightOK(new BoardImpl(new String[] { " BB  ", " GBB " }), 1, "2BB3GBB;;");
    }

    @Test
    public void testMoveRightOK3() {
        testMoveRightOK(new BoardImpl(new String[] { " BBYRR ", " GBB R " }), 0, "2BBYRR1G1BB1R;;");
    }

    @Test
    public void testMoveRightOK4() {
        testMoveRightOK(new BoardImpl(new String[] { " YYGGG ", " GGG B ", "     # " }), 2, "8YYGGG2GGG2B;;");
    }

    @Test
    public void testMoveRightOK5() {
        testMoveRightOK(new BoardImpl(new String[] { "AABB ", "#    " }), 0, "6AABB;;");
    }

    @Test
    public void testMoveRightOK51() {
        testMoveRightOK(new BoardImpl(new String[] { "AABB ", "#    ", "##   " }), 0, "6AA5BB;;");
    }

    @Test
    public void testMoveRightOK6() {
        testMoveRightOK(new BoardImpl(new String[] { "AB ", "#  " }, new byte[] { 0, 1 }), 0, "4AB;12111112;");
    }

    @Test
    public void testMoveRightOK61() {
        testMoveRightOK(new BoardImpl(new String[] { "AB ", "#  ", "## " }, new byte[] { 0, 1 }), 0, "4AB;12111112;");
    }

    private void testMoveRightOK(final Board input, final int index, final String output) {
        final State state = new StateImpl(input);
        state.moveRight(index);
        state.process();
        Assert.assertEquals(state.getSerialization()
                                 .toString(), output);
    }


    @Test
    public void testProcess1() {
        testProcess(new BoardImpl(new String[] { " BB ", "    ", "    " }), "9BB;;");
    }

    @Test
    public void testProcess2() {
        testProcess(new BoardImpl(new String[] { "  GG ", " BB  ", "     " }), "7GG2BB;;");
    }

    @Test
    public void testProcess3() {
        testProcess(new BoardImpl(new String[] { "  GG ", " BBG ", "  GG ", "     " }), "7GG2BBG3GG;;");
    }

    @Test
    public void testProcess4() {
        testProcess(new BoardImpl(new String[] { "  ", " B", "##" }, new byte[] { 0x21 }, new char[] { 'B' }),
                    "1B1B;;1");
    }

    @Test
    public void testProcess5() {
        testProcess(new BoardImpl(new String[] { "  ", " A", "##" }, new byte[] { 0x21 }, new char[] { 'B' }), "3A;;");
    }

    @Test
    public void testProcess6() {
        testProcess(new BoardImpl(new String[] { " #", " B", "##" }, new byte[] { 0x21 }, new char[] { 'B' }), "3B;;");
    }

    @Test
    public void testProcess7() {
        testProcess(new BoardImpl(new String[] { "  ", " A", " B", "##" }, new byte[] { 0x31 }, new char[] { 'B' }),
                    "1A1B1B;;1");
    }

    private void testProcess(final Board input, final String output) {
        final State state = new StateImpl(input);
        state.process();
        Assert.assertEquals(state.getSerialization()
                                 .toString(), output);
    }
}
