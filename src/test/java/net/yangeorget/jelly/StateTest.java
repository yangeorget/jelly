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
    public void testUpdateFromBoard() {
        final Board board = new BoardImpl(new String[] { " BB ", "    " });
        final State state = new StateImpl(board);
        final String ser = state.getSerialization();
        state.updateBoard();
        state.updateFromBoard();
        Assert.assertEquals(state.getSerialization(), ser);
    }


    @Test
    public void testState1() {
        testState(new BoardImpl(new String[] { "AB" }, new byte[][] { { 0, 1 }, { 1, 0 } }), 1);
    }

    @Test
    public void testState2() {
        testState(new BoardImpl(new String[] { "ABA" }, new byte[][] { { 0, 1 }, { 1, 0 } }), 2);
    }

    @Test
    public void testState3() {
        testState(new BoardImpl(new String[] { "A  ", "ACC", "BB " }, new byte[][] { { 0x10, 0x20 }, { 0x20, 0x10 } }),
                  2);
    }

    private void testState(final Board board, final int jellyNb) {
        final Jelly[] jellies = new StateImpl(board).getJellies();
        Assert.assertEquals(jellies.length, jellyNb);
    }

    @Test
    public void testMoveKO1() {
        testMoveKO(new BoardImpl(new String[] { " BBBB" }), 0);
    }

    @Test
    public void testMoveKO2() {
        testMoveKO(new BoardImpl(new String[] { " BBBG" }), 0);
    }

    @Test
    public void testMoveKO3() {
        testMoveKO(new BoardImpl(new String[] { " BB# " }), 0);
    }

    @Test
    public void testMoveKO4() {
        testMoveKO(new BoardImpl(new String[] { " BG# " }), 0);
    }

    @Test
    public void testMoveKO5() {
        testMoveKO(new BoardImpl(new String[] { "AA ", " BB" }, new byte[][] { { 0x01, 0x11 }, { 0x11, 0x01 } }), 0);
    }

    private void testMoveKO(final Board input, final int index) {
        Assert.assertFalse(new StateImpl(input).moveRight(index));
    }

    @Test
    public void testMoveOK1() {
        testMoveOK(new BoardImpl(new String[] { " BB  " }), 0, new BoardImpl(new String[] { "  BB " }));
    }

    @Test
    public void testMoveOK2() {
        testMoveOK(new BoardImpl(new String[] { " BB  ", " GBB " }),
                   1,
                   new BoardImpl(new String[] { "  BB ", "  GBB" }));
    }

    @Test
    public void testMoveOK3() {
        testMoveOK(new BoardImpl(new String[] { " BBYRR ", " GBB R " }), 0, new BoardImpl(new String[] { "  BBYRR",
                                                                                                        " G BB R" }));
    }

    @Test
    public void testMoveOK4() {
        testMoveOK(new BoardImpl(new String[] { " YYGGG ", " GGG B ", "     # " }),
                   2,
                   new BoardImpl(new String[] { "       ", " YYGGG ", " GGG #B" }));
    }

    @Test
    public void testMoveOK5() {
        testMoveOK(new BoardImpl(new String[] { "AABB ", "#    " }),
                   0,
                   new BoardImpl(new String[] { "     ", "#AABB" }));
    }

    private void testMoveOK(final Board input, final int index, final Board output) {
        final State state = new StateImpl(input);
        state.moveRight(index);
        state.gravity();
        Assert.assertEquals(state.getSerialization(), output.serialize());
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
