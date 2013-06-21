package net.yangeorget.jelly;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class NonRegressionTest {
    private static final Logger LOG = LoggerFactory.getLogger(NonRegressionTest.class);

    @Test
    public void testNonRegression1() {
        final Board input = new BoardImpl(new String[] { "     A", "     #", " ABBA " }, new byte[] { 33, 34, 36 });
        final State state = new StateImpl(input);
        state.moveLeft(0);
        state.process();
        Assert.assertTrue(state.getSerialization()
                               .startsWith("A# ABBA "));
        Assert.assertEquals(state.getJellies().length, 1);
        Assert.assertEquals(input.getLinkStarts().length, 3);
        Assert.assertEquals(input.getLinkEnds().length, 3);

        state.moveLeft(0);
        state.process();
        Assert.assertTrue(state.getSerialization()
                               .startsWith("A #ABBA  "));
        Assert.assertEquals(state.getJellies().length, 1);
        Assert.assertEquals(input.getLinkStarts().length, 3);
        Assert.assertEquals(input.getLinkEnds().length, 3);

    }

    @Test
    public void testNonRegression2() {
        final Board input = new BoardImpl(new String[] { "           Y",
                                                        "       ### #",
                                                        "           Y",
                                                        "           #",
                                                        "            ",
                                                        "      YDDYY " }, new byte[] { 0x56, 0x58, 0x59 });
        State state = new StateImpl(input);
        state = state.clone();
        state.moveLeft(0);
        state.process();
        state = state.clone();
        state.moveLeft(0);
        state.process();
        Assert.assertEquals(state.getJellies().length, 1);
    }


    @Test
    public void testNonRegression3() {
        final State state = new StateImpl(new BoardImpl(new String[] { "     ", "B B  " },
                                                        new byte[] { 0x12 },
                                                        new char[] { 'B' }));
        state.process();
        state.moveRight(0);
        state.process();
        state.moveRight(0);
        state.process();
        state.moveRight(1);
        state.process();
        state.moveRight(1);
        state.process();
        Assert.assertEquals(state.getSerialization(), "[B  B B[true][][]]");
    }
}
