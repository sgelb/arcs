package com.github.sgelb.arcs;

import java.util.Arrays;

public class CubeUpdater {
	
	private Square[] squares;
	
	public CubeUpdater() {
		// initialized to prevent null-pointer-exceptions.
		squares = new Square[54];
	}
	
	public Square[] updateSquares(Square[] squares) {
		this.squares = squares;
		int position = 0;
		Square[] updatedSquares = new Square[54];
		
		// Front face.
		for(int y = 1; y > -2; y--) {
			for(int x = -1; x < 2; x++) {
				updatedSquares[position] = getSquareAt(new SquareLocation(x, y, 1),
						new SquareLocation(0, 0, 1));
				position++;
			}
		}
		
		// Up face.
		for(int z = -1; z < 2; z++) {
			for(int x = -1; x < 2; x++) {
				updatedSquares[position] = getSquareAt(new SquareLocation(x, 1, z),
						new SquareLocation(0, 1, 0));
				position++;
			}
		}
		
		// Back face.
		for(int y = 1; y > -2; y--) {
			for(int x = 1; x > -2; x--) {
				updatedSquares[position] = getSquareAt(new SquareLocation(x, y, -1),
						new SquareLocation(0, 0, -1));
				position++;
			}
		}
		
		// Down face.
		for(int z = 1; z > -2; z--) {
			for(int x = -1; x < 2; x++) {
				updatedSquares[position] = getSquareAt(new SquareLocation(x, -1, z),
						new SquareLocation(0, -1, 0));
				position++;
			}
		}
		
		// Left face.
		for(int y = 1; y > -2; y--) {
			for(int z = -1; z < 2; z++) {
				updatedSquares[position] = getSquareAt(new SquareLocation(-1, y, z),
						new SquareLocation(-1, 0, 0));
				position++;
			}
		}
		
		// Right face.
		for(int y = 1; y > -2; y--) {
			for(int z = 1; z > -2; z--) {
				updatedSquares[position] = getSquareAt(new SquareLocation(1, y, z),
						new SquareLocation(1, 0, 0));
				position++;
			}
		}
		
		this.squares = updatedSquares;
		return this.squares;
	}
	
	
	private Square getSquareAt(SquareLocation loc, SquareLocation dir) {
		for(Square square : this.squares) {
			int[] getLoc = square.getLocation().getLocationsAsArray();
			int[] getDir = square.getDirection().getLocationsAsArray();
			
			if(Arrays.equals(loc.getLocationsAsArray(), getLoc)) {
				if(Arrays.equals(dir.getLocationsAsArray(), getDir)) {
					return square;
				}
			}
		}
		return null;
	}
	
	public Square[] getSquares() {
		return this.squares;
	}
	
}
