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
	
	// The color of the Square represented as a string.
	private String color;
	
	/* Position of the square is saved in an object that functions as a vector
	 * containing information about the x, y and y-coordinates.
	 */
	
	private SquareLocation location;
	
	public Square(SquareLocation location, String color) {
		this.location = location;
		this.color = color;
	}
	
	public String getColor() {
		return color;
	}
	
	public SquareLocation getLocation() {
		return location;
	}
	
	public void setLocation(SquareLocation newLocation) {
		this.location = newLocation;
	}
	
	public void setColor(String newColor) {
		this.color = newColor;
	}

}
