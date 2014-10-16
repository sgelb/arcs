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
	
	public void testShouldRotateFront() {
		// First/Upper row on front side
		locationA = new SquareLocation(-1, 1, 1);
		locationB = new SquareLocation(0, 1, 1);
		locationC = new SquareLocation(1, 1, 1);
		cube.rotateFront();
		assertTrue(Arrays.equals(locationA.getLocation(), cube.getCube()[0].getLocationsAsArray()));
		assertTrue(Arrays.equals(locationB.getLocation(), cube.getCube()[1].getLocationsAsArray()));
		assertTrue(Arrays.equals(locationC.getLocation(), cube.getCube()[2].getLocationsAsArray()));
	}
	
	public void testShouldRotateRight() {
		// First/Upper row on right face
		locationA = new SquareLocation(1, 1, 1);
		locationB = new SquareLocation(1, 1, 0);
		locationC = new SquareLocation(1, 1, -1);
		cube.rotateRight();
		assertTrue(Arrays.equals(locationA.getLocation(), cube.getCube()[9].getLocationsAsArray()));
		assertTrue(Arrays.equals(locationB.getLocation(), cube.getCube()[10].getLocationsAsArray()));
		assertTrue(Arrays.equals(locationC.getLocation(), cube.getCube()[11].getLocationsAsArray()));
	}
	

	public void testShouldRotateBack() {
		// Right col on back side
		locationA = new SquareLocation(1, 1, -1);
		locationB = new SquareLocation(1, 0, -1);
		locationC = new SquareLocation(1, -1, -1);
		cube.rotateBack();
		assertTrue(Arrays.equals(locationA.getLocation(), cube.getCube()[18].getLocationsAsArray()));
		assertTrue(Arrays.equals(locationB.getLocation(), cube.getCube()[21].getLocationsAsArray()));
		assertTrue(Arrays.equals(locationC.getLocation(), cube.getCube()[24].getLocationsAsArray()));
	}
	
	
	public void testShouldRotateLeft() {
		// First/Upper row on left face
		locationA = new SquareLocation(-1, 1, -1);
		locationB = new SquareLocation(-1, 1, 0);
		locationC = new SquareLocation(-1, 1, 1);
		cube.rotateLeft();
		assertTrue(Arrays.equals(locationA.getLocation(), cube.getCube()[27].getLocationsAsArray()));
		assertTrue(Arrays.equals(locationB.getLocation(), cube.getCube()[28].getLocationsAsArray()));
		assertTrue(Arrays.equals(locationC.getLocation(), cube.getCube()[29].getLocationsAsArray()));
	}
	
	public void testShouldRotateDown() {
		// Right col on down side
		locationA = new SquareLocation(1, -1, 1);
		locationB = new SquareLocation(1, -1, 0);
		locationC = new SquareLocation(1, -1, -1);
		cube.rotateDown();
		assertTrue(Arrays.equals(locationA.getLocation(), cube.getCube()[38].getLocationsAsArray()));
		assertTrue(Arrays.equals(locationB.getLocation(), cube.getCube()[41].getLocationsAsArray()));
		assertTrue(Arrays.equals(locationC.getLocation(), cube.getCube()[44].getLocationsAsArray()));
	}
	
	public void testShouldRotateUp() {
		// First col on upper side 
		locationA = new SquareLocation(-1, 1, -1);
		locationB = new SquareLocation(-1, 1, 0);
		locationC = new SquareLocation(-1, 1, 1);
		cube.rotateUp();
		assertTrue(Arrays.equals(locationA.getLocation(), cube.getCube()[45].getLocationsAsArray()));
		assertTrue(Arrays.equals(locationB.getLocation(), cube.getCube()[48].getLocationsAsArray()));
		assertTrue(Arrays.equals(locationC.getLocation(), cube.getCube()[51].getLocationsAsArray()));
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

	public void testShouldRotateFrontAndUpdateCube() {
		cube = new RubiksCube();
		cube.getFace(Rotation.FRONT)[0].setColor(Square.BLUE);
		cube.rotateFront();
		Square[] frontFace = cube.getFace(Rotation.FRONT);
		assertSame(frontFace[2].getColor(), Square.BLUE);
	}
	
	public void testShouldRotateRightAndUpdateCube() {
		cube = new RubiksCube();
		cube.getFace(Rotation.FRONT)[2].setColor(Square.BLUE);
		cube.rotateRight();
		Square[] upperFace = cube.getFace(Rotation.UP);
		assertSame(upperFace[2].getColor(), Square.BLUE);
	}
	
	public void testShouldRotateBackAndUpdateCube() {
		cube = new RubiksCube();
		cube.getFace(Rotation.UP)[0].setColor(Square.BLUE);
		cube.rotateBack();
		Square[] leftFace = cube.getFace(Rotation.LEFT);
		assertSame(leftFace[6].getColor(), Square.BLUE);
	}
	
	public void testShouldRotateLeftAndUpdateCube() {
		cube = new RubiksCube();
		cube.getFace(Rotation.FRONT)[0].setColor(Square.BLUE);
		cube.rotateLeft();
		Square[] downFace = cube.getFace(Rotation.DOWN);
		assertSame(downFace[0].getColor(), Square.BLUE);
	}
	
	public void testShouldRotateDownAndUpdateCube() {
		cube = new RubiksCube();
		cube.getFace(Rotation.FRONT)[6].setColor(Square.BLUE);
		cube.rotateDown();
		Square[] rightFace = cube.getFace(Rotation.RIGHT);
		assertSame(rightFace[6].getColor(), Square.BLUE);
	}
	
	public void testShouldRotateUpAndUpdateCube() {
		cube = new RubiksCube();
		cube.getFace(Rotation.FRONT)[0].setColor(Square.BLUE);
		cube.rotateUp();
		Square[] leftFace = cube.getFace(Rotation.LEFT);
		assertSame(leftFace[0].getColor(), Square.BLUE);
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
