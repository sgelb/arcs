package com.github.sgelb.arcs;

import java.util.ArrayList;

import android.util.Log;

public class RubiksCube {
	
	private static final String TAG = "ARCS::RubiksCube";

	private Rotator rotator;
	private CubeUpdater updater;
	private Facelet[] facelets;
	
	public RubiksCube() {
		this.rotator = new Rotator();
		this.updater = new CubeUpdater();
		this.facelets = rotator.getSquares();
	}
	
	public void rotateFront() {
		rotator.rotateFront();
		update();
	}
	
	public void rotateBack() {
		rotator.rotateBack();
		update();
	}
	
	public void rotateUp() {
		rotator.rotateUp();
		update();
	}
	
	public void rotateDown() {
		rotator.rotateDown();
		update();
	}
	
	public void rotateLeft() {
		rotator.rotateLeft();
		update();
	}
	
	public void rotateRight() {
		rotator.rotateRight();
		update();
	}

	public void update() {
		facelets = rotator.getSquares();
		facelets = updater.updateSquares(facelets);
	}
	
	public Facelet[] getSquares() {
		return facelets;
	}
	
	public void setSquares(Facelet[] squares) {
		this.facelets = squares;
	}
	
	public Facelet[] getFace(int facename) {
		Facelet[] face = new Facelet[9];
		for (int i=0; i<9; i++) {
			face[i] = facelets[9*facename + i];
		}
		return face;
	}
	
	public ArrayList<Integer> getFaceColor(int facename) {
		ArrayList<Integer> face = new ArrayList<>(9);
		try {
			for (int i=0; i<9; i++) {
				face.add(facelets[9*facename + i].getColor());
			}
		} catch (IndexOutOfBoundsException e) {
			face = null;
		}
		return face;
	}

	public int[] getFaceletColors() {
		int[] colors = new int[54];
		for (int i=0; i<facelets.length; i++) {
			colors[i] = facelets[i].getColor();
		}
		return colors;
	}

	public void setFaceletColors(int[] colors) {
		for (int i=0; i<colors.length; i++) {
			facelets[i].setColor(colors[i]);
		}
	}
	
	public void setFaceColor(int facename, ArrayList<Integer> face) {
		for(int i = 0; i < 9; i++) {
			facelets[9*facename + i].setColor(face.get(i));
		}
	}
	
	public boolean hasUnsetSquares() {
		for (Facelet facelet : facelets) {
			if (facelet.getColor() == ColorConverter.UNSET_COLOR) {
				return true;
			}
		}
		return false;
	}
	
	// toString-methods for testing purposes.
	
	@Override
	public String toString() {
		String string = "";
		for(int i = 0; i < facelets.length; i += 3) {
			string += facelets[i].getColor() + " " + facelets[i + 1].getColor() + " " + facelets[i + 2].getColor() + "\n";
		}
		return string;
	}
	
	public String getPositions() {
		String string = "";
		for(int i = 0; i < 9; i++) {
			string += "Color: " + facelets[i].getColor() + ", " + facelets[i].getLocation().toString();
		}
		return string;
	}

	public String getSingmasterNotation() {
		StringBuilder singmaster = new StringBuilder();
		
		// URFDLB
		for (Facelet facelet: getFace(Rotator.UP)) {
			singmaster.append(ColorConverter.colorToSingmaster(facelet.getColor()));
		}
		for (Facelet facelet: getFace(Rotator.RIGHT)) {
			singmaster.append(ColorConverter.colorToSingmaster(facelet.getColor()));
		}
		for (Facelet facelet: getFace(Rotator.FRONT)) {
			singmaster.append(ColorConverter.colorToSingmaster(facelet.getColor()));
		}
		for (Facelet facelet: getFace(Rotator.DOWN)) {
			singmaster.append(ColorConverter.colorToSingmaster(facelet.getColor()));
		}
		for (Facelet facelet: getFace(Rotator.LEFT)) {
			singmaster.append(ColorConverter.colorToSingmaster(facelet.getColor()));
		}
		for (Facelet facelet: getFace(Rotator.BACK)) {
			singmaster.append(ColorConverter.colorToSingmaster(facelet.getColor()));
		}
		Log.d(TAG, "CUBE: " + singmaster);
		return singmaster.toString();
	}
}
