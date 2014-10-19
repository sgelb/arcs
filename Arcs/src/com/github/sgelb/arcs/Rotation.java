package com.github.sgelb.arcs;

import java.util.ArrayList;

public class Rotation {
	
	/* This is an implementation of a cube using vectors
	 * to represent the positions of the 54 squares.
	 * This code is heavily inspired by:
	 * http://www.algosome.com/articles/rubiks-cube-computer-simulation.html
	 * Some parts might equal those on the website, though I tried to
	 * rewrite it for the sake of not simply copy/pasting.
	 * Big thanks to www.algosome.com!
	*/
	
	private static final String TAG = "ARCS::Rotation";
	
	// define static face names
	public final static int FRONT = 0; // squares[0-8]
	public final static int RIGHT = 1; // squares[9-17]
	public final static int BACK = 2;  // squares[18-26]
	public final static int LEFT = 3;  // squares[27-35]
	public final static int DOWN = 4;  // squares[36-44]
	public final static int UP = 5;    // squares[45-53]
	
	
	/* This is the actual squares, containing all squares and their
	 * information about location and color.
	 */
	private Square[] squares;
	
	/* This List contains all squares that are needed for a rotator.
	 */
	private ArrayList<Square> selectedSquares;
	

	public Rotation() {
		this.squares = new Square[54];
		initCube();
	}
	
	/* These three methods are used to rotate a square to a certain position.
	 * They don't really alter the square they simply return a new one with
	 * the altered location.
	 */
	private SquareLocation rotateOnXAxis(SquareLocation loc, double degree) {
		int x = loc.getLocationX();
		int y = (int) Math.round(loc.getLocationY() * Math.cos(degree) - loc.getLocationZ() * Math.sin(degree));
		int z = (int) Math.round(loc.getLocationY() * Math.sin(degree) + loc.getLocationZ() * Math.cos(degree));
		return new SquareLocation(x, y, z);
	}
	
	private SquareLocation rotateOnYAxis(SquareLocation loc, double degree) {
		int x = (int) Math.round(loc.getLocationZ() * Math.sin(degree) + loc.getLocationX() * Math.cos(degree));
		int y = (int) loc.getLocationY();
		int z = (int) Math.round(loc.getLocationZ() * Math.cos(degree) - loc.getLocationX() * Math.sin(degree));
		return new SquareLocation(x, y, z);
	}
	
	private SquareLocation rotateOnZAxis(SquareLocation loc, double degree) {
		int x = (int) Math.round(loc.getLocationX() * Math.cos(degree) - loc.getLocationY() * Math.sin(degree));
		int y = (int) Math.round(loc.getLocationX() * Math.sin(degree) + loc.getLocationY() * Math.cos(degree));
		int z = (int) loc.getLocationZ();
		return new SquareLocation(x, y, z);
	}
	
	
	/* rotateSquare takes a square, an axis (string) and a double value,
	 * that represents the rotator on the given axis. newSquareLocation is
	 * initialized with null because otherwise one would have to add an
	 * else statement at the end where it gets initialized, so there is an
	 * actual SquareLocation to be returned.
	 */
	private void rotateSquare(Square square, String axis, double degree) {
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
		ArrayList<Square> squaresForEachFace;
		
		for(int face = FRONT; face <= UP; face++) {
			squaresForEachFace = new ArrayList<Square>();
			
			// This will be the front face.
			for (int yAxis = 1; yAxis > -2; yAxis--) {
				for (int xAxis = -1; xAxis < 2; xAxis++) {
					squaresForEachFace.add(new Square(new SquareLocation(xAxis, yAxis, 1),
							new SquareLocation(0, 0, 1), SquareColor.UNSET_COLOR));
				}
			}
			
			// Here the squares get rotated to their positions.

			// Front
			if (face == FRONT) {
				// we do not need to rotate for front face
			// Right
			} else if (face == RIGHT) {
				for(Square square : squaresForEachFace) {
				rotateSquare(square, "y", Math.PI / 2);
				}
			// Back
			} else if (face == BACK) {
				for(Square square : squaresForEachFace) {
					rotateSquare(square, "x", Math.PI);
				}
			// Left
			} else if (face == LEFT) {
				for(Square square : squaresForEachFace) {
					rotateSquare(square, "y", -Math.PI / 2);
				}
			// Down
			} else if (face == DOWN) {
				for(Square square : squaresForEachFace) {
					rotateSquare(square, "x", Math.PI / 2);
				}
			// Up
			} else if (face == UP) {
				for(Square square : squaresForEachFace) {
					rotateSquare(square, "x", -Math.PI / 2);
				}
			}
			
			// Here the squares that just got initialized are assigned onto the squares.
			for(int squareOnFaceIndex = 0; squareOnFaceIndex < 9; squareOnFaceIndex++) {
				squares[face * 9 + squareOnFaceIndex] = squaresForEachFace.get(squareOnFaceIndex);
			}
			
		}
	}

	/* With this method, all the squares that are needed for a certain
	 * rotator are selected.
	 * Example: for a rotatoion of the left side you will need to select
	 * all squares where the x-value of their locations is -1.
	 * These squares are added to the squares ArrayList and can be used
	 * in the following actual rotator.
	 * 
	 * Again, if one wants to call this method, use the constants as parameters.
	 * setUpSquaresForRotation(FRONT) is much more readable than
	 * setUpSquaresForRotation(3).
	 */
	private void setUpSquaresForRotation(int facename) {
		selectedSquares = new ArrayList<Square>();
		for(Square square : getSquares()) {
			
			if(facename == FRONT) {
				if(square.getLocationZ() == 1) {
					selectedSquares.add(square);
				}
			} else if(facename == RIGHT) {
				if(square.getLocationX() == 1) {
					selectedSquares.add(square);
				}
			} else if(facename == BACK) {
				if(square.getLocationZ() == -1) {
					selectedSquares.add(square);
				}
			} else if(facename == LEFT) {
				if(square.getLocationX() == -1) {
					selectedSquares.add(square);
				}
			} else if(facename == DOWN) {
				if(square.getLocationY() == -1) {
					selectedSquares.add(square);
				}
			} else if(facename == UP) {
				if(square.getLocationY() == 1) {
					selectedSquares.add(square);
				}
			}
		}
	}
	
	
	/* The rotator methods takes the prepared squares (which had to be set
	 * beforehand using the setUpSquaresForRotation-method) and iterates
	 * over them, rotating each square to their new location.
	 */
	public void rotateFront() {
		setUpSquaresForRotation(FRONT);
		for (Square square : squares) {
			if(selectedSquares.contains(square)) {
				rotateSquare(square, "z", -Math.PI / 2);
			}
		}
	}
	
	public void rotateRight() {
		setUpSquaresForRotation(RIGHT);
		for (Square square : squares) {
			if(selectedSquares.contains(square)) {
				rotateSquare(square, "x", -Math.PI / 2);
			}
		}
	}
	
	public void rotateBack() {
		setUpSquaresForRotation(BACK);
		for (Square square : squares) {
			if(selectedSquares.contains(square)) {
				rotateSquare(square, "z", Math.PI / 2);
			}
		}
	}
	
	public void rotateLeft() {
		setUpSquaresForRotation(LEFT);
		for (Square square : squares) {
			if(selectedSquares.contains(square)) {
				rotateSquare(square, "x", Math.PI / 2);
			}
		}
	}
	
	public void rotateDown() {
		setUpSquaresForRotation(DOWN);
		for (Square square : squares) {
			if(selectedSquares.contains(square)) {
				rotateSquare(square, "y", Math.PI / 2);
			}
		}
	}
	
	public void rotateUp() {
		setUpSquaresForRotation(UP);
		for (Square square : squares) {
			if(selectedSquares.contains(square)) {
				rotateSquare(square, "y", -Math.PI / 2);
			}
		}
	}
	
	// Returns all squares.
	public Square[] getSquares() {
		return this.squares;
	}
	
	
	// return one face after the other
	public static int nextFace(int currentFace) {
		if (currentFace == UP) {
			return -1;
		}
		// we have to increment before returning, do not change to currentFace++
		return (currentFace+1);
	}
}