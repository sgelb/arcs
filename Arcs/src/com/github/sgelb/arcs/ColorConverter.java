package com.github.sgelb.arcs;


public class ColorConverter {
	
	public static final int UNSET_COLOR = -1;
	public static final int ORANGE = 0;
	public static final int BLUE = 1;
	public static final int RED = 2;
	public static final int GREEN = 3;
	public static final int WHITE = 4;
	public static final int YELLOW = 5;
	
	private static String[] colorNames = {"ORANGE", "BLUE", "RED", "GREEN", "WHITE", "YELLOW"};
	private static String[] singmasterNames = {"F", "R", "B", "L", "D", "U"};


	public static String getColorName(int color) {
		if (color < 0 || color > colorNames.length - 1) {
			return "UNSET";
		}
		return colorNames[color];
	}

	public static String colorToSingmaster(int color) {
		if (color < 0 || color > colorNames.length - 1) {
			return "X";
		}
		return singmasterNames[color];
	}
	
	public static int getUpperFaceColor(int facePosition) {
		if (facePosition >= 0 && facePosition < 4) {
			return ColorConverter.YELLOW;
		}
		if (facePosition == ColorConverter.WHITE) {
			return ColorConverter.ORANGE;
		}
		if (facePosition == ColorConverter.YELLOW) {
			return ColorConverter.RED;
		}
		return ColorConverter.UNSET_COLOR;
	}

	public static String getUpperFaceColorName(Integer facePosition) {
		if (facePosition >= 0 && facePosition < 4) {
			return getColorName(Rotator.UP);
		}
		if (facePosition == ColorConverter.WHITE) {
			return getColorName(Rotator.FRONT);
		}
		if (facePosition == ColorConverter.YELLOW) {
			return getColorName(Rotator.BACK);
		}
		return getColorName(ColorConverter.UNSET_COLOR);
	}
}
