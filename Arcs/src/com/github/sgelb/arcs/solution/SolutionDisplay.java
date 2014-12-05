package com.github.sgelb.arcs.solution;

import com.github.sgelb.arcs.cube.Facelet;
import com.github.sgelb.arcs.cube.Rotator;
import com.github.sgelb.arcs.cube.RubiksCube;

public class SolutionDisplay {
	
	public RubiksCube cube;
	public String[] solutionString;
	public String nextStep;
	public int positionOfIndex;
	public int[][] steps;
	
	public SolutionDisplay(RubiksCube cube, String solution) {
		this.cube = cube;
		solution = solution.toLowerCase();
		positionOfIndex = 0;
		
		/* steps is a two dimesional int array which has the maximum length
		 * of the solution string. Each array contains an array of length 9
		 * which are the facelets of the front face of the cube.
		 */
		
		solutionString = solution.split(" ");
		steps = new int[solutionString.length][9];
		parse();
	}
	
	public void parse() {
		for(String step : solutionString) {
			rotations(step.charAt(0), Integer.valueOf(step.charAt(1)));
		}
	}
	
	public void rotations(char rotation, int count) {
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
		prepareSteps();
		positionOfIndex++;
	}
	
	public void prepareSteps() {
		Facelet[] front = cube.getFace(Rotator.FRONT);
		for(int i = 0; i < 9; i++) {
			steps[positionOfIndex][i] = front[i].getColor();
		}
	}
	
	public int[][] getSteps() {
		return steps;
	}

}
