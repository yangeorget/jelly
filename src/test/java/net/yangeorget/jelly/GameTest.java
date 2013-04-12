package net.yangeorget.jelly;

import java.util.LinkedList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

public class GameTest {
	@Test
	public void testGame1() {
		testJellies(new String[] {
				"BBBB",
				"  BY",
				"  YY",
				"YB  "}, 0, 0, 4, 2);
	}

	@Test
	public void testGame2() {
		testJellies(new String[] {
				"BBBB",
				"  BB",
				"  BB",
				"BB  "				
		}, 0, 0, 2, 1);
	}
	
	@Test
	public void testGame3() {
		testJellies(new String[] {
				"BYBB",
				"  GG",
				"  BB",
				" B  "				
		}, 0, 0, 6, 3);
	}
	
	@Test
	public void testGameBoard1() {
		testJellies(Boards.BOARD1, 4, 1, 6, 3);
	}
	
	private void testJellies(String[] board, int jelliesFixed, int colorsFixed, int jelliesFloating, int colorsFloating) {
		Game game = new Game(board);
		System.out.println(game.toString());
		State state = game.getStates().get(0);
		List<Jelly> fixedJellies = new LinkedList<Jelly>();
		for (List<Jelly> jellies : state.getFixedJellies().values()) {
			fixedJellies.addAll(jellies);
		}
		Assert.assertEquals(fixedJellies.size(), jelliesFixed);
		Assert.assertEquals(state.getFixedJellies().size(), colorsFixed);		
		List<Jelly> floating = new LinkedList<Jelly>();
		for (List<Jelly> jellies : state.getFloatingJellies().values()) {
			floating.addAll(jellies);
		}
		Assert.assertEquals(floating.size(), jelliesFloating);
		Assert.assertEquals(state.getFloatingJellies().size(), colorsFloating);
	}
}