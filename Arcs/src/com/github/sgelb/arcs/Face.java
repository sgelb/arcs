package com.github.sgelb.arcs;

import android.graphics.Color;

public class Face {
	
	private Color[] squares;
	private Color[] oldSquares;
	
	public Face() {
		squares = new Color[9];
		oldSquares = squares;
	}
	
	
	//##### setter #####
	
	// Old colors are saved when setting new colors for new positions
	public void setSquare(int pos, Color color) {
		oldSquares[pos] = squares[pos];
		squares[pos] = color;
	}
	
	public void setSquares(Color[] newSquares) {
		oldSquares = squares;
		squares = newSquares;
	}
	
	
	//##### getter #####
	
	public Color getSquare(int pos) {
		return squares[pos];
	}
	
	public Color[] getSquares() {
		return squares;
	}
	
	public Color[] getOldSquares() {
		return oldSquares;
	}

}
