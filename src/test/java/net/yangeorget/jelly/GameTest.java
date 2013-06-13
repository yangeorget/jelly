package net.yangeorget.jelly;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GameTest {
    private static final Logger LOG = LoggerFactory.getLogger(GameTest.class);

    @Test
    public void testSolve1() {
        testSolveOK(new BoardImpl(new String[] { "     R" }));
    }

    @Test
    public void testSolve2() {
        testSolveOK(new BoardImpl(new String[] { "R R" }));
    }

    @Test
    public void testSolve3() {
        testSolveOK(new BoardImpl(new String[] { "R R R" }));
    }

    @Test
    public void testSolve4() {
        testSolveOK(new BoardImpl(new String[] { "R R B B" }));
    }

    @Test
    public void testSolve5() {
        testSolveKO(new BoardImpl(new String[] { "R R B R" }));
    }

    @Test
    public void testSolve6() {
        testSolveOK(new BoardImpl(new String[] { " R B R ", " ##### ", "       " }));
    }


    @Test
    public void testSolve7() {
        testSolveOK(new BoardImpl(new String[] { "  G       B ", "#B###G #####" }));
    }


    @Test
    public void testSolve8() {
        testSolveOK(new BoardImpl(new String[] { "       P    ", "      ##    ", "        P B ", "#B###GG#####" }));
    }

    private void testSolveOK(final Board board) {
        final Game game = new GameImpl(board);
        Assert.assertNotNull(game.solve());
    }

    private void testSolveKO(final Board board) {
        final Game game = new GameImpl(board);
        Assert.assertNull(game.solve());
    }

    @Test
    public void testSolveLEVEL_1() {
        solve(1);
    }

    @Test
    public void testSolveLEVEL_2() {
        solve(2);
    }

    @Test
    public void testSolveLEVEL_3() {
        solve(3);
    }

    @Test
    public void testSolveLEVEL_4() {
        solve(4);
    }

    @Test
    public void testSolveLEVEL_5() {
        solve(5);
    }

    @Test
    public void testSolveLEVEL_6() {
        solve(6);
    }

    @Test
    public void testSolveLEVEL_7() {
        solve(7);
    }

    @Test
    public void testSolveLEVEL_8() {
        solve(8);
    }

    @Test
    public void testSolveLEVEL_9() {
        solve(9);
    }

    @Test
    public void testSolveLEVEL_10() {
        solve(10);
    }

    @Test
    public void testSolveLEVEL_11() {
        solve(11);
    }

    @Test
    public void testSolveLEVEL_12() {
        solve(12);
    }

    @Test
    public void testSolveLEVEL_13() {
        solve(13);
    }

    @Test
    public void testSolveLEVEL_14() {
        solve(14);
    }

    @Test
    public void testSolveLEVEL_15() {
        solve(15);
    }

    @Test
    public void testSolveLEVEL_16() {
        solve(16);
    }

    @Test
    public void testSolveLEVEL_17() {
        solve(17);
    }

    @Test
    public void testSolveLEVEL_18() {
        solve(18);
    }

    @Test
    public void testSolveLEVEL_19() {
        solve(19);
    }

    @Test
    public void testSolveLEVEL_20() {
        solve(20);
    }

    private void solve(final int level) {
        final Game game = new GameImpl(Board.LEVELS[level - 1]);
        LOG.debug("solving: " + level);
        final long time = System.currentTimeMillis();
        final State state = game.solve();
        LOG.debug("solved in: " + (System.currentTimeMillis() - time));
        Assert.assertNotNull(state);
        game.explain(state);
    }

    public static void main(final String[] args) {
        for (int i = 0; i < Board.LEVELS.length; i++) {
            new GameImpl(Board.LEVELS[i]).solve();
        }
    }
}
