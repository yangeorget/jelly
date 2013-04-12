package net.yangeorget.jelly;

import java.util.Arrays;

import org.testng.Assert;
import org.testng.annotations.Test;

public class JellyTest {	
	@Test
	public void testClone() {
		Jelly jelly = new Jelly(Arrays.asList(new Position(1, 2), new Position(1, 3), new Position(2, 2)));
		Assert.assertEquals(jelly.toString(), jelly.clone().toString());
	}
	
	@Test
	public void testUpdate() {
		Jelly jelly = new Jelly(Arrays.asList(new Position(1, 2), new Position(1, 3), new Position(2, 2)));
		String jellyAsString = jelly.toString();
		jelly.update(0, 1, 10, 10);
		jelly.update(0, -1, 10, 10);
		Assert.assertEquals(jelly.toString(), jellyAsString);
	}
	
	@Test
	public void testOverlaps1() {
		Jelly jelly1 = new Jelly(Arrays.asList(new Position(1, 2), new Position(1, 3), new Position(2, 2)));
		Jelly jelly2 = new Jelly(Arrays.asList(new Position(2, 1), new Position(3, 1), new Position(2, 2)));
		Assert.assertTrue(jelly1.overlaps(jelly2));
	}
	
	@Test
	public void testOverlaps2() {
		Jelly jelly1 = new Jelly(Arrays.asList(new Position(1, 2), new Position(1, 3), new Position(2, 2)));
		Jelly jelly2 = new Jelly(Arrays.asList(new Position(2, 1), new Position(3, 1), new Position(2, 3)));
		Assert.assertFalse(jelly1.overlaps(jelly2));
	}
}