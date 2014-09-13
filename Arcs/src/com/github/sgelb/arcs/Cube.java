package com.github.sgelb.arcs;

import java.util.ArrayList;
import android.graphics.Color;

public class Cube {
	
	private int[] rotF;
	private int[] rotB;
	private int[] rotU;
	private int[] rotD;
	private int[] rotL;
	private int[] rotR;
	
	private Face front;
	private Face back;
	private Face up;
	private Face down;
	private Face left;
	private Face right;
	
	private ArrayList<Face> faces;
	
	/* Constructor initializes the faces and adds them
	 * to the ArrayList in the addFaces method.
	 */
	public Cube() {
		faces = new ArrayList<Face>();
		front = new Face();
		back = new Face();
		up = new Face();
		down = new Face();
		left = new Face();
		right = new Face();
		
		addFaces();
		
		initRotations();
	}
	
	public void addFaces() {
		faces.add(front);
		faces.add(back);
		faces.add(up);
		faces.add(down);
		faces.add(left);
		faces.add(right);
	}
	
	/* These are the positions of the squares for certain rotations.
	 * If you i.e. rotate front, every face moves different squares.
	 */
	public void initRotations() {
		rotU = new int[]{0, 1, 2};
		rotD = new int[]{6, 7, 8};
		rotL = new int[]{0, 3, 6};
		rotR = new int[]{2, 5, 8};
	}
	
	//##### movements #####
	
	public void moveR(boolean reverse) {
		/* There arrays save the current squares at certain positions.
		 * The first line means: get the squares 2, 5 and 8 (rotR)
		 * from the front face. These are the squares which will change
		 * on the front face if you rotate the right face.
		 * The other move methods work analog to this one.
		 * If you want to do move in the opposite direction, call this
		 * function with the parameter reverse = true.
		 */
		Color[] first = front.getSquaresAt(rotR);
		Color[] seccond = up.getSquaresAt(rotR);
		Color[] third = back.getSquaresAt(rotL);
		Color[] fourth = down.getSquaresAt(rotR);
		
		/* The just fetched square positions are now given to the new
		 * faces. If you move the right face, the new squares on the front
		 * face are now the outer right squares from the down face.
		 */
		if(!reverse) {
			up.setSquares(first);
			back.setSquares(seccond);
			down.setSquares(third);
			front.setSquares(fourth);
			right.turn90Deg(false);
		} else {
			up.setSquares(third);
			back.setSquares(fourth);
			down.setSquares(first);
			front.setSquares(seccond);
			right.turn90Deg(true);
		}
	}
	
	public void moveL(boolean reverse) {
		Color[] first = front.getSquaresAt(rotL);
		Color[] seccond = up.getSquaresAt(rotR);
		Color[] third = back.getSquaresAt(rotL);
		Color[] fourth = down.getSquaresAt(rotL);
			
		if(!reverse) {
			up.setSquares(third);
			back.setSquares(fourth);
			down.setSquares(first);
			front.setSquares(seccond);
			left.turn90Deg(false);
		} else {
			up.setSquares(first);
			back.setSquares(seccond);
			down.setSquares(third);
			front.setSquares(fourth);
			left.turn90Deg(true);
		}
	}
	
	public void moveU(boolean reverse) {
		Color[] first = front.getSquaresAt(rotU);
		Color[] seccond = right.getSquaresAt(rotU);
		Color[] third = back.getSquaresAt(rotU);
		Color[] fourth = left.getSquaresAt(rotU);
			
		if(!reverse) {
			front.setSquares(seccond);
			right.setSquares(third);
			back.setSquares(fourth);
			left.setSquares(first);
			up.turn90Deg(false);
		} else {
			front.setSquares(fourth);
			right.setSquares(first);
			back.setSquares(seccond);
			left.setSquares(third);
			up.turn90Deg(true);
		}
	}
	
	public void moveD(boolean reverse) {
		Color[] first = front.getSquaresAt(rotD);
		Color[] seccond = right.getSquaresAt(rotD);
		Color[] third = back.getSquaresAt(rotD);
		Color[] fourth = left.getSquaresAt(rotD);
			
		if(!reverse) {
			front.setSquares(fourth);
			right.setSquares(first);
			back.setSquares(seccond);
			left.setSquares(third);
			down.turn90Deg(false);
		} else {
			front.setSquares(seccond);
			right.setSquares(third);
			back.setSquares(fourth);
			left.setSquares(first);
			down.turn90Deg(true);
		}
	}
	
	public void moveF(boolean reverse) {
		Color[] first = up.getSquaresAt(rotD);
		Color[] seccond = right.getSquaresAt(rotL);
		Color[] third = down.getSquaresAt(rotU);
		Color[] fourth = left.getSquaresAt(rotR);
			
		if(!reverse) {
			up.setSquares(fourth);
			right.setSquares(first);
			down.setSquares(seccond);
			left.setSquares(third);
			front.turn90Deg(false);
		} else {
			up.setSquares(seccond);
			right.setSquares(third);
			down.setSquares(fourth);
			left.setSquares(first);
			front.turn90Deg(true);
		}
	}
	
	public void moveB(boolean reverse) {
		Color[] first = up.getSquaresAt(rotU);
		Color[] seccond = right.getSquaresAt(rotR);
		Color[] third = down.getSquaresAt(rotD);
		Color[] fourth = left.getSquaresAt(rotL);
			
		if(!reverse) {
			up.setSquares(seccond);
			right.setSquares(third);
			down.setSquares(fourth);
			left.setSquares(first);
			back.turn90Deg(false);
		} else {
			up.setSquares(fourth);
			right.setSquares(first);
			down.setSquares(seccond);
			left.setSquares(third);
			back.turn90Deg(true);
		}
	}

}
