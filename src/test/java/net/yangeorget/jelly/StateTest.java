package net.yangeorget.jelly;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class StateTest {
    private static final Logger LOG = LoggerFactory.getLogger(StateTest.class);

    @Test
    public void testUpdateBoard() {
        final Board board = new BoardImpl(new String[] { " BB ", "    " });
        final String ser = board.serialize();
        final State state = new StateImpl(board);
        state.updateBoard();
        Assert.assertEquals(board.serialize(), ser);
    }

    @Test
    public void testUpdateFromBoard1() {
        final Board board = new BoardImpl(new String[] { " BB ", "    " });
        final State state = new StateImpl(board);
        final String ser = state.getSerialization();
        state.updateBoard();
        state.updateFromBoard();
        Assert.assertEquals(state.getSerialization(), ser);
    }

    @Test
    public void testState1() {
        testState(new BoardImpl(new String[] { "AB" }, new byte[][] { { 0, 1 }, { 1, 0 } }), 2);
    }

    @Test
    public void testState2() {
        testState(new BoardImpl(new String[] { "ABA" }, new byte[][] { { 0, 1 }, { 1, 0 } }), 2, 1);
    }

    @Test
    public void testState3() {
        testState(new BoardImpl(new String[] { "A  ", "ACC", "BB " }, new byte[][] { { 0x10, 0x20 }, { 0x20, 0x10 } }),
                  4,
                  2);
    }

    @Test
    public void testState4() {
        testState(new BoardImpl(new String[] { " YDDY Y ", "  ### # " },
                                new byte[][] { { 1, 2, 3, 4 }, { 2, 1, 4, 3 } }), 4, 1);
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
        testMoveRightKO(new BoardImpl(new String[] { "AA ", " BB" }, new byte[][] { { 0x01, 0x11 }, { 0x11, 0x01 } }),
                        0);
    }

    private void testMoveRightKO(final Board input, final int index) {
        Assert.assertFalse(new StateImpl(input).moveRight(index));
    }

    @Test
    public void testMoveRightOK1() {
        testMoveRightOK(new BoardImpl(new String[] { " BB  " }), 0, new BoardImpl(new String[] { "  BB " }));
    }

    @Test
    public void testMoveRightOK2() {
        testMoveRightOK(new BoardImpl(new String[] { " BB  ", " GBB " }), 1, new BoardImpl(new String[] { "  BB ",
                                                                                                         "  GBB" }));
    }

    @Test
    public void testMoveRightOK3() {
        testMoveRightOK(new BoardImpl(new String[] { " BBYRR ", " GBB R " }),
                        0,
                        new BoardImpl(new String[] { "  BBYRR", " G BB R" }));
    }

    @Test
    public void testMoveRightOK4() {
        testMoveRightOK(new BoardImpl(new String[] { " YYGGG ", " GGG B ", "     # " }),
                        2,
                        new BoardImpl(new String[] { "       ", " YYGGG ", " GGG #B" }));
    }

    @Test
    public void testMoveRightOK5() {
        testMoveRightOK(new BoardImpl(new String[] { "AABB ", "#    " }), 0, new BoardImpl(new String[] { "     ",
                                                                                                         "#AABB" }));
    }

    @Test
    public void testMoveRightOK51() {
        testMoveRightOK(new BoardImpl(new String[] { "AABB ", "#    ", "##   " }),
                        0,
                        new BoardImpl(new String[] { "     ", "#AA  ", "## BB" }));
    }

    @Test
    public void testMoveRightOK6() {
        final Board input = new BoardImpl(new String[] { "AB ", "#  " }, new byte[][] { { 0, 1 }, { 1, 0 } });
        final Board output = new BoardImpl(new String[] { "   ", "#AB" });
        testMoveRightOK(input, 0, output);
        Assert.assertEquals(input.getLinks(0).length, 2);
        Assert.assertEquals(input.getLinks(1).length, 2);
    }

    @Test
    public void testMoveRightOK61() {
        final Board input = new BoardImpl(new String[] { "AB ", "#  ", "## " }, new byte[][] { { 0, 1 }, { 1, 0 } });
        final Board output = new BoardImpl(new String[] { "   ", "#AB", "## " });
        testMoveRightOK(input, 0, output);
        Assert.assertEquals(input.getLinks(0).length, 2);
        Assert.assertEquals(input.getLinks(1).length, 2);
    }

    private void testMoveRightOK(final Board input, final int index, final Board output) {
        final State state = new StateImpl(input);
        state.moveRight(index);
        state.gravity();
        Assert.assertEquals(state.getSerialization(), output.serialize());
        // TODO: integrate checks on jellies
    }

    @Test
    public void testMoveOK7() {
        final Board input = new BoardImpl(new String[] { "     A", "     #", " ABBA " }, new byte[][] { { 0x21,
                                                                                                         0x22,
                                                                                                         0x23,
                                                                                                         0x24 },
                                                                                                       { 0x22,
                                                                                                        0x21,
                                                                                                        0x24,
                                                                                                        0x23 } });
        final State state = new StateImpl(input);
        state.moveLeft(0);
        state.gravity();
        Assert.assertEquals(state.getSerialization(),
                            new BoardImpl(new String[] { "      ", "    A#", " ABBA " }).serialize());
        Assert.assertEquals(input.getLinks(0).length, 4);
        Assert.assertEquals(input.getLinks(1).length, 4);
        state.moveLeft(0);
        state.gravity();
        Assert.assertEquals(state.getSerialization(),
                            new BoardImpl(new String[] { "      ", "   A #", "ABBA  " }).serialize());
        Assert.assertEquals(input.getLinks(0).length, 4);
        Assert.assertEquals(input.getLinks(1).length, 4);

    }

    @Test
    public void testGravity1() {
        testGravity(new BoardImpl(new String[] { " BB ", "    ", "    " }), new BoardImpl(new String[] { "    ",
                                                                                                        "    ",
                                                                                                        " BB " }));
    }

    @Test
    public void testGravity2() {
        testGravity(new BoardImpl(new String[] { "  GG ", " BB  ", "     " }), new BoardImpl(new String[] { "     ",
                                                                                                           "  GG ",
                                                                                                           " BB  " }));
    }

    @Test
    public void testGravity3() {
        testGravity(new BoardImpl(new String[] { "  GG ", " BBG ", "  GG ", "     " }),
                    new BoardImpl(new String[] { "     ", "  GG ", " BBG ", "  GG " }));
    }

    private void testGravity(final Board input, final Board output) {
        final State state = new StateImpl(input);
        state.gravity();
        Assert.assertEquals(state.getSerialization(), output.serialize());
    }

}
