package net.yangeorget.jelly;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PuzzleTest {
    private static final Logger LOG = LoggerFactory.getLogger(PuzzleTest.class);

    @Test
    public void test1() {
        solve(1);
    }

    @Test
    public void test() {
        solve(2);
    }

    @Test
    public void test3() {
        solve(3);
    }

    @Test
    public void test4() {
        solve(4);
    }

    @Test
    public void test5() {
        solve(5);
    }

    @Test
    public void test6() {
        solve(6);
    }

    @Test
    public void test7() {
        solve(7);
    }

    @Test
    public void test8() {
        solve(8);
    }

    @Test
    public void test9() {
        solve(9);
    }

    @Test
    public void test10() {
        solve(10);
    }

    @Test
    public void test11() {
        solve(11);
    }

    @Test
    public void test12() {
        solve(12);
    }

    @Test
    public void test13() {
        solve(13);
    }

    @Test
    public void test14() {
        solve(14);
    }

    @Test
    public void test15() {
        solve(15);
    }

    @Test
    public void test16() {
        solve(16);
    }

    @Test
    public void test17() {
        solve(17);
    }

    @Test
    public void test18() {
        solve(18);
    }

    @Test
    public void test19() {
        solve(19);
    }

    @Test
    public void test20() {
        solve(20);
    }

    @Test
    public void test21() {
        solve(21);
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
