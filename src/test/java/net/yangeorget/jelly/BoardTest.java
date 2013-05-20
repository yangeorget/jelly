package net.yangeorget.jelly;

import org.testng.Assert;
import org.testng.annotations.Test;

public class BoardTest {
    @Test
    public void testHeight() {
        Assert.assertEquals(Board.LEVELS[0].getHeight(), 8);
    }

    @Test
    public void testWidth() {
        Assert.assertEquals(Board.LEVELS[0].getWidth(), 12);
    }
}
