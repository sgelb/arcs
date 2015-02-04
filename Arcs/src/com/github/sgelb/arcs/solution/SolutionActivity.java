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
import android.view.View;
import android.widget.Button;
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


	private ArrayList<RectF> rects;

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

		solution = intent.getStringExtra(MainActivity.SOLUTION);
		width = intent.getIntExtra(MainActivity.WIDTH, 0);
		height = intent.getIntExtra(MainActivity.HEIGHT, 0);


		ImageView imageView = (ImageView) findViewById(R.id.imageView);
		imageView.getLayoutParams().width = height;

		solutionTitle = (TextView) findViewById(R.id.solutionTitle);
		solutionText = (TextView) findViewById(R.id.solutionText);

		// create canvas
		Bitmap bitmap = Bitmap.createBitmap(height, height, Bitmap.Config.ARGB_8888);
		canvas = new Canvas(bitmap);
		imageView.setImageBitmap(bitmap);

		// FIXME: correct calculation of previous rotation
		Button prevBtn = (Button) findViewById(R.id.solPrevBtn);
//		prevBtn.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if (currentStep > 0) {
//					currentStep--;
//					drawSolutionStep();
//				}
//			}
//		});

		Button forwardBtn = (Button) findViewById(R.id.solFwdBtn);
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
			rects.add(new RectF((float) rect.tl().x, (float) rect.tl().y,
					(float) rect.br().x, (float) rect.br().y));
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

		// FIXME: use R.strings
		solutionTitle.setText("Step " + (currentStep + 1) + " of " + totalSteps);
		solutionText.setText(
				solution + "\n\n"
				+ "Rotate "
				+ solutions[currentStep].charAt(0)
				+ " "
				+ 90*Character.getNumericValue(solutions[currentStep].charAt(1))
				+ "° clockwise.");
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
