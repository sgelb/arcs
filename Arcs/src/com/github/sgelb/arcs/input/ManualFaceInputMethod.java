package com.github.sgelb.arcs.input;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.MotionEvent;

import com.github.sgelb.arcs.R;
import com.github.sgelb.arcs.cube.ColorConverter;
import com.github.sgelb.arcs.cube.Rotator;

public class ManualFaceInputMethod extends Observable implements FaceInputMethod {

	private static final String TAG = "ARCS::ManualCubeInputActivity";
	private Scalar unsetColor = new Scalar(0, 0, 0);
	private Scalar orange = new Scalar(255, 165, 0);
	private Scalar blue = new Scalar(0, 0, 255);
	private Scalar red = new Scalar(255, 0, 0);
	private Scalar green = new Scalar(0, 255, 0);
	private Scalar white = new Scalar(255, 255, 255);
	private Scalar yellow = new Scalar(255, 255, 0);
	private ArrayList<Scalar> colorChoices;

	private Context mContext;
	private int xOffset;
	private int padding;
	private int maxRows;
	private int maxCols;
	private int rowStart;
	private int colStart;


	// Array holding coordinates of rectangle overlays
    private ArrayList<Rect> rectangles;

    private ArrayList<Point> colorLines;

    // We are only interested in colors, so face is just an array of
    // ints aka ColorSquare.COLOR
    private ArrayList<Integer> face;

    private Integer currentFace;

    private int detectingColorsCountdown;
    private int detectingColorRounds;
    private ArrayList<HashMap<String, Double>> detectedColors;

	public ManualFaceInputMethod(Context mContext) {
		this.mContext = mContext;
		rectangles = new ArrayList<Rect>(9);
		// remembers chosen colors for rectangles
		colorChoices = new ArrayList<Scalar>(6);
		colorLines = new ArrayList<Point>(8);
		detectingColorsCountdown = 0;
		detectingColorRounds = 1;
	}

	@Override
	public void init(int height, ArrayList<Integer> face) {
		colorChoices.add(orange);
		colorChoices.add(blue);
		colorChoices.add(red);
		colorChoices.add(green);
		colorChoices.add(white);
		colorChoices.add(yellow);
		currentFace = Rotator.FRONT;
		if (face != null) {
			this.face = face;
		} else {
			this.face = new ArrayList<Integer>(Collections.nCopies(9, ColorConverter.UNSET_COLOR));
		}
		rectangles = calculateRectanglesCoordinates(height);
		addObserver((Observer) mContext);
		setMiddleFacelet();
	}

	@Override
	public void drawOverlay(Mat frame) {

		// draw rectangles
		for (int i=0; i<rectangles.size(); i++) {

			int strokewidth = 2;
			Scalar color = unsetColor;

			// auto-detect facelet colors except preset middle facelet
			if (i != 4 && detectingColorsCountdown > 0) {
				collectDetectedColors(i, frame.submat(rectangles.get(i)));
				if (detectingColorsCountdown == 1) {
					calculateColor(i);
				}
			}

			if (face.get(i) > ColorConverter.UNSET_COLOR) {
				strokewidth = 5;
				color = colorChoices.get(face.get(i));
			}
			Core.rectangle(frame, rectangles.get(i).tl(), rectangles.get(i).br(),
					color, strokewidth);
		}
		if (detectingColorsCountdown > 0) {
			detectingColorsCountdown--;
		}

		// draw color line representing upper face
		Core.line(frame, colorLines.get(0), colorLines.get(1),
				colorChoices.get(ColorConverter.getUpperFaceColor(currentFace)), 4);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int touchX = (int) event.getX();
		int touchY = (int) event.getY();
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			for (int i=0; i < rectangles.size(); i++) {
				// skip middle facelet as its color is already set
				if (rectangles.get(i).contains(new Point(touchX, touchY))) {
					if (i != 4) {
						showColorChooserDialog(i);
					} else {
						setAllFacelets();
					}
				}
			}
		}
		return true;
	}

	@Override
	public int getXOffset() {
		return xOffset;
	}

	@Override
	public int getPadding() {
		return padding;
	}


	public Point movePointHorizontally(Point a, Point b, int factor) {
		return new Point(a.x + factor*b.x, a.y);
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

	public ArrayList<Rect> calculateRectanglesCoordinates(int height) {
		// Creates ListArray of cv::rects to draw overlay

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
				rectSize.height + rectSpacing);

		// Initial points: top left/bottom right corners of first rectangle
		Point topLeft = new Point(faceMargin, faceMargin);
		Point bottomRight = new Point(faceMargin + rectSize.width,
				faceMargin + rectSize.height);

		// Values needed by positionViews()
		padding = faceMargin;
		xOffset = (int) (2*faceMargin + 3*rectSize.width + 3*rectSpacing);

		// Values needed by detectColors
		maxRows = (int) rectSize.height - 1;
		maxCols = (int) rectSize.width - 1;
		rowStart = (int) Math.ceil(maxRows/3.0);
		colStart = (int) Math.ceil(maxCols/3.0);

		// Create nine rectangles, three in each row
		for (int row=0; row < 3; row++) {
			for (int col=0; col < 3; col++) {
				tmpRect.add(getNextRectInRow(topLeft, bottomRight, rectDistance, col));
			}
			// move to next row
			topLeft = movePointVertically(topLeft, rectDistance);
			bottomRight = movePointVertically(bottomRight, rectDistance);
		}

		// upper color line
		colorLines.add(new Point(tmpRect.get(1).tl().x,
				tmpRect.get(1).tl().y - faceMargin/2));
		colorLines.add(new Point(tmpRect.get(1).tl().x + rectSize.width,
				tmpRect.get(1).tl().y - faceMargin/2));

		return tmpRect;
	}

	private void showColorChooserDialog(final int position) {
		// show dialog to choose color of rectangle at position
		AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
		alert.setTitle(R.string.chooseColorDialogTitle);
		alert.setItems(R.array.colors, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int colorChoice) {

				face.set(position, colorChoice);

				// test if all facelets on face are set
				// and get next face
				if (!currentFaceHasUnsetFacelets()) {
					setChanged();
					notifyObservers(face);
				}

			}
		});
		AlertDialog alertDialog = alert.create();
		alertDialog.show();
	}

	@Override
	public boolean currentFaceHasUnsetFacelets() {
		for (Integer color : face) {
			if (color == ColorConverter.UNSET_COLOR) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void changeFace(Integer currentFace, ArrayList<Integer> newFace) {
		// set next face and reset
		this.currentFace = currentFace;
		if (newFace == null) {
			face = new ArrayList<Integer>(Collections.nCopies(9, ColorConverter.UNSET_COLOR));
		} else {
			face = newFace;
		}
		setMiddleFacelet();
	}

	@Override
	public String getInstructionText(Integer faceId) {
		// we read the faces in the following order. a face's color is defined by its middle facelet.
		// 1. face: orange - front
		// 2. face: blue - right
		// 3. face: red - back
		// 4. face: green - left
		// 5. face: white - down
		// 6. face: yellow - up

		String colorFacingUser = ColorConverter.getColorName(faceId);
		String colorFacingUp = ColorConverter.getUpperFaceColorName(faceId);
		return String.format(mContext.getString(R.string.manualInstructionContent),
				colorFacingUser, colorFacingUp);
	}

	@Override
	public String getInstructionTitle(Integer faceId) {
		faceId++;
		String ordinalAppendix = null;
		if (faceId == 1) {
			ordinalAppendix = "st";
		} else if (faceId == 2) {
			ordinalAppendix = "nd";
		} else if (faceId == 3) {
			ordinalAppendix = "rd";
		} else {
			ordinalAppendix = "th";
		}

		String msg = String.valueOf(faceId) + ordinalAppendix;
		return mContext.getString(R.string.instructionTitle, msg);
	}

	private void setMiddleFacelet() {
		// set color of middle facelet to match current face
		face.set(4, currentFace);
	}

	private void setAllFacelets() {
		for (int i=0; i< face.size(); i++) {
			face.set(i, currentFace);
		}
		setChanged();
		notifyObservers(face);
	}

	private void collectDetectedColors(int faceletId, Mat facelet) {
		Imgproc.cvtColor(facelet, facelet, Imgproc.COLOR_RGBA2BGR);
		Imgproc.cvtColor(facelet, facelet, Imgproc.COLOR_BGR2HLS);

		// We read hue and luminance values from 5 points, which are arranged like the "5" dots on a dice
		// and calculate the mean over a period of 60 frames
		double hue = facelet.get(maxRows/2, maxCols/2)[0];
		double lum = facelet.get(maxRows/2, maxCols/2)[1];
		for (int row=rowStart; row < maxRows; row = row + rowStart) {
			for (int col=colStart; col < maxCols; col = col + colStart) {
				hue += facelet.get(row, col)[0];
				lum += facelet.get(row, col)[1];
			}
		}
		hue /= 5;
		lum /= 5;

		detectedColors.get(faceletId).put("hue", detectedColors.get(faceletId).get("hue") + hue);
		detectedColors.get(faceletId).put("lum", detectedColors.get(faceletId).get("lum") + lum);
	}

	void calculateColor(int faceletId) {
		double hue = detectedColors.get(faceletId).get("hue") / detectingColorRounds;
		double lum = detectedColors.get(faceletId).get("lum") / detectingColorRounds;

		int color = ColorConverter.UNSET_COLOR;
		if (hue < 14 && hue > 6) {
			color = ColorConverter.ORANGE;
		} else if (hue < 130 && hue > 100 && lum < 130) {
			color = ColorConverter.BLUE;
		} else if (hue <= 6 || hue > 170) {
			color = ColorConverter.RED;
		} else if (hue < 75 && hue > 42) {
			color = ColorConverter.GREEN;
		} else if (hue < 30 && hue > 15) {
			color = ColorConverter.YELLOW;
		} else if (lum > 100) {
			color = ColorConverter.WHITE;
		}

		Log.d(TAG, "COLOR: " + faceletId + ":"+ color);
		Log.d(TAG, "COLOR H: " + hue + "L: " + lum);
		face.set(faceletId, color);

		// test if all facelets on face are set
		if (!currentFaceHasUnsetFacelets()) {
			((Activity) mContext).runOnUiThread(new Runnable() {
				@Override
				public void run()  {
					setChanged();
					notifyObservers(face);
				}
			});
		}
	}

	private void initiateDetectedColors() {
		detectedColors = null;
		detectedColors = new ArrayList<HashMap<String, Double>>();
		for (int i = 0; i < face.size(); i++) {
			detectedColors.add(new HashMap<String, Double>());
			detectedColors.get(i).put("hue", (double) 0);
			detectedColors.get(i).put("lum", (double) 0);
		}
	}

	@Override
	public void startDetectingColors() {
		// fetch colors of facelets the next N frames
		initiateDetectedColors();
		detectingColorsCountdown = detectingColorRounds;

	}
}
