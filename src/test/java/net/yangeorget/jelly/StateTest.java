package net.yangeorget.jelly;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class StateTest {
    private static final Logger LOG = LoggerFactory.getLogger(StateTest.class);

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

    private void testMoveOK(final Board input, final int index, final Board output) {
        final State state = new StateImpl(input);
        state.moveRight(index);
        state.gravity();
        Assert.assertEquals(state.getSerialization(), output.serialize());
    }
}
