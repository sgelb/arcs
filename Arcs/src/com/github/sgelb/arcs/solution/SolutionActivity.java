package com.github.sgelb.arcs.solution;

import java.util.ArrayList;

import org.opencv.core.Rect;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.sgelb.arcs.MainActivity;
import com.github.sgelb.arcs.R;
import com.github.sgelb.arcs.cube.ColorConverter;
import com.github.sgelb.arcs.cube.Facelet;
import com.github.sgelb.arcs.cube.LayoutCalculator;
import com.github.sgelb.arcs.cube.RubiksCube;

public class SolutionActivity extends Activity {

	private RubiksCube cube;
	private String solution;
	private String[] solutions;
	private SolutionDisplay solver;
	private int currentStep;
	private int currentFace;
	private int totalSteps;
	private int width;
	private int height;
	private int xOffset;
	private int padding;
	private ArrayList<Rect> rectangles;
	private Canvas canvas;
	private TextView solutionTitle;
	private TextView solutionText;
	private Facelet[] currentFacelets;
	private ImageView imageView;

	private RotateAnimation rotate90cw;
	private RotateAnimation rotate180;
	private RotateAnimation rotate90ccw;


	private ArrayList<RectF> rects;
	private RotateAnimation[] animations;

	public SolutionActivity() {
		rects = new ArrayList<RectF>();

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.solution_activity);

		Intent intent = getIntent();

		cube = new RubiksCube();
		cube.setFaceletColors(intent.getIntArrayExtra(MainActivity.CUBE));
		solver = new SolutionDisplay(cube);

		// define animations
		rotate90cw = new RotateAnimation(0, 90,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotate90cw.setRepeatCount(-1);
		rotate90cw.setDuration(3000);

		rotate180 = new RotateAnimation(0, 180,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotate180.setRepeatCount(-1);
		rotate180.setDuration(6000);

		rotate90ccw = new RotateAnimation(0, -90,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotate90ccw.setRepeatCount(-1);
		rotate90ccw.setDuration(3000);

		solution = intent.getStringExtra(MainActivity.SOLUTION);
		width = intent.getIntExtra(MainActivity.WIDTH, 0);
		height = intent.getIntExtra(MainActivity.HEIGHT, 0);


		imageView = (ImageView) findViewById(R.id.imageView);
		imageView.getLayoutParams().width = height;

		solutionTitle = (TextView) findViewById(R.id.solutionTitle);
		solutionText = (TextView) findViewById(R.id.solutionText);

		// create canvas
		int h = (int) Math.sqrt((2*height*height));
		Bitmap bitmap = Bitmap.createBitmap(h, h, Bitmap.Config.ARGB_8888);
		canvas = new Canvas(bitmap);
		imageView.setImageBitmap(bitmap);


		// FIXME: correct calculation of previous rotation
		ImageButton prevBtn = (ImageButton) findViewById(R.id.solPrevBtn);
		//		prevBtn.setOnClickListener(new View.OnClickListener() {
		//			@Override
		//			public void onClick(View v) {
		//				if (currentStep > 0) {
		//					currentStep--;
		//					drawSolutionStep();
		//				}
		//			}
		//		});

		ImageButton forwardBtn = (ImageButton) findViewById(R.id.solFwdBtn);
		forwardBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (currentStep < solutions.length - 1) {
					currentStep++;
					drawSolutionStep();
				}
			}
		});


		// Rectangles
		LayoutCalculator lc = new LayoutCalculator(width, height);
		rectangles = lc.calculateRectanglesCoordinates();
		xOffset = lc.getXOffset();
		padding = lc.getPadding();

		// precalculate android.graphics.RectF
		for (Rect rect : rectangles) {
			rects.add(new RectF((float) rect.tl().x+(h-height)/2, (float) rect.tl().y+(h-height)/2,
					(float) rect.br().x+(h-height)/2, (float) rect.br().y+(h-height)/2));
		}

		// split solution string
		solutions = solution.split(" ");
		totalSteps = solutions.length;
		currentStep = 0;

		// present 1st solution step
		drawSolutionStep();
	}


	private void drawSolutionStep() {
		currentFacelets = solver.getNextStep(solutions[currentStep]);
		drawCurrentFace();

		solutionTitle.setText(getString(R.string.solutionStepTitle, currentStep + 1, totalSteps));

		int rotationIndex = Character.getNumericValue(solutions[currentStep].charAt(1)) - 1;
		String rotationText = getResources().getStringArray(R.array.rotationTexts)[rotationIndex];

		// concatenate solution steps into single string, highlighting current step
		StringBuilder solution = new StringBuilder();
		for (int i = 0; i < solutions.length; i++) {
			if (currentStep == i) {
				solution.append("<b><i>" + solutions[i] + "</i></b>");
			} else {
				solution.append(solutions[i]);
			}
			solution.append(" ");
		}
		solution.append("<br><br>");
		solution.append("Rotate ");
		solution.append(solutions[currentStep].charAt(0));
		solution.append(" " + rotationText);

		solutionText.setText(Html.fromHtml(solution.toString()));

		// set animation
		switch (solutions[currentStep].charAt(1)) {
		case '1':
			imageView.setAnimation(rotate90cw);
			break;
		case '2':
			imageView.setAnimation(rotate180);
			break;
		case '3':
			imageView.setAnimation(rotate90ccw);
			break;
		default:
			imageView.setAnimation(null);
		}

	}

	// show cube faces
	private void drawCurrentFace() {
		for (int i=0; i < rectangles.size(); i++) {
			Paint paint = new Paint();
			paint.setStyle(Paint.Style.FILL_AND_STROKE);
			paint.setStrokeWidth(5);
			paint.setColor(ColorConverter.getAndroidColor(currentFacelets[i].getColor()));
			canvas.drawRect(rects.get(i), paint);
		}

	}

	// show rotation arrow
	// show step description


}
