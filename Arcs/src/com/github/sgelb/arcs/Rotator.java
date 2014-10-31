package com.github.sgelb.arcs;

import java.util.ArrayList;

public class Rotator {
	
	/* This is an implementation of a cube using vectors
	 * to represent the positions of the 54 facelets.
	 * This code is heavily inspired by:
	 * http://www.algosome.com/articles/rubiks-cube-computer-simulation.html
	 * Some parts might equal those on the website, though I tried to
	 * rewrite it for the sake of not simply copy/pasting.
	 * Big thanks to www.algosome.com!
	*/
	
	@SuppressWarnings("unused")
	private static final String TAG = "ARCS::Rotator";
	
	// define static face names
	public final static int FRONT = 0; // facelets[0-8]
	public final static int RIGHT = 1; // facelets[9-17]
	public final static int BACK = 2;  // facelets[18-26]
	public final static int LEFT = 3;  // facelets[27-35]
	public final static int DOWN = 4;  // facelets[36-44]
	public final static int UP = 5;    // facelets[45-53]
	
	
	/* This is the actual facelets, containing all facelets and their
	 * information about location and color.
	 */
	private Facelet[] facelets;
	
	/* This List contains all facelets that are needed for a rotator.
	 */
	private ArrayList<Facelet> selectedSquares;
	

	public Rotator() {
		this.facelets = new Facelet[54];
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
	private void rotateSquare(Facelet facelet, String axis, double degree) {
		SquareLocation newSquareLocation = null;
		SquareLocation newSquareDirection = null;
		
		if(axis.equalsIgnoreCase("x")) {
			newSquareLocation = rotateOnXAxis(facelet.getLocation(), degree);
			newSquareDirection = rotateOnXAxis(facelet.getDirection(), degree);
		} else if(axis.equalsIgnoreCase("y")) {
			newSquareLocation = rotateOnYAxis(facelet.getLocation(), degree);
			newSquareDirection = rotateOnYAxis(facelet.getDirection(), degree);
		} else if(axis.equalsIgnoreCase("z")) {
			newSquareLocation = rotateOnZAxis(facelet.getLocation(), degree);
			newSquareDirection = rotateOnZAxis(facelet.getDirection(), degree);
		}
		
		facelet.setLocation(newSquareLocation);
		facelet.setDirection(newSquareDirection);
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
	
	
	/* initCube works as followed: it creates 9 facelets at the
	 * positions of the front face. After that, the 9 facelets are moved to
	 * the positions of one of the other faces. Repeat until all faces are
	 * set.
	 */
	public void initCube() {
		ArrayList<Facelet> squaresForEachFace;
		
		for(int face = FRONT; face <= UP; face++) {
			squaresForEachFace = new ArrayList<Facelet>();
			
			// This will be the front face.
			for (int yAxis = 1; yAxis > -2; yAxis--) {
				for (int xAxis = -1; xAxis < 2; xAxis++) {
					squaresForEachFace.add(new Facelet(new SquareLocation(xAxis, yAxis, 1),
							new SquareLocation(0, 0, 1), ColorConverter.UNSET_COLOR));
				}
			}
			
			// Here the facelets get rotated to their positions.

			// Front
			if (face == FRONT) {
				// we do not need to rotate for front face
			// Right
			} else if (face == RIGHT) {
				for(Facelet facelet : squaresForEachFace) {
				rotateSquare(facelet, "y", Math.PI / 2);
				}
			// Back
			} else if (face == BACK) {
				for(Facelet facelet : squaresForEachFace) {
					rotateSquare(facelet, "x", Math.PI);
				}
			// Left
			} else if (face == LEFT) {
				for(Facelet facelet : squaresForEachFace) {
					rotateSquare(facelet, "y", -Math.PI / 2);
				}
			// Down
			} else if (face == DOWN) {
				for(Facelet facelet : squaresForEachFace) {
					rotateSquare(facelet, "x", Math.PI / 2);
				}
			// Up
			} else if (face == UP) {
				for(Facelet facelet : squaresForEachFace) {
					rotateSquare(facelet, "x", -Math.PI / 2);
				}
			}
			
			// Here the facelets that just got initialized are assigned onto the facelets.
			for(int squareOnFaceIndex = 0; squareOnFaceIndex < 9; squareOnFaceIndex++) {
				facelets[face * 9 + squareOnFaceIndex] = squaresForEachFace.get(squareOnFaceIndex);
			}
			
		}
	}

	/* With this method, all the facelets that are needed for a certain
	 * rotator are selected.
	 * Example: for a rotatoion of the left side you will need to select
	 * all facelets where the x-value of their locations is -1.
	 * These facelets are added to the facelets ArrayList and can be used
	 * in the following actual rotator.
	 * 
	 * Again, if one wants to call this method, use the constants as parameters.
	 * setUpSquaresForRotation(FRONT) is much more readable than
	 * setUpSquaresForRotation(3).
	 */
	private void setUpSquaresForRotation(int facename) {
		selectedSquares = new ArrayList<Facelet>();
		for(Facelet facelet : getSquares()) {
			
			if(facename == FRONT) {
				if(facelet.getLocationZ() == 1) {
					selectedSquares.add(facelet);
				}
			} else if(facename == RIGHT) {
				if(facelet.getLocationX() == 1) {
					selectedSquares.add(facelet);
				}
			} else if(facename == BACK) {
				if(facelet.getLocationZ() == -1) {
					selectedSquares.add(facelet);
				}
			} else if(facename == LEFT) {
				if(facelet.getLocationX() == -1) {
					selectedSquares.add(facelet);
				}
			} else if(facename == DOWN) {
				if(facelet.getLocationY() == -1) {
					selectedSquares.add(facelet);
				}
			} else if(facename == UP) {
				if(facelet.getLocationY() == 1) {
					selectedSquares.add(facelet);
				}
			}
		}
	}
	
	
	/* The rotator methods takes the prepared facelets (which had to be set
	 * beforehand using the setUpSquaresForRotation-method) and iterates
	 * over them, rotating each square to their new location.
	 */
	public void rotateFront() {
		setUpSquaresForRotation(FRONT);
		for (Facelet facelet : facelets) {
			if(selectedSquares.contains(facelet)) {
				rotateSquare(facelet, "z", -Math.PI / 2);
			}
		}
	}
	
	public void rotateRight() {
		setUpSquaresForRotation(RIGHT);
		for (Facelet facelet : facelets) {
			if(selectedSquares.contains(facelet)) {
				rotateSquare(facelet, "x", -Math.PI / 2);
			}
		}
	}
	
	public void rotateBack() {
		setUpSquaresForRotation(BACK);
		for (Facelet facelet : facelets) {
			if(selectedSquares.contains(facelet)) {
				rotateSquare(facelet, "z", Math.PI / 2);
			}
		}
	}
	
	public void rotateLeft() {
		setUpSquaresForRotation(LEFT);
		for (Facelet facelet : facelets) {
			if(selectedSquares.contains(facelet)) {
				rotateSquare(facelet, "x", Math.PI / 2);
			}
		}
	}
	
	public void rotateDown() {
		setUpSquaresForRotation(DOWN);
		for (Facelet facelet : facelets) {
			if(selectedSquares.contains(facelet)) {
				rotateSquare(facelet, "y", Math.PI / 2);
			}
		}
	}
	
	public void rotateUp() {
		setUpSquaresForRotation(UP);
		for (Facelet facelet : facelets) {
			if(selectedSquares.contains(facelet)) {
				rotateSquare(facelet, "y", -Math.PI / 2);
			}
		}
	}
	
	// Returns all facelets.
	public Facelet[] getSquares() {
		return this.facelets;
	}
	
}