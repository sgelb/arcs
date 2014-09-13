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
	
	public Color[] getSquaresAt(int[] pos) {
		Color[] temp = new Color[3];
		temp[0] = squares[pos[0]];
		temp[1] = squares[pos[1]];
		temp[2] = squares[pos[2]];
		return temp;
	}
	
	public Color[] getSquares() {
		return squares;
	}
	
	public Color[] getOldSquares() {
		return oldSquares;
	}
	
	
	// Turns the whole face by 90/ -90 degree.
	
	public void turn90Deg(boolean reverse) {
		oldSquares = squares;
		
		int counter = 9;
		
		if(!reverse) {
			for(int i = 0; i < 9; i++) {
				
				/* this if-statement checks whether we are in bounds
				 * of modulo 9 or not (kind-of-modulo space, not exactly but
				 * it works). If not, add 7 to counter.
				 * Moving in the modulo 9 space makes it more dynamic.
				 */
				if(counter - 3 >= 0) {
					counter -= 3;
				} else {
					counter += 7;
				}
				squares[i] = oldSquares[counter];
			}
		} else {
			for(int i = 9; i >= 0; i--) {
				if(counter - 3 > 0) {
					counter -= 3;
				} else {
					counter += 7;
				}
				squares[i] = oldSquares[counter];
			}
		}
	}
	
	
}
