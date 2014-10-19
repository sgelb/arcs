package com.github.sgelb.arcs;


public class SquareColor {
	
	public static final int UNSET_COLOR = -1;
	public static final int ORANGE = 0;
	public static final int BLUE = 1;
	public static final int RED = 2;
	public static final int GREEN = 3;
	public static final int WHITE = 4;
	public static final int YELLOW = 5;
	
	public static String getString(int color) {
		String colorString = "unset";
		
		switch (color) {
		case ORANGE:
			colorString = "orange";
			break;
		case BLUE:
			colorString = "blue";
			break;
		case RED:
			colorString = "red";
			break;
		case GREEN:
			colorString = "green";
			break;
		case WHITE:
			colorString = "white";
			break;
		case YELLOW:
			colorString = "yellow";
			break;
		default:
			colorString = "unset";
			break;
		}
		return colorString;
	}

}
