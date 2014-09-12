package com.github.sgelb.arcs;

import java.util.ArrayList;

public class Cube {
	
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
	}
	
	public void addFaces() {
		faces.add(front);
		faces.add(back);
		faces.add(up);
		faces.add(down);
		faces.add(left);
		faces.add(right);
	}

}
