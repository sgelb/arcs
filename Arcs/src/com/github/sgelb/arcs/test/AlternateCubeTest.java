package com.github.sgelb.arcs.test;

import org.junit.*;

import static org.junit.Assert.*;

import com.github.sgelb.arcs.Rotation;
import com.github.sgelb.arcs.SquareLocation;
import com.github.sgelb.arcs.Square;

public class AlternateCubeTest {
	
	private Rotation rot;
	private SquareLocation locationA;
	private SquareLocation locationB;
	private SquareLocation locationC;
	
	@Before
	public void setUp() {
		rot = new Rotation();
	}
	
	@Test
	public void rotateRight() {
		locationA = new SquareLocation(1, 1, 1);
		locationB = new SquareLocation(1, 1, 0);
		locationC = new SquareLocation(1, 1, -1);
		rot.setUpSquaresForRotation(Rotation.ROTATION_RIGHT);
		rot.rotateRight();
		assertArrayEquals(locationA.getLocationsAsArray(), rot.squares.get(0).getLocation().getLocationsAsArray());
		assertArrayEquals(locationB.getLocationsAsArray(), rot.squares.get(1).getLocation().getLocationsAsArray());
		assertArrayEquals(locationC.getLocationsAsArray(), rot.squares.get(2).getLocation().getLocationsAsArray());
	}
	
	@Test
	public void rotateLeft() {
		locationA = new SquareLocation(-1, -1, -1);
		locationB = new SquareLocation(-1, -1, 0);
		locationC = new SquareLocation(-1, -1, 1);
		rot.setUpSquaresForRotation(Rotation.ROTATION_LEFT);
		rot.rotateLeft();
		assertArrayEquals(locationA.getLocationsAsArray(), rot.squares.get(0).getLocation().getLocationsAsArray());
		assertArrayEquals(locationB.getLocationsAsArray(), rot.squares.get(1).getLocation().getLocationsAsArray());
		assertArrayEquals(locationC.getLocationsAsArray(), rot.squares.get(2).getLocation().getLocationsAsArray());
	}
	
	@Test
	public void rotateUp() {
		locationA = new SquareLocation(-1, 1, -1);
		locationB = new SquareLocation(-1, 1, 0);
		locationC = new SquareLocation(-1, 1, 1);
		rot.setUpSquaresForRotation(Rotation.ROTATION_UP);
		rot.rotateUp();
		assertArrayEquals(locationA.getLocationsAsArray(), rot.squares.get(0).getLocation().getLocationsAsArray());
		assertArrayEquals(locationB.getLocationsAsArray(), rot.squares.get(1).getLocation().getLocationsAsArray());
		assertArrayEquals(locationC.getLocationsAsArray(), rot.squares.get(2).getLocation().getLocationsAsArray());
	}
	
	@Test
	public void rotateDown() {
		locationA = new SquareLocation(1, -1, 1);
		locationB = new SquareLocation(1, -1, 0);
		locationC = new SquareLocation(1, -1, -1);
		rot.setUpSquaresForRotation(Rotation.ROTATION_DOWN);
		rot.rotateDown();
		assertArrayEquals(locationA.getLocationsAsArray(), rot.squares.get(0).getLocation().getLocationsAsArray());
		assertArrayEquals(locationB.getLocationsAsArray(), rot.squares.get(1).getLocation().getLocationsAsArray());
		assertArrayEquals(locationC.getLocationsAsArray(), rot.squares.get(2).getLocation().getLocationsAsArray());
	}
	
	@Test
	public void rotateFront() {
		locationA = new SquareLocation(-1, 1, 1);
		locationB = new SquareLocation(0, 1, 1);
		locationC = new SquareLocation(1, 1, 1);
		rot.setUpSquaresForRotation(Rotation.ROTATION_FRONT);
		rot.rotateFront();
		assertArrayEquals(locationA.getLocationsAsArray(), rot.squares.get(0).getLocation().getLocationsAsArray());
		assertArrayEquals(locationB.getLocationsAsArray(), rot.squares.get(1).getLocation().getLocationsAsArray());
		assertArrayEquals(locationC.getLocationsAsArray(), rot.squares.get(2).getLocation().getLocationsAsArray());
	}
	
	@Test
	public void rotateBack() {
		locationA = new SquareLocation(1, -1, -1);
		locationB = new SquareLocation(1, 0, -1);
		locationC = new SquareLocation(1, 1, -1);
		rot.setUpSquaresForRotation(Rotation.ROTATION_BACK);
		rot.rotateBack();
		assertArrayEquals(locationA.getLocationsAsArray(), rot.squares.get(0).getLocation().getLocationsAsArray());
		assertArrayEquals(locationB.getLocationsAsArray(), rot.squares.get(1).getLocation().getLocationsAsArray());
		assertArrayEquals(locationC.getLocationsAsArray(), rot.squares.get(2).getLocation().getLocationsAsArray());
	}

}
