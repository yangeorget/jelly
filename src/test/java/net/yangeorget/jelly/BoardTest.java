package net.yangeorget.jelly;

import org.testng.Assert;
import org.testng.annotations.Test;

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
    public void testSerialize() {
        Assert.assertEquals(new BoardImpl(new String[] { " #1", " 2 ", "3  " }).serialize(), "1 2 3  [][][]");
    }

    @Test
    public void testGetJellyColorNb() {
        Assert.assertEquals(new BoardImpl(new String[] { "# A", "  A", "B #" }).getJellyColorNb(), 2);
    }

    @Test
    public void testGetWalls() {
        final boolean[][] walls = new BoardImpl(new String[] { "# A", "  A", "B #" }).getWalls();
        Assert.assertTrue(walls[0][0]);
        Assert.assertFalse(walls[0][1]);
        Assert.assertFalse(walls[0][2]);
        Assert.assertFalse(walls[1][0]);
        Assert.assertFalse(walls[1][1]);
        Assert.assertFalse(walls[1][2]);
        Assert.assertFalse(walls[2][0]);
        Assert.assertFalse(walls[2][1]);
        Assert.assertTrue(walls[2][2]);
    }

    @Test
    public void testToFloating() {
        Assert.assertEquals(BoardImpl.toFloating('a'), 'A');
        Assert.assertEquals(BoardImpl.toFloating('A'), 'A');
    }

    @Test
    public void testToFixed() {
        Assert.assertEquals(BoardImpl.toFixed('A'), 'a');
        Assert.assertEquals(BoardImpl.toFixed('a'), 'a');
    }
}
