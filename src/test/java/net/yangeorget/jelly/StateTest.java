package net.yangeorget.jelly;

import org.testng.Assert;
import org.testng.annotations.Test;

public class StateTest {
	@Test
	public void testClone() {
		State state = new GameImpl(Boards.BOARD1).getStates().get(0);
		Assert.assertEquals(state.toString(), state.clone().toString());
	}

	@Test
	public void testMoveKO1() {
		testMoveKO(new String[] { " BBBB" }, 'B', 0, 0, 1);
	}

	@Test
	public void testMoveKO2() {
		testMoveKO(new String[] { " BBBG" }, 'B', 0, 0, 1);
	}

	@Test
	public void testMoveKO3() {
		testMoveKO(new String[] { " BBw " }, 'B', 0, 0, 1);
	}

	@Test
	public void testMoveKO4() {
		testMoveKO(new String[] { " BGw " }, 'B', 0, 0, 1);
	}

	private void testMoveKO(String[] board, char color, int index, int di,
			int dj) {
		Game game = new GameImpl(board);
		State state = game.getStates().get(0);
		Assert.assertNull(state.move(color, index, di, dj, game.getHeight(),
				game.getWidth()));
	}

	@Test
	public void testMoveOK1() {
		testMoveOK(new String[] { " BB  " }, 'B', 0, 0, 1,
				new String[] { "  BB " });
	}

	@Test
	public void testMoveOK2() {
		testMoveOK(new String[] { " BB  ", " GBB " }, 'G', 0, 0, 1,
				new String[] { "  BB ", "  GBB" });
	}
	
	@Test
	public void testMoveOK3() {
		testMoveOK(new String[] { 
				" BBYRR ", 
				" GBB R " }, 'B', 0, 0, 1,
				new String[] { 
				"  BBYRR", 
				" G BB R" });
	}

	private void testMoveOK(String[] board, char color, int index, int di,
			int dj, String[] eBoard) {
		Game game = new GameImpl(board);
		Boards.assertEquals(
				game.getStates()
						.get(0)
						.move(color, index, di, dj, game.getHeight(),
								game.getWidth())
						.toBoard(game.getHeight(), game.getWidth()),
				Boards.toCharMatrix(eBoard));
	}
}