package net.yangeorget.jelly;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = "fast")
public class NonRegressionTest {
    private static final Logger LOG = LoggerFactory.getLogger(NonRegressionTest.class);

    @Test
    public void test1() {
        final Board input = new BoardImpl(new String[] { "     A", "     #", " ABBA " }, new byte[] { 33, 34, 36 });
        final State state = new StateImpl(input);
        state.move(0, Board.LEFT);
        state.process();
        Assert.assertEquals(state.getJellies().length, 1);
        Assert.assertEquals(input.getLinkStarts().length, 3);
        Assert.assertEquals(input.getLinkEnds().length, 3);
        state.move(0, Board.LEFT);
        state.process();
        Assert.assertEquals(state.getJellies().length, 1);
        Assert.assertEquals(input.getLinkStarts().length, 3);
        Assert.assertEquals(input.getLinkEnds().length, 3);
    }

    @Test
    public void test2() {
        final Board input = new BoardImpl(new String[] { "           Y",
                                                        "       ### #",
                                                        "           Y",
                                                        "           #",
                                                        "            ",
                                                        "      YDDYY " }, new byte[] { 0x56, 0x58, 0x59 });
        State state = new StateImpl(input);
        state = state.clone();
        state.move(0, Board.LEFT);
        state.process();
        state = state.clone();
        state.move(0, Board.LEFT);
        state.process();
        Assert.assertEquals(state.getJellies().length, 1);
    }


    @Test
    public void test3() {
        final State state = new StateImpl(new BoardImpl(new String[] { "     ", "B B  ", "#####" },
                                                        new byte[] { 0x22 },
                                                        new char[] { 'B' }));
        state.process();
        state.move(0, Board.RIGHT);
        state.process();
        state.move(0, Board.RIGHT);
        state.process();
        state.move(1, Board.RIGHT);
        state.process();
        state.move(1, Board.RIGHT);
        state.process();
        Assert.assertEquals(state.getSerialization(), new byte[] { 4,
                                                                  Board.SPACE_BYTE,
                                                                  1,
                                                                  'B',
                                                                  2,
                                                                  Board.SPACE_BYTE,
                                                                  1,
                                                                  'B',
                                                                  1,
                                                                  Board.SPACE_BYTE,
                                                                  1,
                                                                  'B',
                                                                  5,
                                                                  Board.SPACE_BYTE,
                                                                  Board.SER_DELIM_BYTE,
                                                                  1 });
    }

    @Test
    public void test4() {
        final Board board = new BoardImpl(new String[] { "      #     ",
                                                        "      #     ",
                                                        "      #     ",
                                                        "      g     ",
                                                        "       B    ",
                                                        "###    G##  ",
                                                        "##   R R    ",
                                                        "###B########" }, new byte[] { 0x76 }, new char[] { 'R' });
        Assert.assertNotNull(new GameImpl(board).solve(false));
    }

    @Test
    public void test5() {
        final State state = new StateImpl(new BoardImpl(new String[] { "   ", "B B", "###" },
                                                        new byte[] { 0x22 },
                                                        new char[] { 'b' }));
        state.move(0, Board.RIGHT);
        state.process();
        Assert.assertEquals(state.getJellies().length, 1);
        Assert.assertEquals(state.getSerialization(), new byte[] { 1,
                                                                  Board.SPACE_BYTE,
                                                                  2,
                                                                  'B',
                                                                  2,
                                                                  Board.SPACE_BYTE,
                                                                  1,
                                                                  'b',
                                                                  3,
                                                                  Board.SPACE_BYTE,
                                                                  Board.SER_DELIM_BYTE,
                                                                  1 });
    }

    @Test
    public void test6() {
        final Board board = new BoardImpl(new String[] { "   ", "   ", "B  ", "###" },
                                          new byte[] { 0x31, 0x32 },
                                          new char[] { 'B', 'B' });
        final Game game = new GameImpl(board);
        final State state = game.solve(false);
        Assert.assertNotNull(state);
    }


    @Test
    public void test7() {
        final Board board = new BoardImpl(new String[] { "  ", " R", "B ", "##" },
                                          new byte[] { 0x20 },
                                          new char[] { 'R' });
        final State state = new StateImpl(board);
        state.move(1, Board.RIGHT);
        state.process();
        Assert.assertEquals(state.getSerialization(), new byte[] { 1,
                                                                  Board.SPACE_BYTE,
                                                                  1,
                                                                  'R',
                                                                  1,
                                                                  Board.SPACE_BYTE,
                                                                  1,
                                                                  'R',
                                                                  1,
                                                                  Board.SPACE_BYTE,
                                                                  1,
                                                                  'B',
                                                                  2,
                                                                  Board.SPACE_BYTE,
                                                                  Board.SER_DELIM_BYTE,
                                                                  0 });
    }


    @Test
    public void test8() {
        final Board board = new BoardImpl(new String[] { "#     R  ###",
                                                        "#BBBBBBBB###",
                                                        "#R       ###",
                                                        "#R        ##",
                                                        "#DDDDDDDD ##",
                                                        "#     R   ##",
                                                        "#FFFFFFFF###",
                                                        "#  R     ###",
                                                        "############" },
                                          new byte[] { -125, 22, 102 },
                                          new char[] { 'R', 'R', 'R' });
        final State state = new StateImpl(board);
        state.move(3, Board.RIGHT);
        state.process();
        Assert.assertEquals(state.getJellyPositionNb(), 32);
    }
}
