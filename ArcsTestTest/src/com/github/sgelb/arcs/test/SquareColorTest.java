package com.github.sgelb.arcs.test;

import com.github.sgelb.arcs.SquareColor;

import junit.framework.TestCase;

public class SquareColorTest extends TestCase {
	
	public void testGetString() {
		assertEquals(SquareColor.getString(SquareColor.ORANGE), "orange");
		assertEquals(SquareColor.getString(SquareColor.BLUE), "blue");
		assertEquals(SquareColor.getString(SquareColor.RED), "red");
		assertEquals(SquareColor.getString(SquareColor.GREEN), "green");
		assertEquals(SquareColor.getString(SquareColor.WHITE), "white");
		assertEquals(SquareColor.getString(SquareColor.YELLOW), "yellow");
		assertEquals(SquareColor.getString(SquareColor.UNSET_COLOR), "unset");
	}

}
