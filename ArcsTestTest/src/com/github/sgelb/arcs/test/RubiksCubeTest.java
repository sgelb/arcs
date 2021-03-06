package com.github.sgelb.arcs.test;

import java.util.Arrays;

import junit.framework.TestCase;

import com.github.sgelb.arcs.cube.ColorConverter;
import com.github.sgelb.arcs.cube.Facelet;
import com.github.sgelb.arcs.cube.FaceletLocation;
import com.github.sgelb.arcs.cube.Rotator;
import com.github.sgelb.arcs.cube.RubiksCube;


public class RubiksCubeTest  extends TestCase {

	private RubiksCube cube;

	private FaceletLocation locationA;
	private FaceletLocation locationB;
	private FaceletLocation locationC;

	public RubiksCubeTest() {
		cube = new RubiksCube();
	}

	public void testShouldRotateFront() {
		// First/Upper row on front side
		locationA = new FaceletLocation(-1, 1, 1);
		locationB = new FaceletLocation(0, 1, 1);
		locationC = new FaceletLocation(1, 1, 1);
		assertTrue(Arrays.equals(locationA.getLocation(), cube.getFacelets()[0].getLocationsAsArray()));
		assertTrue(Arrays.equals(locationB.getLocation(), cube.getFacelets()[1].getLocationsAsArray()));
		assertTrue(Arrays.equals(locationC.getLocation(), cube.getFacelets()[2].getLocationsAsArray()));
	}

	public void testShouldRotateRight() {
		// First/Upper row on right face
		locationA = new FaceletLocation(1, 1, 1);
		locationB = new FaceletLocation(1, 1, 0);
		locationC = new FaceletLocation(1, 1, -1);
		assertTrue(Arrays.equals(locationA.getLocation(), cube.getFacelets()[9].getLocationsAsArray()));
		assertTrue(Arrays.equals(locationB.getLocation(), cube.getFacelets()[10].getLocationsAsArray()));
		assertTrue(Arrays.equals(locationC.getLocation(), cube.getFacelets()[11].getLocationsAsArray()));
	}


	public void testShouldRotateBack() {
		// Right col on back side
		locationA = new FaceletLocation(1, 1, -1);
		locationB = new FaceletLocation(1, 0, -1);
		locationC = new FaceletLocation(1, -1, -1);
		assertTrue(Arrays.equals(locationA.getLocation(), cube.getFacelets()[18].getLocationsAsArray()));
		assertTrue(Arrays.equals(locationB.getLocation(), cube.getFacelets()[21].getLocationsAsArray()));
		assertTrue(Arrays.equals(locationC.getLocation(), cube.getFacelets()[24].getLocationsAsArray()));
	}


	public void testShouldRotateLeft() {
		// First/Upper row on left face
		locationA = new FaceletLocation(-1, 1, -1);
		locationB = new FaceletLocation(-1, 1, 0);
		locationC = new FaceletLocation(-1, 1, 1);
		assertTrue(Arrays.equals(locationA.getLocation(), cube.getFacelets()[27].getLocationsAsArray()));
		assertTrue(Arrays.equals(locationB.getLocation(), cube.getFacelets()[28].getLocationsAsArray()));
		assertTrue(Arrays.equals(locationC.getLocation(), cube.getFacelets()[29].getLocationsAsArray()));
	}

	public void testShouldRotateDown() {
		// Right col on down side
		locationA = new FaceletLocation(1, -1, 1);
		locationB = new FaceletLocation(1, -1, 0);
		locationC = new FaceletLocation(1, -1, -1);
		assertTrue(Arrays.equals(locationA.getLocation(), cube.getFacelets()[38].getLocationsAsArray()));
		assertTrue(Arrays.equals(locationB.getLocation(), cube.getFacelets()[41].getLocationsAsArray()));
		assertTrue(Arrays.equals(locationC.getLocation(), cube.getFacelets()[44].getLocationsAsArray()));
	}

	public void testShouldRotateUp() {
		// First col on upper side
		locationA = new FaceletLocation(-1, 1, -1);
		locationB = new FaceletLocation(-1, 1, 0);
		locationC = new FaceletLocation(-1, 1, 1);
		assertTrue(Arrays.equals(locationA.getLocation(), cube.getFacelets()[45].getLocationsAsArray()));
		assertTrue(Arrays.equals(locationB.getLocation(), cube.getFacelets()[48].getLocationsAsArray()));
		assertTrue(Arrays.equals(locationC.getLocation(), cube.getFacelets()[51].getLocationsAsArray()));
	}

	public void testInitialCube() {
		cube = new RubiksCube();
		for (Facelet facelet : cube.getFacelets()) {
			assertEquals(ColorConverter.UNSET_COLOR, facelet.getColor());
		}
	}

	public void testShouldUpdateCube() {
		Facelet[] originalFrontFace = cube.getFace(Rotator.FRONT);
		originalFrontFace[0].setColor(ColorConverter.BLUE);
		cube.rotateUp();
		Facelet[] updatedFrontFace = cube.getFace(Rotator.FRONT);
		assertNotSame(originalFrontFace[0].getColor(), updatedFrontFace[0].getColor());
	}


	public void testShouldRotateFrontAndUpdateCube() {
		cube = new RubiksCube();
		cube.getFace(Rotator.FRONT)[0].setColor(ColorConverter.BLUE);
		cube.rotateFront();
		Facelet[] frontFace = cube.getFace(Rotator.FRONT);
		assertSame(frontFace[2].getColor(), ColorConverter.BLUE);
	}

	public void testShouldRotateRightAndUpdateCube() {
		cube = new RubiksCube();
		cube.getFace(Rotator.FRONT)[2].setColor(ColorConverter.BLUE);
		cube.rotateRight();
		Facelet[] upperFace = cube.getFace(Rotator.UP);
		assertSame(upperFace[2].getColor(), ColorConverter.BLUE);
	}

	public void testShouldRotateBackAndUpdateCube() {
		cube = new RubiksCube();
		cube.getFace(Rotator.UP)[0].setColor(ColorConverter.BLUE);
		cube.rotateBack();
		Facelet[] leftFace = cube.getFace(Rotator.LEFT);
		assertSame(leftFace[6].getColor(), ColorConverter.BLUE);
	}

	public void testShouldRotateLeftAndUpdateCube() {
		cube = new RubiksCube();
		cube.getFace(Rotator.FRONT)[0].setColor(ColorConverter.BLUE);
		cube.rotateLeft();
		Facelet[] downFace = cube.getFace(Rotator.DOWN);
		assertSame(downFace[0].getColor(), ColorConverter.BLUE);
	}

	public void testShouldRotateDownAndUpdateCube() {
		cube = new RubiksCube();
		cube.getFace(Rotator.FRONT)[6].setColor(ColorConverter.BLUE);
		cube.rotateDown();
		Facelet[] rightFace = cube.getFace(Rotator.RIGHT);
		assertSame(rightFace[6].getColor(), ColorConverter.BLUE);
	}

	public void testShouldRotateUpAndUpdateCube() {
		cube = new RubiksCube();
		cube.getFace(Rotator.FRONT)[0].setColor(ColorConverter.BLUE);
		cube.rotateUp();
		Facelet[] leftFace = cube.getFace(Rotator.LEFT);
		assertSame(leftFace[0].getColor(), ColorConverter.BLUE);
	}

	public void testGetColors() {
		Facelet[] face = cube.getFace(Rotator.FRONT);
		face[0].setColor(ColorConverter.BLUE);
		int color = face[0].getColor();
		assertEquals(ColorConverter.BLUE, color);
	}

}
