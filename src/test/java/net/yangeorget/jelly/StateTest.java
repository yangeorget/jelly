package net.yangeorget.jelly;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class StateTest {
    private static final Logger LOG = LoggerFactory.getLogger(StateTest.class);

    @Test
    public void testGetDistinctColorsNb1() {
        Assert.assertEquals(new StateImpl(new BoardImpl("  R ")).getDistinctColorsNb(), 1);
    }

    @Test
    public void testGetDistinctColorsNb2() {
        Assert.assertEquals(new StateImpl(Board.LEVELS[0]).getDistinctColorsNb(), 3);
    }

    @Test
    public void testGravity1() {
        testGravity(new BoardImpl(" BB ", "    ", "    "), new BoardImpl("    ", "    ", " BB "));
    }

    @Test
    public void testGravity2() {
        testGravity(new BoardImpl("  GG ", " BB  ", "     "), new BoardImpl("     ", "  GG ", " BB  "));
    }

    @Test
    public void testGravity3() {
        testGravity(new BoardImpl("  GG ", " BBG ", "  GG ", "     "),
                    new BoardImpl("     ", "  GG ", " BBG ", "  GG "));
    }

    private void testGravity(final Board input, final Board output) {
        final State state = new StateImpl(input);
        state.gravity();
        Assert.assertEquals(new BoardImpl(state), output);
    }

    @Test
    public void testMoveKO1() {
        testMoveKO(new BoardImpl(" BBBB"), 0);
    }

    @Test
    public void testMoveKO2() {
        testMoveKO(new BoardImpl(" BBBG"), 0);
    }

    @Test
    public void testMoveKO3() {
        testMoveKO(new BoardImpl(" BB0 "), 0);
    }

    @Test
    public void testMoveKO4() {
        testMoveKO(new BoardImpl(" BG0 "), 0);
    }

    private void testMoveKO(final Board input, final int index) {
        Assert.assertFalse(new StateImpl(input).moveRight(index));
    }

    @Test
    public void testMoveOK1() {
        testMoveOK(new BoardImpl(" BB  "), 0, new BoardImpl("  BB "));
    }

    @Test
    public void testMoveOK2() {
        testMoveOK(new BoardImpl(" BB  ", " GBB "), 1, new BoardImpl("  BB ", "  GBB"));
    }

    @Test
    public void testMoveOK3() {
        testMoveOK(new BoardImpl(" BBYRR ", " GBB R "), 0, new BoardImpl("  BBYRR", " G BB R"));
    }

    @Test
    public void testMoveOK4() {
        testMoveOK(new BoardImpl(" YYGGG ", " GGG B ", "     0 "), 2, new BoardImpl("       ", " YYGGG ", " GGG  B"));
    }

    private void testMoveOK(final Board input, final int index, final Board output) {
        final State state = new StateImpl(input);
        state.moveRight(index);
        state.gravity();
        Assert.assertEquals(new BoardImpl(state), output);
    }
}
