package net.yangeorget.jelly;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = "fast")
public class GameTest {
    private static final Logger LOG = LoggerFactory.getLogger(GameTest.class);

    @Test
    public void testSolveKO1() {
        testSolveKO(new BoardImpl(new String[] { "R R B R" }));
    }

    @Test
    public void testSolveOK1() {
        testSolveOK(new BoardImpl(new String[] { "     R" }));
    }

    @Test
    public void testSolveOK2() {
        testSolveOK(new BoardImpl(new String[] { "R R" }));
    }

    @Test
    public void testSolveOK3() {
        testSolveOK(new BoardImpl(new String[] { "R R R" }));
    }

    @Test
    public void testSolveOK4() {
        testSolveOK(new BoardImpl(new String[] { "R R B B" }));
    }

    @Test
    public void testSolveOK5() {
        testSolveOK(new BoardImpl(new String[] { " R B R ", " ##### ", "       " }));
    }

    @Test
    public void testSolveOK6() {
        testSolveOK(new BoardImpl(new String[] { "  G       B ", "#B###G #####" }));
    }

    @Test
    public void testSolveOK7() {
        testSolveOK(new BoardImpl(new String[] { "       P    ", "      ##    ", "        P B ", "#B###GG#####" }));
    }

    @Test
    public void testSolveOK8() {
        testSolveOK(new BoardImpl(new String[] { "     ", "B B  ", "#####" }, new byte[] { 0x22 }, new char[] { 'B' }));
    }

    @Test
    public void testSolveOK9() {
        testSolveOK(new BoardImpl(new String[] { "     ", "R   B", "#####" }, new byte[] { 0x22 }, new char[] { 'B' }));
    }

    private void testSolveOK(final Board board) {
        Assert.assertNotNull(new GameImpl(board).solve(false));
    }

    private void testSolveKO(final Board board) {
        Assert.assertNull(new GameImpl(board).solve(false));
    }
}
