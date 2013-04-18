package net.yangeorget.jelly;

import java.util.Arrays;
import org.testng.Assert;
import org.testng.annotations.Test;

public class JellyTest {
    private static Frame F5_5 = new Frame() {
        @Override
        public int getHeight() {
            return 5;
        }

        @Override
        public int getWidth() {
            return 5;
        }
    };

    @Test
    public void testClone() {
        final Jelly jelly = new JellyImpl(F5_5, Arrays.asList(new Position(1, 2),
                                                              new Position(1, 3),
                                                              new Position(2, 2)));
        Assert.assertEquals(jelly.toString(), jelly.clone()
                                                   .toString());
    }

    @Test
    public void testUpdate() {
        final Jelly jelly = new JellyImpl(F5_5, Arrays.asList(new Position(1, 2),
                                                              new Position(1, 3),
                                                              new Position(2, 2)));
        final String jellyAsString = jelly.toString();
        jelly.hMove(1);
        jelly.hMove(-1);
        Assert.assertEquals(jelly.toString(), jellyAsString);
    }

    @Test
    public void testOverlaps1() {
        final Jelly jelly1 = new JellyImpl(F5_5, Arrays.asList(new Position(1, 2), new Position(1, 3), new Position(2,
                                                                                                                    2)));
        final Jelly jelly2 = new JellyImpl(F5_5, Arrays.asList(new Position(2, 1), new Position(3, 1), new Position(2,
                                                                                                                    2)));
        Assert.assertTrue(jelly1.overlaps(jelly2));
    }

    @Test
    public void testOverlaps2() {
        final Jelly jelly1 = new JellyImpl(F5_5, Arrays.asList(new Position(1, 2), new Position(1, 3), new Position(2,
                                                                                                                    2)));
        final Jelly jelly2 = new JellyImpl(F5_5, Arrays.asList(new Position(2, 1), new Position(3, 1), new Position(2,
                                                                                                                    3)));
        Assert.assertFalse(jelly1.overlaps(jelly2));
    }

    @Test
    public void testAdjacentTo1() {
        final Jelly jelly1 = new JellyImpl(F5_5, Arrays.asList(new Position(1, 2), new Position(1, 3), new Position(2,
                                                                                                                    2)));
        final Jelly jelly2 = new JellyImpl(F5_5, Arrays.asList(new Position(3, 1), new Position(3, 2)));
        Assert.assertTrue(jelly1.adjacentTo(jelly2));
    }

    @Test
    public void testAdjacentTo2() {
        final Jelly jelly1 = new JellyImpl(F5_5, Arrays.asList(new Position(1, 2), new Position(1, 3), new Position(2,
                                                                                                                    2)));
        final Jelly jelly2 = new JellyImpl(F5_5, Arrays.asList(new Position(3, 0), new Position(3, 1)));
        Assert.assertFalse(jelly1.adjacentTo(jelly2));
    }
}
