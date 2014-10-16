package com.github.sgelb.arcs;

public class Square {
	
	/* A square represents one rectangle on the Cube.
	 */
	
	public static final int BLUE = 1;
	public static final int GREEN = 2;
	public static final int ORANGE = 3;
	public static final int RED = 4;
	public static final int WHITE = 5;
	public static final int YELLOW = 6;
	public static final int UNSET_COLOR = 7;
	
	// The square color represented
	private int color;
	
	/* Position of the square is saved in an object that functions as a vector
	 * containing information about the x, y and y-coordinates.
	 */
	
	private SquareLocation location;
	
	/* direction holds the information on which face it is located.
	 * Only one of the x, y or z coordinates has a value that is non-zero.
	 * If, for example, the z value is 1, the square is located at the front
	 * face.
	 */
	private SquareLocation direction;
	
	public Square(SquareLocation location, SquareLocation direction, int color) {
		this.location = location;
		this.direction = direction;
		this.color = color;
	}
	
	// getter and setter-methods
	public int getLocationX() {
		return location.getLocationX();
	}
	
	public int getLocationY() {
		return location.getLocationY();
	}
	
	public int getLocationZ() {
		return location.getLocationZ();
	}
	
	public SquareLocation getLocation() {
		return location;
	}
	
	public int[] getLocationsAsArray() {
		return location.getLocation();
	}
	
	public int getDirectionX() {
		return direction.getLocationX();
	}
	
	public int getDirectionY() {
		return direction.getLocationY();
	}
	
	public int getDirectionZ() {
		return direction.getLocationZ();
	}
	
	public int[] getDirectionsAsArray() {
		return direction.getLocation();
	}
	
	public SquareLocation getDirection() {
		return direction;
	}
	
	public int getColor() {
		return color;
	}
	
	
	public void setLocation(SquareLocation newLocation) {
		this.location = newLocation;
	}
	
	public void setDirection(SquareLocation newDirection) {
		this.direction = newDirection;
	}
	
	public void setColor(int color) {
		this.color = color;
	}

}
