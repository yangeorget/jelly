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
    public void testGravityOK1() {
        testGravityOK(new BoardImpl(" BB ", "    ", "    "), new BoardImpl("    ", "    ", " BB "));
    }

    @Test
    public void testGravityOK2() {
        testGravityOK(new BoardImpl("  GG ", " BB  ", "     "), new BoardImpl("     ", "  GG ", " BB  "));
    }

    @Test
    public void testGravityOK3() {
        testGravityOK(new BoardImpl("  GG ", " BBG ", "  GG ", "     "), new BoardImpl("     ",
                                                                                       "  GG ",
                                                                                       " BBG ",
                                                                                       "  GG "));
    }

    private void testGravityOK(final Board input, final Board output) {
        final Game game = new GameImpl(input);
        final State state = game.getStates()
                                .get(0);
        state.gravity();
        LOG.debug(state.toString());
        Assert.assertEquals(state.toBoard(), output);
    }

    @Test
    public void testSlideKO1() {
        testSlideKO(new BoardImpl(" BBBB"), 'B', 0, 1);
    }

    @Test
    public void testSlideKO2() {
        testSlideKO(new BoardImpl(" BBBG"), 'B', 0, 1);
    }

    @Test
    public void testSlideKO3() {
        testSlideKO(new BoardImpl(" BBw "), 'B', 0, 1);
    }

    @Test
    public void testSlideKO4() {
        testSlideKO(new BoardImpl(" BGw "), 'B', 0, 1);
    }

    private void testSlideKO(final Board input, final char color, final int index, final int move) {
        final Game game = new GameImpl(input);
        final State state = game.getStates()
                                .get(0);
        Assert.assertNull(state.slide(color, index, move));
    }

    @Test
    public void testSlideOK1() {
        testSlideOK(new BoardImpl(" BB  "), 'B', 0, 1, new BoardImpl("  BB "));
    }

    @Test
    public void testSlideOK2() {
        testSlideOK(new BoardImpl(" BB  ", " GBB "), 'G', 0, 1, new BoardImpl("  BB ", "  GBB"));
    }

    @Test
    public void testSlideOK3() {
        testSlideOK(new BoardImpl(" BBYRR ", " GBB R "), 'B', 0, 1, new BoardImpl("  BBYRR", " G BB R"));
    }

    @Test
    public void testSlideOK4() {
        testSlideOK(new BoardImpl(" YYGGG ", " GGG B ", "     w "), 'B', 0, 1, new BoardImpl("       ",
                                                                                             " YYGGG ",
                                                                                             " GGG wB"));
    }

    private void testSlideOK(final Board input, final char color, final int index, final int move, final Board output) {
        final Game game = new GameImpl(input);
        LOG.debug(game.toString());
        final State state = game.getStates()
                                .get(0)
                                .slide(color, index, move);
        LOG.debug(state.toString());
        Assert.assertEquals(state.toBoard(), output);
    }
}
