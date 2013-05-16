package net.yangeorget.jelly;

import org.testng.Assert;
import org.testng.annotations.Test;

public class JellyTest {

    @Test
    public void testClone() {
        final Jelly jelly = new JellyImpl((byte) 0x55,
                                          'B',
                                          false,
                                          (byte) 2,
                                          (byte) 3,
                                          (byte) 1,
                                          (byte) 2,
                                          (byte) 0x12,
                                          (byte) 0x13,
                                          (byte) 0x22);
        Assert.assertEquals(jelly.toString(), jelly.clone()
                                                   .toString());
    }

    @Test
    public void testGetHeight1() {
        final Jelly jelly = new JellyImpl((byte) 0x8D, 'B', false, (byte) 0, (byte) 0, (byte) 0, (byte) 0);
        Assert.assertEquals(jelly.getHeight(), 8);
    }

    @Test
    public void testGetWidth1() {
        final Jelly jelly = new JellyImpl((byte) 0x8D, 'B', false, (byte) 0, (byte) 0, (byte) 0, (byte) 0);
        Assert.assertEquals(jelly.getWidth(), 13);
    }

    @Test
    public void testGetHeight2() {
        final Jelly jelly = new JellyImpl((byte) 0xD8, 'B', false, (byte) 0, (byte) 0, (byte) 0, (byte) 0);
        Assert.assertEquals(jelly.getHeight(), 13);
    }

    @Test
    public void testGetWidth2() {
        final Jelly jelly = new JellyImpl((byte) 0xD8, 'B', false, (byte) 0, (byte) 0, (byte) 0, (byte) 0);
        Assert.assertEquals(jelly.getWidth(), 8);
    }

    @Test
    public void testMoveRight() {
        final Jelly jelly1 = new JellyImpl((byte) 0x55,
                                           'B',
                                           false,
                                           (byte) 2,
                                           (byte) 3,
                                           (byte) 1,
                                           (byte) 2,
                                           (byte) 0x12,
                                           (byte) 0x13,
                                           (byte) 0x22);
        final Jelly jelly2 = new JellyImpl((byte) 0x55,
                                           'B',
                                           false,
                                           (byte) 3,
                                           (byte) 4,
                                           (byte) 1,
                                           (byte) 2,
                                           (byte) 0x13,
                                           (byte) 0x14,
                                           (byte) 0x23);
        jelly1.moveRight();
        Assert.assertEquals(jelly1.toString(), jelly2.toString());
    }

    @Test
    public void testMoveDown() {
        final Jelly jelly1 = new JellyImpl((byte) 0x55,
                                           'B',
                                           false,
                                           (byte) 2,
                                           (byte) 3,
                                           (byte) 1,
                                           (byte) 2,
                                           (byte) 0x12,
                                           (byte) 0x13,
                                           (byte) 0x22);
        final Jelly jelly2 = new JellyImpl((byte) 0x55,
                                           'B',
                                           false,
                                           (byte) 2,
                                           (byte) 3,
                                           (byte) 2,
                                           (byte) 3,
                                           (byte) 0x22,
                                           (byte) 0x23,
                                           (byte) 0x32);
        jelly1.moveDown();
        Assert.assertEquals(jelly1.toString(), jelly2.toString());
    }


    @Test
    public void testOverlaps1() {
        final Jelly jelly1 = new JellyImpl((byte) 0x55,
                                           'B',
                                           false,
                                           (byte) 2,
                                           (byte) 3,
                                           (byte) 1,
                                           (byte) 2,
                                           (byte) 0x12,
                                           (byte) 0x13,
                                           (byte) 0x22);
        final Jelly jelly2 = new JellyImpl((byte) 0x55,
                                           'B',
                                           false,
                                           (byte) 1,
                                           (byte) 2,
                                           (byte) 2,
                                           (byte) 3,
                                           (byte) 0x21,
                                           (byte) 0x22,
                                           (byte) 0x31);
        Assert.assertTrue(jelly1.overlaps(jelly2));
    }

    @Test
    public void testOverlaps2() {
        final Jelly jelly1 = new JellyImpl((byte) 0x55,
                                           'B',
                                           false,
                                           (byte) 2,
                                           (byte) 3,
                                           (byte) 1,
                                           (byte) 2,
                                           (byte) 0x12,
                                           (byte) 0x13,
                                           (byte) 0x22);
        final Jelly jelly2 = new JellyImpl((byte) 0x55,
                                           'B',
                                           false,
                                           (byte) 1,
                                           (byte) 3,
                                           (byte) 2,
                                           (byte) 3,
                                           (byte) 0x21,
                                           (byte) 0x23,
                                           (byte) 0x31);
        Assert.assertFalse(jelly1.overlaps(jelly2));
    }
}
