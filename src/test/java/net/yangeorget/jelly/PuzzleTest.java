package net.yangeorget.jelly;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PuzzleTest {
    private static final Logger LOG = LoggerFactory.getLogger(PuzzleTest.class);

    @Test(groups = "fast")
    public void test1() {
        solve(1);
    }

    @Test(groups = "fast")
    public void test() {
        solve(2);
    }

    @Test(groups = "fast")
    public void test3() {
        solve(3);
    }

    @Test(groups = "fast")
    public void test4() {
        solve(4);
    }

    @Test(groups = "fast")
    public void test5() {
        solve(5);
    }

    @Test(groups = "fast")
    public void test6() {
        solve(6);
    }

    @Test(groups = "fast")
    public void test7() {
        solve(7);
    }

    @Test(groups = "fast")
    public void test8() {
        solve(8);
    }

    @Test(groups = "fast")
    public void test9() {
        solve(9);
    }

    @Test(groups = "fast")
    public void test10() {
        solve(10);
    }

    @Test(groups = "fast")
    public void test11() {
        solve(11);
    }

    @Test(groups = "fast")
    public void test12() {
        solve(12);
    }

    @Test(groups = "fast")
    public void test13() {
        solve(13);
    }

    @Test(groups = "fast")
    public void test14() {
        solve(14);
    }

    @Test(groups = "slow")
    public void test15() {
        solve(15);
    }

    @Test(groups = "fast")
    public void test16() {
        solve(16);
    }

    @Test(groups = "slow")
    public void test17() {
        solve(17);
    }

    @Test(groups = "slow")
    public void test18() {
        solve(18);
    }

    @Test(groups = "fast")
    public void test19() {
        solve(19);
    }

    @Test(groups = "fast")
    public void test20() {
        solve(20);
    }

    @Test(groups = "fast")
    public void test21() {
        solve(21);
    }

    @Test(groups = "fast")
    public void test22() {
        solve(22);
    }

    @Test(groups = "fast")
    public void test23() {
        solve(23);
    }

    @Test(groups = "slow")
    public void test24() {
        solve(24);
    }

    @Test(groups = "fast")
    public void test25() {
        solve(25);
    }

    @Test(groups = "fast")
    public void test26() {
        solve(26);
    }

    @Test(groups = "slow")
    public void test27() {
        solve(27);
    }

    @Test(groups = "fast")
    public void test28() {
        solve(28);
    }

    @Test(groups = "fast")
    public void test29() {
        solve(29);
    }

    @Test(groups = "dunno")
    public void test30() {
        solve(30);
    }

    @Test(groups = "dunno")
    public void test31() {
        solve(31);
    }

    @Test(groups = "fast")
    public void test32() {
        solve(32);
    }

    @Test(groups = "dunno")
    public void test33() {
        solve(33);
    }

    @Test(groups = "dunno")
    public void test34() {
        solve(34);
    }

    @Test(groups = "fast")
    public void test35() {
        solve(35);
    }

    @Test(groups = "slow")
    public void test36() {
        solve(36);
    }

    @Test(groups = "dunno")
    public void test37() {
        solve(37);
    }

    @Test(groups = "fast")
    public void test38() {
        solve(38);
    }

    @Test(groups = "dunno")
    public void test39() { // TODO: out of memory
        solve(39);
    }

    @Test(groups = "dunno")
    public void test40() { // TODO: out of memory
        solve(40);
    }

    private void solve(final int level) {
        LOG.debug("solving: " + level);
        final Board board = PuzzleData.LEVELS[level - 1];
        final State state = new GameImpl(board).solve(true);
        Assert.assertNotNull(state);
        Assert.assertEquals(board.getJellyPositionNb(), state.getJellyPositionNb());
    }

    public static void main(final String[] args) {
        for (final String arg : args) {
            new GameImpl(PuzzleData.LEVELS[Integer.parseInt(arg)]).solve(true);
        }
    }
}
