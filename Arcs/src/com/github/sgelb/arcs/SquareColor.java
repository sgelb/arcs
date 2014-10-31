package com.github.sgelb.arcs;



public class SquareColor {
	
	public static final int UNSET_COLOR = -1;
	public static final int ORANGE = 0;
	public static final int BLUE = 1;
	public static final int RED = 2;
	public static final int GREEN = 3;
	public static final int WHITE = 4;
	public static final int YELLOW = 5;
	
	public static String getColorString(int color) {
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

	public static String getSingmasterString(int color) {
		String colorString = "X";

		switch (color) {
		case ORANGE:
			colorString = "F";
			break;
		case BLUE:
			colorString = "R";
			break;
		case RED:
			colorString = "B";
			break;
		case GREEN:
			colorString = "L";
			break;
		case WHITE:
			colorString = "D";
			break;
		case YELLOW:
			colorString = "U";
			break;
		default:
			colorString = "X";
			break;
		}
		return colorString;
	}
	
	public static int getColorOfUpperFace(int faceId) {
		int upperFaceColor = SquareColor.UNSET_COLOR; 
		if (faceId < 4) {
			upperFaceColor = SquareColor.YELLOW;
		} else if (faceId == SquareColor.WHITE) {
			upperFaceColor = SquareColor.ORANGE;
		} else if (faceId == SquareColor.YELLOW) {
			upperFaceColor = SquareColor.RED;
		}
		return upperFaceColor;
	}

	public static String getStringForUpperFace(Integer facePosition) {
		String upperFaceColorString = getColorString(Rotator.UP);
		if (facePosition < 4) {
			upperFaceColorString = getColorString(Rotator.UP);
		} else if (facePosition == SquareColor.WHITE) {
			upperFaceColorString = getColorString(Rotator.FRONT);
		} else if (facePosition == SquareColor.YELLOW) {
			upperFaceColorString = getColorString(Rotator.BACK);
		}
		return upperFaceColorString;
	}
	
}
