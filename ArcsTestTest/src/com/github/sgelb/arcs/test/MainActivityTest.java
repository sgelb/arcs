package com.github.sgelb.arcs.test;

import java.util.ArrayList;
import java.util.HashMap;

import org.opencv.core.Point;

import android.test.ActivityInstrumentationTestCase2;

import com.github.sgelb.arcs.MainActivity;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

	private MainActivity activity;
	
	public MainActivityTest() {
		super(MainActivity.class);
	}
	
	protected void setUp() throws Exception {
	    super.setUp();
	    activity = getActivity();
	  }
	
	public void testPreconditions() {
	    assertNotNull("MainActivity is null", activity);
	}
	
	public void testMovePointDiagonally() {
		Point a = new Point(1,1);
		Point b = new Point(2,2);
		Point expected = new Point(3, 3);
		assertEquals(expected, activity.movePointDiagonally(a, b));
    }
	
	public void testMovePointHorizontally() {
		Point a = new Point(1,1);
		Point b = new Point(2,2);
		Point expected = new Point(3, 1);
		assertEquals(expected, activity.movePointHorizontally(a, b));
    }
	
	public void testMovePointVertically() {
		Point a = new Point(1,1);
		Point b = new Point(2,2);
		Point expected = new Point(1, 3);
		assertEquals(expected, activity.movePointVertically(a, b));
    }
	
	public void testCalculateSquareCoordinates() {
		ArrayList<HashMap<String, Point>> expected = new ArrayList<HashMap<String,Point>>();
		ArrayList<HashMap<String, Point>> result = new ArrayList<HashMap<String,Point>>();
		
		// first square
		HashMap<String, Point> first = new HashMap<>();
		first.put("tl", new Point(1208, 72));
		first.put("br", new Point(1050, 230));
		expected.add(first);
		
		// last square
		HashMap<String, Point> last = new HashMap<>();
		last.put("tl", new Point(792, 488));
		last.put("br", new Point(634, 646));
		expected.add(last);
		
		result = activity.calculateSquareCoordinates(1280, 720);
		assertEquals(expected.get(0), result.get(0));
		assertEquals(expected.get(1), result.get(8));
	}
    
	
}
