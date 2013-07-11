package net.yangeorget.jelly;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = "fast")
public class BoardTest {
    @Test
    public void testHeight() {
        Assert.assertEquals(new BoardImpl(new String[] { "   ", "   " }).getHeight(), 2);
    }

    @Test
    public void testWidth() {
        Assert.assertEquals(new BoardImpl(new String[] { "   ", "   " }).getWidth(), 3);
    }

    @Test
    public void testGetJellyColorNb() {
        Assert.assertEquals(new BoardImpl(new String[] { "# A", "  A", "B #" }).getJellyColorNb(), 2);
    }

    @Test
    public void testGetWalls() {
        final Board board = new BoardImpl(new String[] { "# A", "  A", "B #" });
        Assert.assertTrue(board.isWall(0, 0));
        Assert.assertFalse(board.isWall(0, 1));
        Assert.assertFalse(board.isWall(0, 2));
        Assert.assertFalse(board.isWall(1, 0));
        Assert.assertFalse(board.isWall(1, 1));
        Assert.assertFalse(board.isWall(1, 2));
        Assert.assertFalse(board.isWall(2, 0));
        Assert.assertFalse(board.isWall(2, 1));
        Assert.assertTrue(board.isWall(2, 2));
    }

    @Test
    public void testToFloating() {
        Assert.assertEquals(BoardImpl.toFloating(BoardImpl.toSpaceCharDelta('a')), BoardImpl.toSpaceCharDelta('A'));
        Assert.assertEquals(BoardImpl.toFloating(BoardImpl.toSpaceCharDelta('A')), BoardImpl.toSpaceCharDelta('A'));
    }

    @Test
    public void testToFixed() {
        Assert.assertEquals(BoardImpl.toFixed(BoardImpl.toSpaceCharDelta('A')), BoardImpl.toSpaceCharDelta('a'));
        Assert.assertEquals(BoardImpl.toFixed(BoardImpl.toSpaceCharDelta('a')), BoardImpl.toSpaceCharDelta('a'));
    }
}
