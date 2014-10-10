package com.github.sgelb.arcs;

public class RubiksCube {
	
	public Rotation rotation;
	public CubeUpdater updater;
	public Square[] cube;
	
	public RubiksCube() {
		this.rotation = new Rotation();
		this.updater = new CubeUpdater();
		this.cube = rotation.getCube().clone();
	}
	
	public void rotateFront() {
		rotation.setUpSquaresForRotation(Rotation.ROTATION_FRONT);
		rotation.rotateFront();
		update();
	}
	
	public void rotateBack() {
		rotation.setUpSquaresForRotation(Rotation.ROTATION_BACK);
		rotation.rotateBack();
		update();
	}
	
	public void rotateUp() {
		rotation.setUpSquaresForRotation(Rotation.ROTATION_UP);
		rotation.rotateUp();
		update();
	}
	
	public void rotateDown() {
		rotation.setUpSquaresForRotation(Rotation.ROTATION_DOWN);
		rotation.rotateDown();
		update();
	}
	
	public void rotateLeft() {
		rotation.setUpSquaresForRotation(Rotation.ROTATION_LEFT);
		rotation.rotateLeft();
		update();
	}
	
	public void rotateRight() {
		rotation.setUpSquaresForRotation(Rotation.ROTATION_RIGHT);
		rotation.rotateRight();
		update();
	}

	public void update() {
		cube = rotation.getCube();
		cube = updater.updateCube(cube);
	}
	
	public Square[] getCube() {
		return cube.clone();
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
