package com.github.sgelb.arcs.test;

import org.junit.*;

import static org.junit.Assert.*;

import com.github.sgelb.arcs.Rotation;
import com.github.sgelb.arcs.SquareLocation;
import com.github.sgelb.arcs.Square;

public class AlternateCubeTest {
	
//	private AlternateCube cube;
//	private AlternateCube testCube;
	private Rotation rot;
	private SquareLocation locationA;
	private SquareLocation locationB;
	private SquareLocation locationC;
	
	@Before
	public void setUp() {
		rot = new Rotation();
		locationA = new SquareLocation(-1, 1, -1);
		locationB = new SquareLocation(-1, 0, -1);
		locationC = new SquareLocation(-1, -1, -1);
	}
	
	@Ignore
	@Test
	public void rotateRight() {
		rot.setUpSquaresForRotation(Rotation.ROTATION_RIGHT);
		rot.rotateRight();
		assertArrayEquals(locationA.getLocationsAsArray(), rot.squares.get(0).getLocation().getLocationsAsArray());
		assertArrayEquals(locationB.getLocationsAsArray(), rot.squares.get(1).getLocation().getLocationsAsArray());
		assertArrayEquals(locationC.getLocationsAsArray(), rot.squares.get(2).getLocation().getLocationsAsArray());
	}
	
	@Ignore
	@Test
	public void rotateLeft() {
		rot.setUpSquaresForRotation(Rotation.ROTATION_LEFT);
		rot.rotateLeft();
		assertArrayEquals(locationA.getLocationsAsArray(), rot.squares.get(0).getLocation().getLocationsAsArray());
		assertArrayEquals(locationB.getLocationsAsArray(), rot.squares.get(1).getLocation().getLocationsAsArray());
		assertArrayEquals(locationC.getLocationsAsArray(), rot.squares.get(2).getLocation().getLocationsAsArray());
	}
	
	@Ignore
	@Test
	public void rotateTop() {
		rot.setUpSquaresForRotation(Rotation.ROTATION_UP);
		rot.rotateUp();
		assertArrayEquals(locationA.getLocationsAsArray(), rot.squares.get(0).getLocation().getLocationsAsArray());
		assertArrayEquals(locationB.getLocationsAsArray(), rot.squares.get(1).getLocation().getLocationsAsArray());
		assertArrayEquals(locationC.getLocationsAsArray(), rot.squares.get(2).getLocation().getLocationsAsArray());
	}
	
	@Ignore
	@Test
	public void rotateDown() {
		rot.setUpSquaresForRotation(Rotation.ROTATION_DOWN);
		rot.rotateDown();
		assertArrayEquals(locationA.getLocationsAsArray(), rot.squares.get(0).getLocation().getLocationsAsArray());
		assertArrayEquals(locationB.getLocationsAsArray(), rot.squares.get(1).getLocation().getLocationsAsArray());
		assertArrayEquals(locationC.getLocationsAsArray(), rot.squares.get(2).getLocation().getLocationsAsArray());
	}
	
	@Ignore
	@Test
	public void rotateFront() {
		rot.setUpSquaresForRotation(Rotation.ROTATION_FRONT);
		rot.rotateFront();
		assertArrayEquals(locationA.getLocationsAsArray(), rot.squares.get(0).getLocation().getLocationsAsArray());
		assertArrayEquals(locationB.getLocationsAsArray(), rot.squares.get(1).getLocation().getLocationsAsArray());
		assertArrayEquals(locationC.getLocationsAsArray(), rot.squares.get(2).getLocation().getLocationsAsArray());
	}
	
	@Test
	public void rotateBack() {
		rot.setUpSquaresForRotation(Rotation.ROTATION_BACK);
		rot.rotateBack();
		assertArrayEquals(locationA.getLocationsAsArray(), rot.squares.get(0).getLocation().getLocationsAsArray());
		assertArrayEquals(locationB.getLocationsAsArray(), rot.squares.get(1).getLocation().getLocationsAsArray());
		assertArrayEquals(locationC.getLocationsAsArray(), rot.squares.get(2).getLocation().getLocationsAsArray());
	}

}
