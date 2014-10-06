package com.github.sgelb.arcs;

import java.util.ArrayList;

public class Rotation {
	
	/* This is an implementation on a cube using vectors
	 * to represent the positions of the squares.
	*/
	
	public final static int ROTATION_UP = 1;
	public final static int ROTATION_DOWN = 2;
	public final static int ROTATION_FRONT = 3;
	public final static int ROTATION_BACK = 4;
	public final static int ROTATION_RIGHT = 5;
	public final static int ROTATION_LEFT = 6;
	
	// A cube defines itself by 54 squares
	// das klingt doof ichkannkeinenglisch.jpg
	public Square[] cube = new Square[54];
	
	// TODO: new squares have to be assigned to cube.
	
	public ArrayList<Square> squares;
	public ArrayList<Square> squaresForEachFace;
	
	public String[] colors = new String[6];
	
	public Rotation() {
		colors[0] = "yellow";
		colors[1] = "orange";
		colors[2] = "green";
		colors[3] = "white";
		colors[4] = "blue";
		colors[5] = "red";
		initCube();
	}
	
	public SquareLocation rotateOnXAxis(SquareLocation loc, double degree) {
		int x = (int) loc.locationX;
		int y = (int) Math.round(loc.locationY * Math.cos(degree) - loc.locationZ * Math.sin(degree));
		int z = (int) Math.round(loc.locationY * Math.sin(degree) - loc.locationZ * Math.cos(degree));
		return new SquareLocation(x, y, z);
	}
	
	public SquareLocation rotateOnYAxis(SquareLocation loc, double degree) {
		int x = (int) Math.round(loc.locationZ * Math.sin(degree) - loc.locationX * Math.cos(degree));
		int y = (int) loc.locationY;
		int z = (int) Math.round(loc.locationZ * Math.cos(degree) - loc.locationX * Math.sin(degree));
		return new SquareLocation(x, y, z);
	}
	
	public SquareLocation rotateOnZAxis(SquareLocation loc, double degree) {
		int x = (int) Math.round(loc.locationX * Math.cos(degree) - loc.locationY * Math.sin(degree));
		int y = (int) Math.round(loc.locationX * Math.sin(degree) - loc.locationY * Math.cos(degree));
		int z = (int) loc.locationZ;
		return new SquareLocation(x, y, z);
	}
	
	public void rotateSquare(Square square, String axis, double degree) {
		SquareLocation newSquareLocation;
		
		if(axis.equalsIgnoreCase("x")) {
			newSquareLocation = rotateOnXAxis(square.getLocation(), degree);
		} else if(axis.equalsIgnoreCase("y")) {
			newSquareLocation = rotateOnYAxis(square.getLocation(), degree);
		} else if(axis.equalsIgnoreCase("z")) {
			newSquareLocation = rotateOnZAxis(square.getLocation(), degree);
		} else {
			// Default case. Should never get in here.
			newSquareLocation = null;
		}
		
		square.setLocation(newSquareLocation);
	}
	
	/* Coordinates:
	 * 			y
	 * 			^
	 * 			|
	 * 			|
	 * 			|
	 * 			|
	 * 			|_____________> x
	 * 			/
	 * 		   /
	 * 		  /
	 * 		 /
	 * 		z
	 */
	
	public void initCube() {
		
		// Initialize the cube.
		for(int i = 0; i < 6; i++) {
			squaresForEachFace = new ArrayList<Square>();
			
			// This will be the front face.
			for(int j = -1; j < 2; j++) {
				for(int k = -1; k < 2; k++) {
					squaresForEachFace.add(new Square(new SquareLocation(j, k, 1), colors[i]));
				}
			}
			
			// Here the squares get rotated to their positions.
			// Bottom
			if(i == 1) {
				for(Square square : squaresForEachFace) {
					rotateSquare(square, "x", Math.PI / 2);
				}
			// Back
			} else if(i == 2) {
				for(Square square : squaresForEachFace) {
					rotateSquare(square, "x", Math.PI);
				}
			// Up
			} else if(i == 3) {
				for(Square square : squaresForEachFace) {
					rotateSquare(square, "x", -Math.PI / 2);
				}
			// Left
			} else if(i == 4) {
				for(Square square : squaresForEachFace) {
					rotateSquare(square, "y", -Math.PI / 2);
				}
			// Right	
			} else if(i == 5) {
				for(Square square : squaresForEachFace) {
					rotateSquare(square, "y", Math.PI / 2);
				}
			}
			
			// Here the squaresForEachFace are assigned onto the cube.
			for(int j = 0; j < 9; j++) {
				cube[i * 9 + j] = squaresForEachFace.get(j);
			}
		}
	}
	
	public Square[] getAllSquares() {
		return cube;
	}
	
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
	
	////////////////////////////////////////////////////////////////////////////
	
	
	public void setUpSquaresForRotation(int rotation) {
		squares = new ArrayList<Square>();
		for(Square square : getAllSquares()) {
			
			if(rotation == ROTATION_FRONT) {
				if(square.getLocation().locationZ == 1) {
					squares.add(square);
				}
			} else if(rotation == ROTATION_BACK) {
				if(square.getLocation().locationZ == -1) {
					squares.add(square);
				}
			} else if(rotation == ROTATION_UP) {
				if(square.getLocation().locationY == 1) {
					squares.add(square);
				}
			} else if(rotation == ROTATION_DOWN) {
				if(square.getLocation().locationY == -1) {
					squares.add(square);
				}
			} else if(rotation == ROTATION_LEFT) {
				if(square.getLocation().locationX == -1) {
					squares.add(square);
				}
			} else if(rotation == ROTATION_RIGHT) {
				if(square.getLocation().locationX == 1) {
					squares.add(square);
				}
			}
		}
	}
	
	public void rotateLeft() {
		for(Square square : squares) {
			rotateSquare(square, "x", Math.PI / 2);
		}
	}
	
	public void rotateRight() {
		for(Square square : squares) {
			rotateSquare(square, "x", -Math.PI / 2);
		}
	}
	
	public void rotateFront() {
		for(Square square : squares) {
			rotateSquare(square, "z", -Math.PI / 2);
		}
	}
	
	public void rotateBack() {
		for(Square square : squares) {
			rotateSquare(square, "z", Math.PI / 2);
		}
	}
	
	public void rotateUp() {
		for(Square square : squares) {
			rotateSquare(square, "y", -Math.PI / 2);
		}
	}
	
	public void rotateDown() {
		for(Square square : squares) {
			rotateSquare(square, "y", Math.PI / 2);
		}
	}
	
}