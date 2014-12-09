package com.github.sgelb.arcs.test;

import java.util.ArrayList;

import org.opencv.core.Point;
import org.opencv.core.Rect;

import android.test.ActivityTestCase;

import com.github.sgelb.arcs.input.ManualFaceInputMethod;

public class ManualFaceInputViewTest extends ActivityTestCase {

	private ManualFaceInputMethod manualFaceInputMethod;

	public ManualFaceInputViewTest() {
		manualFaceInputMethod = new ManualFaceInputMethod(getActivity());
	}

	public void testMovePointHorizontally() {
		Point a = new Point(1,1);
		Point b = new Point(2,2);
		int factor = 2;
		Point expected = new Point(-3, 1);
		assertEquals(expected, manualFaceInputMethod.movePointHorizontally(a, b, factor));
    }

	public void testMovePointVertically() {
		Point a = new Point(1,1);
		Point b = new Point(2,2);
		Point expected = new Point(1, 3);
		assertEquals(expected, manualFaceInputMethod.movePointVertically(a, b));
    }

	public void testCalculateSquareCoordinates() {
		ArrayList<Rect> expected = new ArrayList<Rect>();
		ArrayList<Rect> result = new ArrayList<Rect>();

		// first square
		Rect first = new Rect(new Point(1208, 72), new Point(1050, 230));
		expected.add(first);

		// last square
		Rect last = new Rect(new Point(792, 646), new Point(634, 488));
		expected.add(last);

		result = manualFaceInputMethod.calculateRectanglesCoordinates(720);
		assertEquals(expected.get(0), result.get(0));
		assertEquals(expected.get(1), result.get(8));
	}


}
