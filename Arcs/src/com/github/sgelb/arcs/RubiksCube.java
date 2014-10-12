package com.github.sgelb.arcs;

public class RubiksCube {
	
	public Rotation rotation;
	public CubeUpdater updater;
	public Square[] cube;
	
	public Square[] face;
	
	public RubiksCube() {
		this.rotation = new Rotation();
		this.updater = new CubeUpdater();
		this.cube = rotation.getCube().clone();
		this.face = new Square[9];
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
	
	/* The getter-methods return an square-array of length 9,
	 * which contains all squares of the desired face.
	 * The squares can be accessed to get or set their color
	 * (square.getColor() or square.setColor(String newColor).
	 */
	
	public Square[] getFrontFace() {
		Square[] face = new Square[9];
		for(int i = 0; i < 9; i++) {
			face[i] = cube[i];
		}
		return face.clone();
	}
	
	public Square[] getBackFace() {
		Square[] face = new Square[9];
		for(int i = 0; i < 9; i++) {
			face[i] = cube[18 + i];
		}
		return face.clone();
	}
	
	public Square[] getUpFace() {
		Square[] face = new Square[9];
		for(int i = 0; i < 9; i++) {
			face[i] = cube[9 + i];
		}
		return face.clone();
	}
	
	public Square[] getDownFace() {
		Square[] face = new Square[9];
		for(int i = 0; i < 9; i++) {
			face[i] = cube[27 + i];
		}
		return face.clone();
	}
	
	public Square[] getLeftFace() {
		Square[] face = new Square[9];
		for(int i = 0; i < 9; i++) {
			face[i] = cube[36 + i];
		}
		return face.clone();
	}
	
	public Square[] getRightFace() {
		Square[] face = new Square[9];
		for(int i = 0; i < 9; i++) {
			face[i] = cube[45 + i];
		}
		return face.clone();
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
