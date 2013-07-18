package net.yangeorget.jelly;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test
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
    public void testGetJellyPositionNb() {
        Assert.assertEquals(Board.LEVELS[26].getJellyPositionNb(), 32);
    }

    @Test
    public void testGetWalls() {
        final Board board = new BoardImpl(new String[] { "# A", "  A", "B #" });
        Assert.assertTrue(board.isWall(Cells.value(0, 0)));
        Assert.assertFalse(board.isWall(Cells.value(0, 1)));
        Assert.assertFalse(board.isWall(Cells.value(0, 2)));
        Assert.assertFalse(board.isWall(Cells.value(1, 0)));
        Assert.assertFalse(board.isWall(Cells.value(1, 1)));
        Assert.assertFalse(board.isWall(Cells.value(1, 2)));
        Assert.assertFalse(board.isWall(Cells.value(2, 0)));
        Assert.assertFalse(board.isWall(Cells.value(2, 1)));
        Assert.assertTrue(board.isWall(Cells.value(2, 2)));
    }

    @Test
    public void testToFloating() {
        Assert.assertEquals(BoardImpl.toFloating((byte) 'a'), (byte) 'A');
        Assert.assertEquals(BoardImpl.toFloating((byte) 'A'), (byte) 'A');
    }

    @Test
    public void testToFixed() {
        Assert.assertEquals(BoardImpl.toFixed((byte) 'A'), (byte) 'a');
        Assert.assertEquals(BoardImpl.toFixed((byte) 'a'), (byte) 'a');
    }
}
