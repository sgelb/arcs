package com.github.sgelb.arcs.input;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.MotionEvent;

import com.github.sgelb.arcs.R;
import com.github.sgelb.arcs.cube.ColorConverter;
import com.github.sgelb.arcs.cube.Rotator;

public class ManualFaceInputMethod extends Observable implements FaceInputMethod {

	@SuppressWarnings("unused")
	private static final String TAG = "ARCS::ManualCubeInputActivity";

	private Context mContext;

	// Array holding coordinates of rectangle overlays
    private ArrayList<Rect> rectangles;
    // Array holding coordinates of color line
    private ArrayList<Point> colorLine;

    // We are only interested in colors, so face is just an array of
    // ints aka ColorSquare.COLOR
    private ArrayList<Integer> face;

    private Integer currentFace;

	public ManualFaceInputMethod(Context mContext) {
		this.mContext = mContext;
		rectangles = new ArrayList<Rect>(9);
		// remembers chosen colors for rectangles
		colorLine = new ArrayList<Point>(2);
	}

	@Override
	public void init(ArrayList<Rect> rectangles, ArrayList<Integer> face) {
		currentFace = Rotator.FRONT;
		if (face != null) {
			this.face = face;
		} else {
			this.face = new ArrayList<Integer>(Collections.nCopies(9, ColorConverter.UNSET_COLOR));
		}
		this.rectangles = rectangles;
		colorLine.add(0, new Point(rectangles.get(1).tl().x, rectangles.get(1).tl().y/2));
		colorLine.add(1, new Point(rectangles.get(1).br().x, rectangles.get(1).tl().y/2));
		addObserver((Observer) mContext);
		setMiddleFacelet();
	}

	@Override
	public void drawOverlay(Mat frame) {
		for (int i=0; i<rectangles.size(); i++) {
			int strokewidth = 2;
			Scalar color = new Scalar(0, 0, 0);

			if (face.get(i) > ColorConverter.UNSET_COLOR) {
				strokewidth = 5;
				color = ColorConverter.colorChoices[face.get(i)];
			}
			Core.rectangle(frame, rectangles.get(i).tl(), rectangles.get(i).br(),
					color, strokewidth);
		}
		Core.line(frame, colorLine.get(0), colorLine.get(1),
				ColorConverter.colorChoices[ColorConverter.getUpperFaceColor(currentFace)], 4);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// view is y-mirrored, therefor we have to substract x from width
//		int touchX = width - (int) event.getX();
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
}
