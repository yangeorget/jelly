package net.yangeorget.jelly;

import org.testng.Assert;
import org.testng.annotations.Test;

public class StateTest {
	@Test
	public void testClone() {
		State state = new Game(Boards.BOARD1).getStates().get(0);
		Assert.assertEquals(state.toString(), state.clone().toString());
	}
	
	@Test
	public void testMove1() {
		testMoveKO(new String[] {
				"BBBB",
				"    ",
				"    ",
				"    "}, 'B', 0, 0, 1);
	}

	@Test
	public void testMove2() {
		testMoveKO(new String[] {
				"BBBG",
				"    ",
				"    ",
				"    "}, 'B', 0, 0, 1);
	}
	
	private void testMoveKO(String[] board, char color, int index, int di, int dj) {
		Game game = new Game(board);
		State state = game.getStates().get(0);
		Assert.assertNull(state.move(color, index, di, dj, game.getHeight(), game.getWidth()));		
	}
}