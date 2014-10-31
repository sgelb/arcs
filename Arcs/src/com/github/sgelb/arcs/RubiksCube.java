package com.github.sgelb.arcs;

import java.util.ArrayList;

import android.util.Log;

public class RubiksCube {
	
	private static final String TAG = "ARCS::RubiksCube";

	private Rotation rotator;
	private CubeUpdater updater;
	private Square[] squares;
	
	public RubiksCube() {
		this.rotator = new Rotation();
		this.updater = new CubeUpdater();
		this.squares = rotator.getSquares();
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
		squares = rotator.getSquares();
		squares = updater.updateSquares(squares);
	}
	
	public Square[] getSquares() {
		return squares;
	}
	
	public void setSquares(Square[] squares) {
		this.squares = squares;
	}
	
	public Square[] getFace(int facename) {
		Square[] face = new Square[9];
		for (int i=0; i<9; i++) {
			face[i] = squares[9*facename + i];
		}
		return face;
	}
	
	public ArrayList<Integer> getFaceColor(int facename) {
		ArrayList<Integer> face = new ArrayList<>(9);
		try {
			for (int i=0; i<9; i++) {
				face.add(squares[9*facename + i].getColor());
			}
		} catch (IndexOutOfBoundsException e) {
			face = null;
		}
		return face;
	}

	public int[] getFaceletColors() {
		int[] colors = new int[54];
		for (int i=0; i<squares.length; i++) {
			colors[i] = squares[i].getColor();
		}
		return colors;
	}

	public void setFaceletColors(int[] colors) {
		for (int i=0; i<colors.length; i++) {
			squares[i].setColor(colors[i]);
		}
	}
	
	public void setFaceColor(int facename, ArrayList<Integer> face) {
		for(int i = 0; i < 9; i++) {
			squares[9*facename + i].setColor(face.get(i));
		}
	}
	
	public boolean hasUnsetSquares() {
		for (Square square : squares) {
			if (square.getColor() == SquareColor.UNSET_COLOR) {
				return true;
			}
		}
		return false;
	}
	
	// toString-methods for testing purposes.
	
	@Override
	public String toString() {
		String string = "";
		for(int i = 0; i < squares.length; i += 3) {
			string += squares[i].getColor() + " " + squares[i + 1].getColor() + " " + squares[i + 2].getColor() + "\n";
		}
		return string;
	}
	
	public String getPositions() {
		String string = "";
		for(int i = 0; i < 9; i++) {
			string += "Color: " + squares[i].getColor() + ", " + squares[i].getLocation().toString();
		}
		return string;
	}

	public String getSingmasterNotation() {
		StringBuilder singmaster = new StringBuilder();
		
		// URFDLB
		for (Square square: getFace(Rotation.UP)) {
			singmaster.append(SquareColor.getSingmasterString(square.getColor()));
		}
		for (Square square: getFace(Rotation.RIGHT)) {
			singmaster.append(SquareColor.getSingmasterString(square.getColor()));
		}
		for (Square square: getFace(Rotation.FRONT)) {
			singmaster.append(SquareColor.getSingmasterString(square.getColor()));
		}
		for (Square square: getFace(Rotation.DOWN)) {
			singmaster.append(SquareColor.getSingmasterString(square.getColor()));
		}
		for (Square square: getFace(Rotation.LEFT)) {
			singmaster.append(SquareColor.getSingmasterString(square.getColor()));
		}
		for (Square square: getFace(Rotation.BACK)) {
			singmaster.append(SquareColor.getSingmasterString(square.getColor()));
		}
		Log.d(TAG, "CUBE: " + singmaster);
		return singmaster.toString();
	}
}
