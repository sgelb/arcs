package com.github.sgelb.arcs.cube;

import java.util.Arrays;



public class CubeUpdater {
	
	private Facelet[] facelets;
	
	public CubeUpdater() {
		// initialized to prevent null-pointer-exceptions.
		facelets = new Facelet[54];
	}
	
	public Facelet[] updateSquares(Facelet[] squares) {
		this.facelets = squares;
		int position = 0;
		Facelet[] updatedSquares = new Facelet[54];
		
		// Front face.
		for(int y = 1; y > -2; y--) {
			for(int x = -1; x < 2; x++) {
				updatedSquares[position] = getSquareAt(new SquareLocation(x, y, 1),
						new SquareLocation(0, 0, 1));
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

		// Back face.
		for(int y = 1; y > -2; y--) {
			for(int x = 1; x > -2; x--) {
				updatedSquares[position] = getSquareAt(new SquareLocation(x, y, -1),
						new SquareLocation(0, 0, -1));
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
				
		
		// Down face.
		for(int z = 1; z > -2; z--) {
			for(int x = -1; x < 2; x++) {
				updatedSquares[position] = getSquareAt(new SquareLocation(x, -1, z),
						new SquareLocation(0, -1, 0));
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
		
		this.facelets = updatedSquares;
		return this.facelets;
	}
	
	
	private Facelet getSquareAt(SquareLocation loc, SquareLocation dir) {
		for(Facelet facelet : this.facelets) {
			int[] getLoc = facelet.getLocation().getLocation();
			int[] getDir = facelet.getDirection().getLocation();
			
			if(Arrays.equals(loc.getLocation(), getLoc)) {
				if(Arrays.equals(dir.getLocation(), getDir)) {
					return facelet;
				}
			}
		}
		return null;
	}
	
	public Facelet[] getSquares() {
		return this.facelets;
	}
	
}
