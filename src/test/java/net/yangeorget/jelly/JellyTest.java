package net.yangeorget.jelly;

import java.util.Arrays;
import org.testng.Assert;
import org.testng.annotations.Test;

public class JellyTest {
    @Test
    public void testClone() {
        final Jelly jelly = new JellyImpl(Arrays.asList(new Position(1, 2), new Position(1, 3), new Position(2, 2)));
        Assert.assertEquals(jelly.toString(), jelly.clone()
                                                   .toString());
    }

    @Test
    public void testUpdate() {
        final Jelly jelly = new JellyImpl(Arrays.asList(new Position(1, 2), new Position(1, 3), new Position(2, 2)));
        final String jellyAsString = jelly.toString();
        jelly.moveHorizontally(1, 10);
        jelly.moveHorizontally(-1, 10);
        Assert.assertEquals(jelly.toString(), jellyAsString);
    }

    @Test
    public void testOverlaps1() {
        final Jelly jelly1 = new JellyImpl(Arrays.asList(new Position(1, 2), new Position(1, 3), new Position(2, 2)));
        final Jelly jelly2 = new JellyImpl(Arrays.asList(new Position(2, 1), new Position(3, 1), new Position(2, 2)));
        Assert.assertTrue(jelly1.overlaps(jelly2));
    }

    @Test
    public void testOverlaps2() {
        final Jelly jelly1 = new JellyImpl(Arrays.asList(new Position(1, 2), new Position(1, 3), new Position(2, 2)));
        final Jelly jelly2 = new JellyImpl(Arrays.asList(new Position(2, 1), new Position(3, 1), new Position(2, 3)));
        Assert.assertFalse(jelly1.overlaps(jelly2));
    }
}
