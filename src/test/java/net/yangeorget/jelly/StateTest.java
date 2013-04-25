package net.yangeorget.jelly;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class StateTest {
    private static final Logger LOG = LoggerFactory.getLogger(StateTest.class);

    @Test
    public void testClone() {
        final State state = new StateImpl(Board.LEVEL_1);
        Assert.assertEquals(state.clone()
                                 .toBoard(), state.toBoard());
    }

    @Test
    public void testGetDistinctColorsNb1() {
        Assert.assertEquals(new StateImpl(new BoardImpl("  R ")).getDistinctColorsNb(), 1);
    }

    @Test
    public void testGetDistinctColorsNb2() {
        Assert.assertEquals(new StateImpl(Board.LEVEL_1).getDistinctColorsNb(), 7);
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
        final StateImpl state = new StateImpl(input);
        state.gravity();
        Assert.assertEquals(state.toBoard(), output);
    }

    @Test
    public void testMoveKO1() {
        testMoveKO(new BoardImpl(" BBBB"), 0, 1);
    }

    @Test
    public void testMoveKO2() {
        testMoveKO(new BoardImpl(" BBBG"), 0, 1);
    }

    @Test
    public void testMoveKO3() {
        testMoveKO(new BoardImpl(" BBw "), 0, 1);
    }

    @Test
    public void testMoveKO4() {
        testMoveKO(new BoardImpl(" BGw "), 0, 1);
    }

    private void testMoveKO(final Board input, final int index, final int move) {
        final State state = new StateImpl(input);
        final Jelly jelly = state.getJellies()
                                 .get(index);
        Assert.assertFalse(state.move(jelly, move));
    }

    @Test
    public void testMoveOK1() {
        testMoveOK(new BoardImpl(" BB  "), 0, 1, new BoardImpl("  BB "));
    }

    @Test
    public void testMoveOK2() {
        testMoveOK(new BoardImpl(" BB  ", " GBB "), 1, 1, new BoardImpl("  BB ", "  GBB"));
    }

    @Test
    public void testMoveOK3() {
        testMoveOK(new BoardImpl(" BBYRR ", " GBB R "), 0, 1, new BoardImpl("  BBYRR", " G BB R"));
    }

    @Test
    public void testMoveOK4() {
        testMoveOK(new BoardImpl(" YYGGG ", " GGG B ", "     w "), 2, 1, new BoardImpl("       ", " YYGGG ", " GGG wB"));
    }

    private void testMoveOK(final Board input, final int index, final int move, final Board output) {
        final State state = new StateImpl(input);
        final Jelly jelly = state.getJellies()
                                 .get(index);
        state.move(jelly, move);
        Assert.assertEquals(state.toBoard(), output);
    }
}
