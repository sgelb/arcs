package com.github.sgelb.arcs;

import java.util.ArrayList;
import java.util.Observable;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.MotionEvent;

public class ManualCubeInputMethod extends Observable implements CubeInputMethod {

	private static final String TAG = "ARCS::ManualCubeInputActivity";
	private Scalar unsetColor = new Scalar(0, 0, 0);
	private Scalar blue = new Scalar(0, 0, 255);
	private Scalar green = new Scalar(0, 255, 0);
	private Scalar orange = new Scalar(255, 165, 0);
	private Scalar red = new Scalar(255, 0, 0);
	private Scalar white = new Scalar(255, 255, 255);
	private Scalar yellow = new Scalar(255, 255, 0);
	private ArrayList<Scalar> colorChoices;
	
	private Context mContext;
	private int width;
	private int xOffset;
	private int padding;
	// Array holding coordinates of rectangle overlays
    private ArrayList<Rect> rectangles;
    private ArrayList<Scalar> rectColors;
    private Square[] squares;


	public ManualCubeInputMethod(Context mContext) {
		this.mContext = mContext;
		this.rectangles = new ArrayList<Rect>(9);
		this.colorChoices = new ArrayList<Scalar>(6);
		this.rectColors = new ArrayList<Scalar>(9);
	}

	@Override
	public void init(int width, int height) {
		this.width = width;
		colorChoices.add(blue);
		colorChoices.add(green);
		colorChoices.add(orange);
		colorChoices.add(red);
		colorChoices.add(white);
		colorChoices.add(yellow);
		if (rectangles.isEmpty()) {
    		rectangles = calculateRectanglesCoordinates(width, height);
    	}
	}

	@Override
	public void drawOverlay(Mat frame) {
		for (int i=0; i<rectangles.size(); i++) {
			int strokewidth = 2;
			if (rectColors.get(i) != unsetColor) {
				strokewidth = 5;
			}
			Core.rectangle(frame, rectangles.get(i).tl(), rectangles.get(i).br(), 
					rectColors.get(i), strokewidth);
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
					setColorDialog(i);
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
				rectColors.add(unsetColor);
			}
			// move to next row
			topLeft = movePointVertically(topLeft, rectDistance);
			bottomRight = movePointVertically(bottomRight, rectDistance);
		}
		return tmpRect;
	}

	private void setColorDialog(final int position) {
		// show dialog to choose color of rectangle at position
		AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
		alert.setTitle(R.string.chooseColorDialogTitle);
		alert.setItems(R.array.colors, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				rectColors.set(position, colorChoices.get(which));
				setChanged();
				notifyObservers(position);
			}
		});
		AlertDialog alertDialog = alert.create();
		alertDialog.show();
	}
	
	public Square[] getSquares() {
		return squares;
	}
	
	
}
