package com.github.sgelb.arcs.input;

import java.util.ArrayList;

import org.opencv.core.Mat;

import android.view.MotionEvent;

public interface FaceInputMethod {

	public void init(int height, ArrayList<Integer> face);
	public void drawOverlay(Mat frame);
	public boolean onTouchEvent(MotionEvent event);
	public int getXOffset();
	public int getPadding();
	public String getInstructionText(Integer faceId);
	public String getInstructionTitle(Integer faceId);
	public void changeFace(Integer faceId, ArrayList<Integer> face);
	public boolean currentFaceHasUnsetFacelets();
	public void startDetectingColors();
}
