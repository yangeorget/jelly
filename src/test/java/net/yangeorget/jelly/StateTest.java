package net.yangeorget.jelly;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class StateTest {
    private static final Logger LOG = LoggerFactory.getLogger(StateTest.class);

    @Test
    public void testClone() {
        final State state = new GameImpl(Board.BOARD1).getStates()
                                                      .get(0);
        Assert.assertEquals(state.clone()
                                 .toBoard(), state.toBoard());
    }

    @Test
    public void testGetDistinctColorsNb1() {
        Assert.assertEquals(new GameImpl(new BoardImpl("  R ")).getStates()
                                                               .get(0)
                                                               .getDistinctColorsNb(), 1);
    }

    @Test
    public void testGetDistinctColorsNb2() {
        Assert.assertEquals(new GameImpl(Board.BOARD1).getStates()
                                                      .get(0)
                                                      .getDistinctColorsNb(), 4);
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
        final Game game = new GameImpl(input);
        final StateImpl state = (StateImpl) game.getStates()
                                                .get(0);
        state.gravity();
        LOG.debug(state.toString());
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
        final Game game = new GameImpl(input);
        final State state = game.getStates()
                                .get(0);
        Assert.assertNull(state.move(index, move));
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
        final Game game = new GameImpl(input);
        LOG.debug(game.toString());
        final State state = game.getStates()
                                .get(0)
                                .move(index, move);
        LOG.debug(state.toString());
        Assert.assertEquals(state.toBoard(), output);
    }
}
