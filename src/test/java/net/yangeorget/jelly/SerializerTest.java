package net.yangeorget.jelly;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = "fast")
public class SerializerTest {
    private static final Logger LOG = LoggerFactory.getLogger(SerializerTest.class);

    @Test
    public void testSerialize1() {
        testSerialize(new BoardImpl(new String[] { "ABA", "ABB" }), new byte[] { 1,
                                                                                'A',
                                                                                1,
                                                                                'B',
                                                                                2,
                                                                                'A',
                                                                                2,
                                                                                'B',
                                                                                Board.SER_DELIM_BYTE,
                                                                                Board.SER_DELIM_BYTE,
                                                                                0 });
    }

    @Test
    public void testSerialize2() {
        testSerialize(new BoardImpl(new String[] { "ABAABB" }), new byte[] { 1,
                                                                            'A',
                                                                            1,
                                                                            'B',
                                                                            2,
                                                                            'A',
                                                                            2,
                                                                            'B',
                                                                            Board.SER_DELIM_BYTE,
                                                                            Board.SER_DELIM_BYTE,
                                                                            0 });
    }

    @Test
    public void testSerialize3() {
        testSerialize(new BoardImpl(new String[] { "#     ", "ABAABB" }), new byte[] { 6,
                                                                                      Board.SPACE_BYTE,
                                                                                      1,
                                                                                      'A',
                                                                                      1,
                                                                                      'B',
                                                                                      2,
                                                                                      'A',
                                                                                      2,
                                                                                      'B',
                                                                                      Board.SER_DELIM_BYTE,
                                                                                      Board.SER_DELIM_BYTE,
                                                                                      0 });
    }

    @Test
    public void testSerialize4() {
        testSerialize(new BoardImpl(new String[] { "AAAABB", "BBAAAA", "      ", "      ", "      ", "ABBAAA" },
                                    new byte[] { 0x50, 0x51 }), new byte[] { 4,
                                                                            'A',
                                                                            4,
                                                                            'B',
                                                                            4,
                                                                            'A',
                                                                            18,
                                                                            Board.SPACE_BYTE,
                                                                            1,
                                                                            'A',
                                                                            2,
                                                                            'B',
                                                                            3,
                                                                            'A',
                                                                            Board.SER_DELIM_BYTE,
                                                                            0x51,
                                                                            0x50,
                                                                            0x50,
                                                                            0x51,
                                                                            Board.SER_DELIM_BYTE,
                                                                            0 });
    }

    @Test
    public void testSerialize5() {
        testSerialize(new BoardImpl(new String[] { "AAAABB", "BBAAAA", "      ", "      ", "      ", "ABBAAA" },
                                    new byte[] { 0x50, 0x51, 0x53 }), new byte[] { 4,
                                                                                  'A',
                                                                                  4,
                                                                                  'B',
                                                                                  4,
                                                                                  'A',
                                                                                  18,
                                                                                  Board.SPACE_BYTE,
                                                                                  1,
                                                                                  'A',
                                                                                  2,
                                                                                  'B',
                                                                                  3,
                                                                                  'A',
                                                                                  Board.SER_DELIM_BYTE,
                                                                                  0x53,
                                                                                  0x50,
                                                                                  0x51,
                                                                                  0x50,
                                                                                  0x51,
                                                                                  0x53,
                                                                                  Board.SER_DELIM_BYTE,
                                                                                  0 });
    }

    // TODO: tests with #
    // TODO: test size of serialization
    // TODO: write tests in the case of emerging jellies

    private void testSerialize(final Board board, final byte[] serialization) {
        Assert.assertEquals(new StateImpl(board).getSerialization(), serialization);
    }
}
