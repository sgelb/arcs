package com.github.sgelb.arcs;

public class SquareLocation {
	
	// might have to be double values.
	public double locationX;
	public double locationY;
	public double locationZ;
	
	public SquareLocation(double x, double y, double z) {
		this.locationX = x;
		this.locationY = y;
		this.locationZ = z;
	}
	
	public void setLocation(double x, double y, double z) {
		this.locationX = x;
		this.locationY = y;
		this.locationZ = z;
	}
	
	// For test purposes.
	public int[] getLocationsAsArray() {
		return new int[]{(int) locationX, (int) locationY, (int) locationZ};
	}
	
	@Override
	public String toString() {
		String string = "X: " + locationX + ", Y: " + locationY + ", Z: " + locationZ + "\n";
		return string;
	}

}
