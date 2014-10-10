package com.github.sgelb.arcs.test;

import org.junit.*;

import static org.junit.Assert.*;

import com.github.sgelb.arcs.RubiksCube;
import com.github.sgelb.arcs.Rotation;
import com.github.sgelb.arcs.SquareLocation;
import com.github.sgelb.arcs.Square;

public class RubiksCubeTest {
	
	private RubiksCube cube;
	
	private SquareLocation locationA;
	private SquareLocation locationB;
	private SquareLocation locationC;
	
	@Before
	public void setUp() {
		cube = new RubiksCube();
	}
	
	@Test
	public void shouldRotateRight() {
		locationA = new SquareLocation(1, 1, 1);
		locationB = new SquareLocation(1, 1, 0);
		locationC = new SquareLocation(1, 1, -1);
		cube.rotation.setUpSquaresForRotation(Rotation.ROTATION_RIGHT);
		cube.rotation.rotateRight();
		assertArrayEquals(locationA.getLocationsAsArray(), cube.getCube()[45].getLocation().getLocationsAsArray());
		assertArrayEquals(locationB.getLocationsAsArray(), cube.getCube()[46].getLocation().getLocationsAsArray());
		assertArrayEquals(locationC.getLocationsAsArray(), cube.getCube()[47].getLocation().getLocationsAsArray());
	}
	
	@Test
	public void shouldRotateLeft() {
		locationA = new SquareLocation(-1, 1, -1);
		locationB = new SquareLocation(-1, 1, 0);
		locationC = new SquareLocation(-1, 1, 1);
		cube.rotateLeft();
		assertArrayEquals(locationA.getLocationsAsArray(), cube.getCube()[36].getLocation().getLocationsAsArray());
		assertArrayEquals(locationB.getLocationsAsArray(), cube.getCube()[37].getLocation().getLocationsAsArray());
		assertArrayEquals(locationC.getLocationsAsArray(), cube.getCube()[38].getLocation().getLocationsAsArray());
	}
	
	@Test
	public void shouldRotateUp() {
		locationA = new SquareLocation(-1, 1, -1);
		locationB = new SquareLocation(-1, 1, 0);
		locationC = new SquareLocation(-1, 1, 1);
		cube.rotateUp();
		assertArrayEquals(locationA.getLocationsAsArray(), cube.getCube()[9].getLocation().getLocationsAsArray());
		assertArrayEquals(locationB.getLocationsAsArray(), cube.getCube()[12].getLocation().getLocationsAsArray());
		assertArrayEquals(locationC.getLocationsAsArray(), cube.getCube()[15].getLocation().getLocationsAsArray());
	}
	
	@Test
	public void shouldRotateDown() {
		locationA = new SquareLocation(1, -1, 1);
		locationB = new SquareLocation(1, -1, 0);
		locationC = new SquareLocation(1, -1, -1);
		cube.rotateDown();
		assertArrayEquals(locationA.getLocationsAsArray(), cube.getCube()[29].getLocation().getLocationsAsArray());
		assertArrayEquals(locationB.getLocationsAsArray(), cube.getCube()[32].getLocation().getLocationsAsArray());
		assertArrayEquals(locationC.getLocationsAsArray(), cube.getCube()[35].getLocation().getLocationsAsArray());
	}
	
	@Test
	public void shouldRotateFront() {
		locationA = new SquareLocation(-1, 1, 1);
		locationB = new SquareLocation(0, 1, 1);
		locationC = new SquareLocation(1, 1, 1);
		cube.rotateFront();
		assertArrayEquals(locationA.getLocationsAsArray(), cube.getCube()[0].getLocation().getLocationsAsArray());
		assertArrayEquals(locationB.getLocationsAsArray(), cube.getCube()[1].getLocation().getLocationsAsArray());
		assertArrayEquals(locationC.getLocationsAsArray(), cube.getCube()[2].getLocation().getLocationsAsArray());
	}
	
	@Test
	public void shouldRotateBack() {
		locationA = new SquareLocation(1, 1, -1);
		locationB = new SquareLocation(1, 0, -1);
		locationC = new SquareLocation(1, -1, -1);
		cube.rotateBack();
		assertArrayEquals(locationA.getLocationsAsArray(), cube.getCube()[18].getLocation().getLocationsAsArray());
		assertArrayEquals(locationB.getLocationsAsArray(), cube.getCube()[21].getLocation().getLocationsAsArray());
		assertArrayEquals(locationC.getLocationsAsArray(), cube.getCube()[24].getLocation().getLocationsAsArray());
	}
	
	@Ignore
	@Test
	public void shouldUpdateCube() {
		System.out.println(cube.toString());
		cube.rotateRight();
		cube.rotateFront();
		cube.update();
		System.out.println(cube.toString());
	}
	
	@Ignore
	@Test
	public void getColors() {
		for(Square square : cube.rotation.cube) {
			if(square.getLocation().locationX == -1 &&
					square.getLocation().locationY == -1 &&
					square.getLocation().locationZ == 1) {
				System.out.println(square.getColor());
			}
		}
	}

}
