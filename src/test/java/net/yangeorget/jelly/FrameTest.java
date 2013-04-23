package net.yangeorget.jelly;

import org.testng.Assert;
import org.testng.annotations.Test;

public class FrameTest {
    @Test
    public void testHeight() {
        Assert.assertEquals(Board.LEVEL_1.getHeight(), 8);
    }

    @Test
    public void testWidth() {
        Assert.assertEquals(Board.LEVEL_1.getWidth(), 12);
    }
}
