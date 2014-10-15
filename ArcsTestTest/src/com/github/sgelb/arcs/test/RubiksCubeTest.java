package com.github.sgelb.arcs.test;

import java.util.Arrays;

import junit.framework.TestCase;
import android.util.Log;

import com.github.sgelb.arcs.Rotation;
import com.github.sgelb.arcs.RubiksCube;
import com.github.sgelb.arcs.Square;
import com.github.sgelb.arcs.SquareLocation;


public class RubiksCubeTest  extends TestCase {
	
	private RubiksCube cube;
	
	private SquareLocation locationA;
	private SquareLocation locationB;
	private SquareLocation locationC;
	
	public RubiksCubeTest() {
		cube = new RubiksCube();
	}
	
	public void testShouldRotateFront() {
		// First/Upper row on front side
		locationA = new SquareLocation(-1, 1, 1);
		locationB = new SquareLocation(0, 1, 1);
		locationC = new SquareLocation(1, 1, 1);
		cube.rotateFront();
		assertTrue(Arrays.equals(locationA.getLocationsAsArray(), cube.getCube()[0].getLocation().getLocationsAsArray()));
		assertTrue(Arrays.equals(locationB.getLocationsAsArray(), cube.getCube()[1].getLocation().getLocationsAsArray()));
		assertTrue(Arrays.equals(locationC.getLocationsAsArray(), cube.getCube()[2].getLocation().getLocationsAsArray()));
	}
	
	public void testShouldRotateRight() {
		// First/Upper row on right face
		locationA = new SquareLocation(1, 1, 1);
		locationB = new SquareLocation(1, 1, 0);
		locationC = new SquareLocation(1, 1, -1);
		cube.rotateRight();
		assertTrue(Arrays.equals(locationA.getLocationsAsArray(), cube.getCube()[9].getLocation().getLocationsAsArray()));
		assertTrue(Arrays.equals(locationB.getLocationsAsArray(), cube.getCube()[10].getLocation().getLocationsAsArray()));
		assertTrue(Arrays.equals(locationC.getLocationsAsArray(), cube.getCube()[11].getLocation().getLocationsAsArray()));
	}
	

	public void testShouldRotateBack() {
		// Right col on back side
		locationA = new SquareLocation(1, 1, -1);
		locationB = new SquareLocation(1, 0, -1);
		locationC = new SquareLocation(1, -1, -1);
		cube.rotateBack();
		assertTrue(Arrays.equals(locationA.getLocationsAsArray(), cube.getCube()[18].getLocation().getLocationsAsArray()));
		assertTrue(Arrays.equals(locationB.getLocationsAsArray(), cube.getCube()[21].getLocation().getLocationsAsArray()));
		assertTrue(Arrays.equals(locationC.getLocationsAsArray(), cube.getCube()[24].getLocation().getLocationsAsArray()));
	}
	
	
	public void testShouldRotateLeft() {
		// First/Upper row on left face
		locationA = new SquareLocation(-1, 1, -1);
		locationB = new SquareLocation(-1, 1, 0);
		locationC = new SquareLocation(-1, 1, 1);
		cube.rotateLeft();
		assertTrue(Arrays.equals(locationA.getLocationsAsArray(), cube.getCube()[27].getLocation().getLocationsAsArray()));
		assertTrue(Arrays.equals(locationB.getLocationsAsArray(), cube.getCube()[28].getLocation().getLocationsAsArray()));
		assertTrue(Arrays.equals(locationC.getLocationsAsArray(), cube.getCube()[29].getLocation().getLocationsAsArray()));
	}
	
	public void testShouldRotateDown() {
		// Right col on down side
		locationA = new SquareLocation(1, -1, 1);
		locationB = new SquareLocation(1, -1, 0);
		locationC = new SquareLocation(1, -1, -1);
		cube.rotateDown();
		assertTrue(Arrays.equals(locationA.getLocationsAsArray(), cube.getCube()[38].getLocation().getLocationsAsArray()));
		assertTrue(Arrays.equals(locationB.getLocationsAsArray(), cube.getCube()[41].getLocation().getLocationsAsArray()));
		assertTrue(Arrays.equals(locationC.getLocationsAsArray(), cube.getCube()[44].getLocation().getLocationsAsArray()));
	}
	
	public void testShouldRotateUp() {
		// First col on upper side 
		locationA = new SquareLocation(-1, 1, -1);
		locationB = new SquareLocation(-1, 1, 0);
		locationC = new SquareLocation(-1, 1, 1);
		cube.rotateUp();
		assertTrue(Arrays.equals(locationA.getLocationsAsArray(), cube.getCube()[45].getLocation().getLocationsAsArray()));
		assertTrue(Arrays.equals(locationB.getLocationsAsArray(), cube.getCube()[48].getLocation().getLocationsAsArray()));
		assertTrue(Arrays.equals(locationC.getLocationsAsArray(), cube.getCube()[51].getLocation().getLocationsAsArray()));
	}
	
	
	public void testShouldUpdateCube() {
		Square[] originalFrontFace = cube.getFace(Rotation.FRONT);
		originalFrontFace[0].setColor(Square.BLUE);
		cube.rotateUp();
		Square[] updatedFrontFace = cube.getFace(Rotation.FRONT);
		assertNotSame(originalFrontFace[0].getColor(), updatedFrontFace[0].getColor());
	}
	
	public void testInitialCube() {
		cube = new RubiksCube();
		for (Square square : cube.getCube()) {
			assertEquals(Square.UNSET_COLOR, square.getColor());
		}
	}
	
	public void testShouldRotateAndUpdateCube() {
		// new cube, all squares' colors are unset
		cube = new RubiksCube();

		// set upper left square on front face to red
		cube.getFace(Rotation.FRONT)[0].setColor(Square.BLUE);
		
		// debug: show blue square before rotation
		Square[] squares = cube.getCube();
		for (int i=0; i < squares.length; i++) {
			if (squares[i].getColor() == Square.BLUE) {
				Log.d("DEBUG", "Blue before: " + i);
				
				// location is wrong. y is -1, should be 1
				Log.d("DEBUG", "Pos: " + squares[i].getLocation().locationX +
						", " + squares[i].getLocation().locationY +
						", " + squares[i].getLocation().locationZ
						);
				
				// direction seem ok
				Log.d("DEBUG", "Dir: " + squares[i].getDirection().locationX + 
						", " + squares[i].getDirection().locationY +
						", " + squares[i].getDirection().locationZ
						);
			}
		}
		
		// get front face squares
		Square[] frontFace = cube.getFace(Rotation.FRONT);

		// rotate upper Face. Red square should be rotated to upper left square on left side,
		// cube.getFace(Rotation.LEFT)[0] (aka cube.getCube[27])
		cube.rotateUp();

		Square[] rightFace = cube.getFace(Rotation.RIGHT);

		// debug: show blue square after rotation
		squares = cube.getCube();
		for (int i=0; i < squares.length; i++) {
			if (squares[i].getColor() == Square.BLUE) {

				// i should be 9, is 6.
				Log.d("DEBUG", "Blue after: " + i);
				
				// unchanged, wrong.
				Log.d("DEBUG", "Pos: " + squares[i].getLocation().locationX +
						", " + squares[i].getLocation().locationY +
						", " + squares[i].getLocation().locationZ
						);
				
				// unchanged, wrong.
				Log.d("DEBUG", "Dir: " + squares[i].getDirection().locationX + 
						", " + squares[i].getDirection().locationY +
						", " + squares[i].getDirection().locationZ
						);
			}
		}
		
		// fails. in fact, the red square can be found on frontFace[6] or
		// cube.getCube()[6], which is wrong
		assertSame(frontFace[0].getColor(), rightFace[0].getColor());
	}

	public void testGetColors() {
		Square[] face = cube.getFace(Rotation.FRONT);
		face[0].setColor(Square.BLUE);
		String color = face[0].getColor();
		assertEquals(Square.BLUE, color);
	}

//	public void testShouldReturnFaces() {
//		testShouldUpdateCube();
//		for(int i = 0; i < 9; i++) {
//			System.out.println(squares.getRightFace()[i].getColor());
//		}
//	}

}
