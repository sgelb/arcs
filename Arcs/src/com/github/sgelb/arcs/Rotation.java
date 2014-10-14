package com.github.sgelb.arcs;

import java.util.ArrayList;

public class Rotation {
	
	/* This is an implementation on a cube using vectors
	 * to represent the positions of the squares.
	 * This code is heavily inspired by:
	 * http://www.algosome.com/articles/rubiks-cube-computer-simulation.html
	 * Some parts might equal those on the website, though I tried to
	 * rewrite it for the sake of not simply copy/pasting.
	 * Big thanks to www.algosome.com!
	*/
	
	
	/* These constants are used to determine which squares should be
	 * selected for the rotation. Needed in setUpSquaresForRotation.
	 * Should be given as constants so you know which side you want
	 * to rotate, don't just call setUpSquaresForRotation(my_integer).
	 */
	public final static int ROTATION_UP = 1;
	public final static int ROTATION_DOWN = 2;
	public final static int ROTATION_FRONT = 3;
	public final static int ROTATION_BACK = 4;
	public final static int ROTATION_RIGHT = 5;
	public final static int ROTATION_LEFT = 6;
	
	
	/* This is the actual cube, containing all squares and their
	 * information about location and color.
	 */
	public Square[] cube;
	
	// TODO: new squares have to be assigned to cube.
	
	/* This List contains all squares that are needed for a rotation.
	 * 
	 */
	public ArrayList<Square> selectedSquares;
	
	/* This is a temporary List only used in the initCube-method.
	 * It stores the 9 squares for each face before and after
	 * they get shifted. After that, they are assigned to cube
	 * and squaresForEachFace is initialized anew.
	 */
	public ArrayList<Square> squaresForEachFace;
	
	// Contains the colors for the squares during the initialization.
	public String[] colors;
	
	public Rotation() {
		this.cube = new Square[54];
		
		colors = new String[6];
		colors[0] = "yellow";
		colors[1] = "orange";
		colors[2] = "green";
		colors[3] = "white";
		colors[4] = "blue";
		colors[5] = "red";
		initCube();
	}
	
	/* These three methods are used to rotate a square to a certain position.
	 * They don't really alter the square they simply return a new one with
	 * the altered location.
	 */
	public SquareLocation rotateOnXAxis(SquareLocation loc, double degree) {
		int x = (int) loc.locationX;
		int y = (int) Math.round(loc.locationY * Math.cos(degree) - loc.locationZ * Math.sin(degree));
		int z = (int) Math.round(loc.locationY * Math.sin(degree) + loc.locationZ * Math.cos(degree));
		return new SquareLocation(x, y, z);
	}
	
	public SquareLocation rotateOnYAxis(SquareLocation loc, double degree) {
		int x = (int) Math.round(loc.locationZ * Math.sin(degree) + loc.locationX * Math.cos(degree));
		int y = (int) loc.locationY;
		int z = (int) Math.round(loc.locationZ * Math.cos(degree) - loc.locationX * Math.sin(degree));
		return new SquareLocation(x, y, z);
	}
	
	public SquareLocation rotateOnZAxis(SquareLocation loc, double degree) {
		int x = (int) Math.round(loc.locationX * Math.cos(degree) - loc.locationY * Math.sin(degree));
		int y = (int) Math.round(loc.locationX * Math.sin(degree) + loc.locationY * Math.cos(degree));
		int z = (int) loc.locationZ;
		return new SquareLocation(x, y, z);
	}
	
	
	/* rotateSquare takes a square, an axis (string) and a double value,
	 * that represents the rotation on the given axis. newSquareLocation is
	 * initialized with null because otherwise one would have to add an
	 * else statement at the end where it gets initialized, so there is an
	 * actual SquareLocation to be returned.
	 */
	public void rotateSquare(Square square, String axis, double degree) {
		SquareLocation newSquareLocation = null;
		SquareLocation newSquareDirection = null;
		
		if(axis.equalsIgnoreCase("x")) {
			newSquareLocation = rotateOnXAxis(square.getLocation(), degree);
			newSquareDirection = rotateOnXAxis(square.getDirection(), degree);
		} else if(axis.equalsIgnoreCase("y")) {
			newSquareLocation = rotateOnYAxis(square.getLocation(), degree);
			newSquareDirection = rotateOnYAxis(square.getDirection(), degree);
		} else if(axis.equalsIgnoreCase("z")) {
			newSquareLocation = rotateOnZAxis(square.getLocation(), degree);
			newSquareDirection = rotateOnZAxis(square.getDirection(), degree);
		}
		
		square.setLocation(newSquareLocation);
		square.setDirection(newSquareDirection);
	}
	
	/* We use this coordinate system. There is no reason for it. Period.
	 * 
	 * Coordinates:
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
	
	
	/* initCube works as followed: it creates 9 squares at the
	 * positions of the front face. After that, the 9 squares are moved to
	 * the positions of one of the other faces. Repeat until all faces are
	 * set.
	 */
	public void initCube() {
		
		for(int i = 0; i < 6; i++) {
			squaresForEachFace = new ArrayList<Square>();
			
			// This will be the front face.
			for(int j = -1; j < 2; j++) {
				for(int k = -1; k < 2; k++) {
					squaresForEachFace.add(new Square(new SquareLocation(j, k, 1), new SquareLocation(0, 0, 1), colors[i]));
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
			
			// Here the squares that just got initialized are assigned onto the cube.
			for(int j = 0; j < 9; j++) {
				cube[i * 9 + j] = squaresForEachFace.get(j);
			}
			
		}
	}
	

	/* With this method, all the squares that are needed for a certain
	 * rotation are selected.
	 * Example: for a rotation of the left side you will need to select
	 * all squares where the x-value of their locations is -1.
	 * These squares are added to the squares ArrayList and can be used
	 * in the following actual rotation.
	 * 
	 * Again, if one wants to call this method, use the constants as parameters.
	 * setUpSquaresForRotation(ROTATION_FRONT) is much more readable than
	 * setUpSquaresForRotation(3).
	 */
	public void setUpSquaresForRotation(int rotation) {
		selectedSquares = new ArrayList<Square>();
		for(Square square : getCube()) {
			
			if(rotation == ROTATION_FRONT) {
				if(square.getLocation().locationZ == 1) {
					selectedSquares.add(square);
				}
			} else if(rotation == ROTATION_BACK) {
				if(square.getLocation().locationZ == -1) {
					selectedSquares.add(square);
				}
			} else if(rotation == ROTATION_UP) {
				if(square.getLocation().locationY == 1) {
					selectedSquares.add(square);
				}
			} else if(rotation == ROTATION_DOWN) {
				if(square.getLocation().locationY == -1) {
					selectedSquares.add(square);
				}
			} else if(rotation == ROTATION_LEFT) {
				if(square.getLocation().locationX == -1) {
					selectedSquares.add(square);
				}
			} else if(rotation == ROTATION_RIGHT) {
				if(square.getLocation().locationX == 1) {
					selectedSquares.add(square);
				}
			}
		}
	}
	
	
	/* The rotation methods takes the prepared squares (which had to be set
	 * beforehand using the setUpSquaresForRotation-method) and iterates
	 * over them, rotating each square to their new location.
	 */
	public void rotateLeft() {
		setUpSquaresForRotation(Rotation.ROTATION_LEFT);
		for(Square square : cube) {
			if(selectedSquares.contains(square)) {
				rotateSquare(square, "x", Math.PI / 2);
			}
		}
	}
	
	public void rotateRight() {
		setUpSquaresForRotation(Rotation.ROTATION_RIGHT);
		for(Square square : cube) {
			if(selectedSquares.contains(square)) {
				rotateSquare(square, "x", -Math.PI / 2);
			}
		}
	}
	
	public void rotateFront() {
		setUpSquaresForRotation(Rotation.ROTATION_FRONT);
		for(Square square : cube) {
			if(selectedSquares.contains(square)) {
				rotateSquare(square, "z", -Math.PI / 2);
			}
		}
	}
	
	public void rotateBack() {
		setUpSquaresForRotation(Rotation.ROTATION_BACK);
		for(Square square : cube) {
			if(selectedSquares.contains(square)) {
				rotateSquare(square, "z", Math.PI / 2);
			}
		}
	}
	
	public void rotateUp() {
		setUpSquaresForRotation(Rotation.ROTATION_UP);
		for(Square square : cube) {
			if(selectedSquares.contains(square)) {
				rotateSquare(square, "y", -Math.PI / 2);
			}
		}
	}
	
	public void rotateDown() {
		setUpSquaresForRotation(Rotation.ROTATION_DOWN);
		for(Square square : cube) {
			if(selectedSquares.contains(square)) {
				rotateSquare(square, "y", Math.PI / 2);
			}
		}
	}
	
	// Returns all squares.
	public Square[] getCube() {
		return cube;
	}
	
}