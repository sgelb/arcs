package com.github.sgelb.arcs.test;

import java.util.Arrays;

import junit.framework.TestCase;

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
	
	public void testShouldRotateRight() {
		locationA = new SquareLocation(1, 1, 1);
		locationB = new SquareLocation(1, 1, 0);
		locationC = new SquareLocation(1, 1, -1);
		cube.rotateRight();
		assertTrue(Arrays.equals(locationA.getLocationsAsArray(), cube.getCube()[45].getLocation().getLocationsAsArray()));
		assertTrue(Arrays.equals(locationB.getLocationsAsArray(), cube.getCube()[46].getLocation().getLocationsAsArray()));
		assertTrue(Arrays.equals(locationC.getLocationsAsArray(), cube.getCube()[47].getLocation().getLocationsAsArray()));
	}
	
	public void testShouldRotateLeft() {
		locationA = new SquareLocation(-1, 1, -1);
		locationB = new SquareLocation(-1, 1, 0);
		locationC = new SquareLocation(-1, 1, 1);
		cube.rotateLeft();
		assertTrue(Arrays.equals(locationA.getLocationsAsArray(), cube.getCube()[36].getLocation().getLocationsAsArray()));
		assertTrue(Arrays.equals(locationB.getLocationsAsArray(), cube.getCube()[37].getLocation().getLocationsAsArray()));
		assertTrue(Arrays.equals(locationC.getLocationsAsArray(), cube.getCube()[38].getLocation().getLocationsAsArray()));
	}
	
	public void testShouldRotateUp() {
		locationA = new SquareLocation(-1, 1, -1);
		locationB = new SquareLocation(-1, 1, 0);
		locationC = new SquareLocation(-1, 1, 1);
		cube.rotateUp();
		assertTrue(Arrays.equals(locationA.getLocationsAsArray(), cube.getCube()[9].getLocation().getLocationsAsArray()));
		assertTrue(Arrays.equals(locationB.getLocationsAsArray(), cube.getCube()[12].getLocation().getLocationsAsArray()));
		assertTrue(Arrays.equals(locationC.getLocationsAsArray(), cube.getCube()[15].getLocation().getLocationsAsArray()));
	}
	
	public void testShouldRotateDown() {
		locationA = new SquareLocation(1, -1, 1);
		locationB = new SquareLocation(1, -1, 0);
		locationC = new SquareLocation(1, -1, -1);
		cube.rotateDown();
		assertTrue(Arrays.equals(locationA.getLocationsAsArray(), cube.getCube()[29].getLocation().getLocationsAsArray()));
		assertTrue(Arrays.equals(locationB.getLocationsAsArray(), cube.getCube()[32].getLocation().getLocationsAsArray()));
		assertTrue(Arrays.equals(locationC.getLocationsAsArray(), cube.getCube()[35].getLocation().getLocationsAsArray()));
	}
	
	public void testShouldRotateFront() {
		locationA = new SquareLocation(-1, 1, 1);
		locationB = new SquareLocation(0, 1, 1);
		locationC = new SquareLocation(1, 1, 1);
		cube.rotateFront();
		assertTrue(Arrays.equals(locationA.getLocationsAsArray(), cube.getCube()[0].getLocation().getLocationsAsArray()));
		assertTrue(Arrays.equals(locationB.getLocationsAsArray(), cube.getCube()[1].getLocation().getLocationsAsArray()));
		assertTrue(Arrays.equals(locationC.getLocationsAsArray(), cube.getCube()[2].getLocation().getLocationsAsArray()));
	}
	
	public void testShouldRotateBack() {
		locationA = new SquareLocation(1, 1, -1);
		locationB = new SquareLocation(1, 0, -1);
		locationC = new SquareLocation(1, -1, -1);
		cube.rotateBack();
		assertTrue(Arrays.equals(locationA.getLocationsAsArray(), cube.getCube()[18].getLocation().getLocationsAsArray()));
		assertTrue(Arrays.equals(locationB.getLocationsAsArray(), cube.getCube()[21].getLocation().getLocationsAsArray()));
		assertTrue(Arrays.equals(locationC.getLocationsAsArray(), cube.getCube()[24].getLocation().getLocationsAsArray()));
	}
	
	public void testShouldUpdateCube() {
		Square[] originalFrontFace = cube.getFace(Rotation.FRONT);
		originalFrontFace[0].setColor(Square.BLUE);
		cube.rotateUp();
		Square[] updatedFrontFace = cube.getFace(Rotation.FRONT);
		assertNotSame(originalFrontFace[0].getColor(), updatedFrontFace[0].getColor());
	}
	
	public void testShouldRotateAndUpdateCube() {

		// new cube, all squares' colors are unset
		cube = new RubiksCube();

		// set upper left square on front face to red
		cube.getFace(Rotation.FRONT)[0].setColor(Square.BLUE);

		// get front face squares
		Square[] frontFace = cube.getFace(Rotation.FRONT);

		// rotate upper Face. Red square should be rotated to upper left square on left side,
		// cube.getFace(Rotation.LEFT)[0] (aka cube.getCube[27])
		cube.rotateUp();

		Square[] leftFace = cube.getFace(Rotation.LEFT);

		// fails. in fact, the red square can be found on frontFace[6] or
		// cube.getCube()[6]
		assertSame(frontFace[0].getColor(), leftFace[0].getColor());
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
