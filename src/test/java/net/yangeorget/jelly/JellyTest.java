package net.yangeorget.jelly;

import org.testng.Assert;
import org.testng.annotations.Test;

public class JellyTest {
    @Test
    public void testClone() {
        final Board board = new BoardImpl(new String[] { "     ", "     ", "     ", "     ", "     " });
        final State state = new StateImpl(board);
        final Jelly jelly = new JellyImpl(state,
                                          false,
                                          (byte) 2,
                                          (byte) 3,
                                          (byte) 1,
                                          (byte) 2,
                                          'B',
                                          (byte) 0x12,
                                          (byte) 0x13,
                                          (byte) 0x22);
        Assert.assertEquals(jelly.toString(), jelly.clone(state)
                                                   .toString());
    }

    @Test
    public void testMoveRight() {
        final Board board = new BoardImpl(new String[] { "     ", "     ", "     ", "     ", "     " });
        final State state = new StateImpl(board);
        final Jelly jelly1 = new JellyImpl(state,
                                           false,
                                           (byte) 2,
                                           (byte) 3,
                                           (byte) 1,
                                           (byte) 2,
                                           'B',
                                           (byte) 0x12,
                                           (byte) 0x13,
                                           (byte) 0x22);
        final Jelly jelly2 = new JellyImpl(state,
                                           false,
                                           (byte) 3,
                                           (byte) 4,
                                           (byte) 1,
                                           (byte) 2,
                                           'B',
                                           (byte) 0x13,
                                           (byte) 0x14,
                                           (byte) 0x23);
        jelly1.moveRight();
        Assert.assertEquals(jelly1.toString(), jelly2.toString());
    }

    @Test
    public void testMoveDown() {
        final Board board = new BoardImpl(new String[] { "     ", "     ", "     ", "     ", "     " });
        final State state = new StateImpl(board);
        final Jelly jelly1 = new JellyImpl(state,
                                           false,
                                           (byte) 2,
                                           (byte) 3,
                                           (byte) 1,
                                           (byte) 2,
                                           'B',
                                           (byte) 0x12,
                                           (byte) 0x13,
                                           (byte) 0x22);
        final Jelly jelly2 = new JellyImpl(state,
                                           false,
                                           (byte) 2,
                                           (byte) 3,
                                           (byte) 2,
                                           (byte) 3,
                                           'B',
                                           (byte) 0x22,
                                           (byte) 0x23,
                                           (byte) 0x32);
        jelly1.moveDown();
        Assert.assertEquals(jelly1.toString(), jelly2.toString());
    }


    @Test
    public void testOverlaps1() {
        final Board board = new BoardImpl(new String[] { "     ", "     ", "     ", "     ", "     " });
        final State state = new StateImpl(board);
        final Jelly jelly1 = new JellyImpl(state,
                                           false,
                                           (byte) 2,
                                           (byte) 3,
                                           (byte) 1,
                                           (byte) 2,
                                           'B',
                                           (byte) 0x12,
                                           (byte) 0x13,
                                           (byte) 0x22);
        final Jelly jelly2 = new JellyImpl(state,
                                           false,
                                           (byte) 1,
                                           (byte) 2,
                                           (byte) 2,
                                           (byte) 3,
                                           'B',
                                           (byte) 0x21,
                                           (byte) 0x22,
                                           (byte) 0x31);
        Assert.assertTrue(jelly1.overlaps(jelly2));
    }

    @Test
    public void testOverlaps2() {
        final Board board = new BoardImpl(new String[] { "     ", "     ", "     ", "     ", "     " });
        final State state = new StateImpl(board);
        final Jelly jelly1 = new JellyImpl(state,
                                           false,
                                           (byte) 2,
                                           (byte) 3,
                                           (byte) 1,
                                           (byte) 2,
                                           'B',
                                           (byte) 0x12,
                                           (byte) 0x13,
                                           (byte) 0x22);
        final Jelly jelly2 = new JellyImpl(state,
                                           false,
                                           (byte) 1,
                                           (byte) 3,
                                           (byte) 2,
                                           (byte) 3,
                                           'B',
                                           (byte) 0x21,
                                           (byte) 0x23,
                                           (byte) 0x31);
        Assert.assertFalse(jelly1.overlaps(jelly2));
    }
}
