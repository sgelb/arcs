package com.github.sgelb.arcs.solution;

import com.github.sgelb.arcs.cube.Facelet;
import com.github.sgelb.arcs.cube.Rotator;
import com.github.sgelb.arcs.cube.RubiksCube;

/* This class is taking the cube and calculates the next
 * step only when it's needed.
 */

public class SolutionDisplay {
	
	private RubiksCube cube;
	
	// The cube is given from the activity.
	
	public SolutionDisplay(RubiksCube cube) {
		this.cube = cube;
	}
	
	public Facelet[] getNextStep(String step) {
		
		/* step is a string that looks like "R3".
		 * The first char is the rotation and the second
		 * char is the count, how often this rotation
		 * should be done.
		 */
		
		char rotation = step.charAt(0);
		int count = Integer.valueOf(step.charAt(1));
		
		switch(rotation) {
		case 'f':
			for(int i = 0; i < count; i++) {
				cube.rotateFront();
			}
			break;
		case 'b':
			for(int i = 0; i < count; i++) {
				cube.rotateBack();
			}
			break;
		case 'u':
			for(int i = 0; i < count; i++) {
				cube.rotateUp();
			}
			break;
		case 'd':
			for(int i = 0; i < count; i++) {
				cube.rotateDown();
			}
			break;
		case 'l':
			for(int i = 0; i < count; i++) {
				cube.rotateLeft();
			}
			break;
		case 'r':
			for(int i = 0; i < count; i++) {
				cube.rotateRight();
			}
			break;
		default:
			break;
		}
		
		// Returns an array of Facelets.
		return cube.getFace(Rotator.FRONT);
	}

}
