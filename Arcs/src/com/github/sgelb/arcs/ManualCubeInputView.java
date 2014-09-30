package com.github.sgelb.arcs;

import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;

import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;


public class ManualCubeInputView implements CubeInputView {

	private static final String TAG = "ARCS::ManualCubeInputActivity";
	private static final Scalar RECTCOLOR = new Scalar(200, 200, 200);
	private int width;
	private int xOffset;
	private int padding;
	// Array holding coordinates of rectangle overlays
    private ArrayList<Rect> rectangles;


	public ManualCubeInputView() {
	}

	@Override
	public void init(int width, int height) {
		this.width = width;
		this.rectangles = new ArrayList<Rect>(9);
		if (rectangles.isEmpty()) {
    		rectangles = calculateRectanglesCoordinates(width, height);
    	}
	}

	@Override
	public void drawOverlay(Mat frame) {
		for (Rect rect : rectangles) {
			Core.rectangle(frame, rect.tl(), rect.br(), RECTCOLOR, 2);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// view is y-mirrored, therefor we have to substract x from width
		int touchX = width - (int) event.getX();
		int touchY = (int) event.getY();
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			for (int i=0; i < rectangles.size(); i++) {
				if (rectangles.get(i).contains(new Point(touchX, touchY))) {
					Log.d(TAG, "Touched rectangle " + i);
					Toast.makeText(MainActivity.getContext(), "RECT " + i, Toast.LENGTH_SHORT).show();
				}
			}
		}
		return true;
	}

	@Override
	public int getXOffset() {
		return this.xOffset;
	}
	
	@Override
	public int getPadding() {
		return this.padding;
	}
	
	public Point movePointHorizontally(Point a, Point b, int factor) {
		// beware: due to y-mirrored output, we have to substract x
		return new Point(a.x - factor*b.x, a.y);
	}

	public Rect getNextRectInRow(Point tl, Point br, Point dist, int factor) {
		// calculate next rectangle in row 
		return new Rect(
				movePointHorizontally(tl, dist, factor),
				movePointHorizontally(br, dist, factor));
	}

	public Point movePointVertically(Point a, Point b) {
		return new Point(a.x, a.y + b.y);
	}

	public ArrayList<Rect> calculateRectanglesCoordinates(int width, int height) {
		// Creates ListArray of cv::rects to draw overlay

		// Beware: because front camera output is y-mirrored,
		// point of origin is on top left corner and some values 
		// have to be negated/subtracted for correct calculating

		// Rectangle positions in ListArray:
		// |0|1|2|
		// |3|4|5|
		// |6|7|8|

		ArrayList<Rect> tmpRect = new ArrayList<Rect>(9);

		// spacing between rects
		int rectSpacing = 7*height/100;

		// margin around face
		int faceMargin = 10*height/100;

		// Size of single rect
		Size rectSize = new Size(
				(height - 2*rectSpacing - 2*faceMargin)/3,
				(height - 2*rectSpacing - 2*faceMargin)/3); 

		// vector from tl of rect to tl of rect on the right
		Point rectDistance = new Point(rectSize.width + rectSpacing, 
				(double) rectSize.height + rectSpacing);

		// Initial points: top left/bottom right corners of first rectangle
		Point topLeft = new Point(width - faceMargin, faceMargin);
		Point bottomRight = new Point(width - faceMargin - rectSize.width,
				faceMargin + rectSize.height);

		// Values needed by positionViews()
		this.padding = faceMargin;
		this.xOffset = (int) (2*faceMargin + 3*rectSize.width + 2*rectSpacing);

		// Create nine rectangles, three in each row
		for (int row=0; row < 3; row++) {
			for (int col=0; col < 3; col++) {
				tmpRect.add(getNextRectInRow(topLeft, bottomRight, rectDistance, col));
			}
			// move to next row
			topLeft = movePointVertically(topLeft, rectDistance);
			bottomRight = movePointVertically(bottomRight, rectDistance);
		}
		return tmpRect;
	}

}
