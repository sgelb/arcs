package com.github.sgelb.arcs.test;

import com.github.sgelb.arcs.SquareColor;

import junit.framework.TestCase;

public class SquareColorTest extends TestCase {
	
	public void testGetString() {
		assertEquals(SquareColor.getColorString(SquareColor.ORANGE), "orange");
		assertEquals(SquareColor.getColorString(SquareColor.BLUE), "blue");
		assertEquals(SquareColor.getColorString(SquareColor.RED), "red");
		assertEquals(SquareColor.getColorString(SquareColor.GREEN), "green");
		assertEquals(SquareColor.getColorString(SquareColor.WHITE), "white");
		assertEquals(SquareColor.getColorString(SquareColor.YELLOW), "yellow");
		assertEquals(SquareColor.getColorString(SquareColor.UNSET_COLOR), "unset");
	}

}
