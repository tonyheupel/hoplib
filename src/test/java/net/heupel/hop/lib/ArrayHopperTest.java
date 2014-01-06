package net.heupel.hop.lib;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class ArrayHopperTest {
	@Test
	public void testSimple() {
		int[] route = new int[] { 5, 6, 0, 4, 2, 4, 1, 0, 0, 4 };

		ArrayHopper hopper = new ArrayHopper(route);
		assertTrue(
			"The route should be [0, 5, 9, \"out\".]",
			Arrays.equals(new Object[] { 0, 5, 9, "out" }, hopper.shortestPathOut())
		);
	}


	@Test
	public void testFailure() {
		int[] route = new int[] { 5, 6, 0, 4, 2, 1, 1, 0, 0, 4 };

		ArrayHopper hopper = new ArrayHopper(route);

		assertTrue(
			"The route should be empty since it doesn't exist.",
			hopper.shortestPathOut().length == 0
		);
	}


	@Test
	public void testBacktracking() {
		int[] route = new int[] { 5, 2, 4, 2, 2, 0, 3, 0, 0, 4 };

		ArrayHopper hopper = new ArrayHopper(route);
		assertTrue(
			"The route should be [0, 4, 6, 9, \"out\".]",
			Arrays.equals(new Object[] { 0, 4, 6, 9, "out" }, hopper.shortestPathOut())
		);
	}
}
