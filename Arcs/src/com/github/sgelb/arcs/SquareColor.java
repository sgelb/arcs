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

	public static String getStringForUpperFace(Integer faceId) {
		String upperFaceColorString = getString(SquareColor.UNSET_COLOR);
		if (faceId < 4) {
			upperFaceColorString = SquareColor.getString(SquareColor.YELLOW);
		} else if (faceId == SquareColor.WHITE) {
			upperFaceColorString = SquareColor.getString(SquareColor.ORANGE);
		} else if (faceId == SquareColor.YELLOW) {
			upperFaceColorString = SquareColor.getString(SquareColor.RED);
		}
		return upperFaceColorString;
	}

//	public static int fromSingmasterToColor(char character) {
//		int color = -1;
//		
//		switch (character) {
//		case 'U':
//			color = YELLOW;
//			break;
//		case 'R':
//			color = BLUE;
//			break;
//		case 'F':
//			color = ORANGE;
//			break;
//		case 'D':
//			color = WHITE;
//			break;
//		case 'L':
//			color = GREEN;
//			break;
//		case 'B':
//			color = RED;
//			break;
//		default:
//			break;
//		}
//		return color;
//	}
	
}
