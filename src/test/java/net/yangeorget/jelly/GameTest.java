package net.yangeorget.jelly;

import java.util.LinkedList;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GameTest {
    @Test
    public void testGame1() {
        testJellies(new BoardImpl("BBBB", "  BY", "  YY", "YB  "), 0, 0, 4, 2);
    }

    @Test
    public void testGame2() {
        testJellies(new BoardImpl("BBBB", "  BB", "  BB", "BB  "), 0, 0, 2, 1);
    }

    @Test
    public void testGame3() {
        testJellies(new BoardImpl("BYBB", "  GG", "  BB", " B  "), 0, 0, 6, 3);
    }

    @Test
    public void testGameBoard1() {
        testJellies(Board.BOARD1, 4, 1, 6, 3);
    }

    private void testJellies(final Board board,
                             final int jelliesFixed,
                             final int colorsFixed,
                             final int jelliesFloating,
                             final int colorsFloating) {
        final Game game = new GameImpl(board);
        final State state = game.getStates()
                                .get(0);
        final List<Jelly> fixedJellies = new LinkedList<Jelly>();
        for (final List<Jelly> jellies : state.getFixedJellies()
                                              .values()) {
            fixedJellies.addAll(jellies);
        }
        Assert.assertEquals(fixedJellies.size(), jelliesFixed);
        Assert.assertEquals(state.getFixedJellies()
                                 .size(), colorsFixed);
        final List<Jelly> floating = new LinkedList<Jelly>();
        for (final List<Jelly> jellies : state.getFloatingJellies()
                                              .values()) {
            floating.addAll(jellies);
        }
        Assert.assertEquals(floating.size(), jelliesFloating);
        Assert.assertEquals(state.getFloatingJellies()
                                 .size(), colorsFloating);
    }
}
