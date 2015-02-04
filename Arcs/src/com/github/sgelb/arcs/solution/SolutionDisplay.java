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
		int face = Rotator.FRONT;
		char rotation = step.charAt(0);
		int count = Integer.valueOf(step.charAt(1));

		switch(rotation) {
		case 'f':
			face = Rotator.FRONT;
			for(int i = 0; i < count; i++) {
				cube.rotateFront();
			}
			break;
		case 'r':
			face = Rotator.RIGHT;
			for(int i = 0; i < count; i++) {
				cube.rotateRight();
			}
			break;
		case 'b':
			face = Rotator.BACK;
			for(int i = 0; i < count; i++) {
				cube.rotateBack();
			}
			break;
		case 'l':
			face = Rotator.LEFT;
			for(int i = 0; i < count; i++) {
				cube.rotateLeft();
			}
			break;
		case 'd':
			face = Rotator.DOWN;
			for(int i = 0; i < count; i++) {
				cube.rotateDown();
			}
			break;
		case 'u':
			face = Rotator.UP;
			for(int i = 0; i < count; i++) {
				cube.rotateUp();
			}
			break;
		default:
			break;
		}

		// Returns an array of Facelets.
		return cube.getFace(face);
	}

}
