package com.github.sgelb.arcs.solution;

import java.util.ArrayList;

import org.opencv.core.Rect;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.sgelb.arcs.MainActivity;
import com.github.sgelb.arcs.R;
import com.github.sgelb.arcs.cube.ColorConverter;
import com.github.sgelb.arcs.cube.LayoutCalculator;
import com.github.sgelb.arcs.cube.RubiksCube;

public class SolutionActivity extends Activity {

	private RubiksCube cube;
	private String solution;
	private SolutionDisplay solver;
	private int width;
	private int height;
	private int xOffset;
	private int padding;
	private ArrayList<Rect> rectangles;

	public SolutionActivity() {
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

		TextView solutionTitle = (TextView) findViewById(R.id.solutionTitle);
		TextView solutionText = (TextView) findViewById(R.id.solutionText);
		Button prevBtn = (Button) findViewById(R.id.solPrevBtn);
		Button fwdBtn = (Button) findViewById(R.id.solFwdBtn);

		// add clicklistener auf fwd/prev-Buttons
		Bitmap bitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		imageView.setImageBitmap(bitmap);


		// Rectangles
		LayoutCalculator lc = new LayoutCalculator(width, height);
		rectangles = lc.calculateRectanglesCoordinates();
		xOffset = lc.getXOffset();
		padding = lc.getPadding();

	    Paint paint = new Paint();
	    paint.setStyle(Paint.Style.FILL_AND_STROKE);
	    paint.setStrokeWidth(10);

	    for (int i=0; i < rectangles.size(); i++) {
	    	paint.setColor(ColorConverter.getAndroidColor(cube.getFaceletColors()[i]));
	    	canvas.drawRect(
	    			(float) rectangles.get(i).tl().x,
	    			(float) rectangles.get(i).tl().y,
	    			(float) rectangles.get(i).br().x,
	    			(float) rectangles.get(i).br().y,
	    			paint);
	    }
	    // present 1st solution step

	}



	// show cube faces
	// show rotation arrow
	// show step description


}
