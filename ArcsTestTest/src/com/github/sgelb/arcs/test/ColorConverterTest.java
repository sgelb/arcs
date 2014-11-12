package com.github.sgelb.arcs.test;

import com.github.sgelb.arcs.cube.ColorConverter;

import android.test.ActivityTestCase;

public class ColorConverterTest extends ActivityTestCase {

	public void testGetColorName() {
		assertEquals("ORANGE", ColorConverter.getColorName(0));
		assertEquals("BLUE", ColorConverter.getColorName(1));
		assertEquals("RED", ColorConverter.getColorName(2));
		assertEquals("GREEN", ColorConverter.getColorName(3));
		assertEquals("WHITE", ColorConverter.getColorName(4));
		assertEquals("YELLOW", ColorConverter.getColorName(5));
		assertEquals("UNSET", ColorConverter.getColorName(-1));
		assertEquals("UNSET", ColorConverter.getColorName(6));
	}
	
	public void testColorToSingmaster() {
		assertEquals("F", ColorConverter.colorToSingmaster(0));
		assertEquals("R", ColorConverter.colorToSingmaster(1));
		assertEquals("B", ColorConverter.colorToSingmaster(2));
		assertEquals("L", ColorConverter.colorToSingmaster(3));
		assertEquals("D", ColorConverter.colorToSingmaster(4));
		assertEquals("U", ColorConverter.colorToSingmaster(5));
		assertEquals("X", ColorConverter.colorToSingmaster(-1));
		assertEquals("X", ColorConverter.colorToSingmaster(6));
	}
	
	public void testGetUpperFaceColor() {
		assertEquals(ColorConverter.YELLOW, ColorConverter.getUpperFaceColor(0));
		assertEquals(ColorConverter.YELLOW, ColorConverter.getUpperFaceColor(1));
		assertEquals(ColorConverter.YELLOW, ColorConverter.getUpperFaceColor(2));
		assertEquals(ColorConverter.YELLOW, ColorConverter.getUpperFaceColor(3));
		assertEquals(ColorConverter.ORANGE, ColorConverter.getUpperFaceColor(4));
		assertEquals(ColorConverter.RED, ColorConverter.getUpperFaceColor(5));
		assertEquals(-1, ColorConverter.getUpperFaceColor(-1));
		assertEquals(-1, ColorConverter.getUpperFaceColor(6));
	}
	
	public void testGetUpperFaceColorName() {
		assertEquals("YELLOW", ColorConverter.getUpperFaceColorName(0));
		assertEquals("YELLOW", ColorConverter.getUpperFaceColorName(1));
		assertEquals("YELLOW", ColorConverter.getUpperFaceColorName(2));
		assertEquals("YELLOW", ColorConverter.getUpperFaceColorName(3));
		assertEquals("ORANGE", ColorConverter.getUpperFaceColorName(4));
		assertEquals("RED", ColorConverter.getUpperFaceColorName(5));
		assertEquals("UNSET", ColorConverter.getUpperFaceColorName(-1));
		assertEquals("UNSET", ColorConverter.getUpperFaceColorName(6));
	}
}
