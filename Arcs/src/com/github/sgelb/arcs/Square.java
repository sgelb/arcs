package com.github.sgelb.arcs;

public class Square {
	
	/* A square represents one rectangle on the Cube.
	 */
	
	public static final String BLUE = "blue";
	public static final String GREEN = "green";
	public static final String ORANGE = "orange";
	public static final String RED = "red";
	public static final String WHITE = "white";
	public static final String YELLOW = "yellow";
	public static final String UNSET_COLOR = "unsetColor";
	
	// The color of the Square represented as a string.
	private String color;
	
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
	
	public Square(SquareLocation location, SquareLocation direction, String color) {
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
	
	public String getColor() {
		return color;
	}
	
	
	public void setLocation(SquareLocation newLocation) {
		this.location = newLocation;
	}
	
	public void setDirection(SquareLocation newDirection) {
		this.direction = newDirection;
	}
	
	public void setColor(String newColor) {
		this.color = newColor;
	}

}
