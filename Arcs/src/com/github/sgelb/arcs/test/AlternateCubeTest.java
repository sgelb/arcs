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
	public void shouldRotateRight() {
		locationA = new SquareLocation(1, 1, 1);
		locationB = new SquareLocation(1, 1, 0);
		locationC = new SquareLocation(1, 1, -1);
		rot.setUpSquaresForRotation(Rotation.ROTATION_RIGHT);
		rot.rotateRight();
		assertArrayEquals(locationA.getLocationsAsArray(), rot.selectedSquares.get(0).getLocation().getLocationsAsArray());
		assertArrayEquals(locationB.getLocationsAsArray(), rot.selectedSquares.get(1).getLocation().getLocationsAsArray());
		assertArrayEquals(locationC.getLocationsAsArray(), rot.selectedSquares.get(2).getLocation().getLocationsAsArray());
	}
	
	@Test
	public void shouldRotateLeft() {
		locationA = new SquareLocation(-1, -1, -1);
		locationB = new SquareLocation(-1, -1, 0);
		locationC = new SquareLocation(-1, -1, 1);
		rot.setUpSquaresForRotation(Rotation.ROTATION_LEFT);
		rot.rotateLeft();
		assertArrayEquals(locationA.getLocationsAsArray(), rot.selectedSquares.get(0).getLocation().getLocationsAsArray());
		assertArrayEquals(locationB.getLocationsAsArray(), rot.selectedSquares.get(1).getLocation().getLocationsAsArray());
		assertArrayEquals(locationC.getLocationsAsArray(), rot.selectedSquares.get(2).getLocation().getLocationsAsArray());
	}
	
	@Test
	public void shouldRotateUp() {
		locationA = new SquareLocation(-1, 1, -1);
		locationB = new SquareLocation(-1, 1, 0);
		locationC = new SquareLocation(-1, 1, 1);
		rot.setUpSquaresForRotation(Rotation.ROTATION_UP);
		rot.rotateUp();
		assertArrayEquals(locationA.getLocationsAsArray(), rot.selectedSquares.get(0).getLocation().getLocationsAsArray());
		assertArrayEquals(locationB.getLocationsAsArray(), rot.selectedSquares.get(1).getLocation().getLocationsAsArray());
		assertArrayEquals(locationC.getLocationsAsArray(), rot.selectedSquares.get(2).getLocation().getLocationsAsArray());
	}
	
	@Test
	public void shouldRotateDown() {
		locationA = new SquareLocation(1, -1, 1);
		locationB = new SquareLocation(1, -1, 0);
		locationC = new SquareLocation(1, -1, -1);
		rot.setUpSquaresForRotation(Rotation.ROTATION_DOWN);
		rot.rotateDown();
		assertArrayEquals(locationA.getLocationsAsArray(), rot.selectedSquares.get(0).getLocation().getLocationsAsArray());
		assertArrayEquals(locationB.getLocationsAsArray(), rot.selectedSquares.get(1).getLocation().getLocationsAsArray());
		assertArrayEquals(locationC.getLocationsAsArray(), rot.selectedSquares.get(2).getLocation().getLocationsAsArray());
	}
	
	@Test
	public void shouldRotateFront() {
		locationA = new SquareLocation(-1, 1, 1);
		locationB = new SquareLocation(0, 1, 1);
		locationC = new SquareLocation(1, 1, 1);
		rot.setUpSquaresForRotation(Rotation.ROTATION_FRONT);
		rot.rotateFront();
		assertArrayEquals(locationA.getLocationsAsArray(), rot.selectedSquares.get(0).getLocation().getLocationsAsArray());
		assertArrayEquals(locationB.getLocationsAsArray(), rot.selectedSquares.get(1).getLocation().getLocationsAsArray());
		assertArrayEquals(locationC.getLocationsAsArray(), rot.selectedSquares.get(2).getLocation().getLocationsAsArray());
	}
	
	@Test
	public void shouldRotateBack() {
		locationA = new SquareLocation(1, -1, -1);
		locationB = new SquareLocation(1, 0, -1);
		locationC = new SquareLocation(1, 1, -1);
//		System.out.println(rot.toString());
		rot.setUpSquaresForRotation(Rotation.ROTATION_BACK);
		rot.rotateBack();
//		System.out.println(rot.toString());
		assertArrayEquals(locationA.getLocationsAsArray(), rot.selectedSquares.get(0).getLocation().getLocationsAsArray());
		assertArrayEquals(locationB.getLocationsAsArray(), rot.selectedSquares.get(1).getLocation().getLocationsAsArray());
		assertArrayEquals(locationC.getLocationsAsArray(), rot.selectedSquares.get(2).getLocation().getLocationsAsArray());
	}
	
	
	@Test
	public void shouldUpdateCube() {
		System.out.println(rot.toString());
		rot.setUpSquaresForRotation(Rotation.ROTATION_RIGHT);
		rot.rotateRight();
		rot.setUpSquaresForRotation(Rotation.ROTATION_FRONT);
		rot.rotateFront();
		rot.hugeUpdateMethod();
		System.out.println(rot.toString());
	}
	
	@Ignore
	@Test
	public void getColors() {
		for(Square square : rot.cube) {
			if(square.getLocation().locationX == -1 &&
					square.getLocation().locationY == -1 &&
					square.getLocation().locationZ == 1) {
				System.out.println(square.getColor());
			}
		}
	}

}
