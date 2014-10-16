package com.github.sgelb.arcs;

public class RubiksCube {
	
	private Rotation rotator;
	private CubeUpdater updater;
	private Square[] cube;
	
	public RubiksCube() {
		this.rotator = new Rotation();
		this.updater = new CubeUpdater();
		this.cube = rotator.getCube();
	}
	
	public void rotateFront() {
		rotator.rotateFront();
		update();
	}
	
	public void rotateBack() {
		rotator.rotateBack();
		update();
	}
	
	public void rotateUp() {
		rotator.rotateUp();
		update();
	}
	
	public void rotateDown() {
		rotator.rotateDown();
		update();
	}
	
	public void rotateLeft() {
		rotator.rotateLeft();
		update();
	}
	
	public void rotateRight() {
		rotator.rotateRight();
		update();
	}

	public void update() {
		cube = rotator.getCube();
		cube = updater.updateSquares(cube);
	}
	
	public Square[] getCube() {
		return cube;
	}
	
	/* Returns all squares of the desired face.
	 * facename is defined as an public final static variable in Rotation.java:
	 * Rotation.FRONT, Rotation.RIGHT, ...
	 */
	public Square[] getFace(int facename) {
		Square[] face = new Square[9];
		for(int i = 0; i < 9; i++) {
			face[i] = cube[9*facename + i];
		}
		return face;
	}
	
	// toString-methods for testing purposes.
	
	@Override
	public String toString() {
		String string = "";
		for(int i = 0; i < cube.length; i += 3) {
			string += cube[i].getColor() + " " + cube[i + 1].getColor() + " " + cube[i + 2].getColor() + "\n";
		}
		return string;
	}
	
	public String getPositions() {
		String string = "";
		for(int i = 0; i < 9; i++) {
			string += "Color: " + cube[i].getColor() + ", " + cube[i].getLocation().toString();
		}
		return string;
	}
}
