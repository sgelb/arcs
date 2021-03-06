package com.github.sgelb.arcs;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sgelb.arcs.cube.LayoutCalculator;
import com.github.sgelb.arcs.cube.Rotator;
import com.github.sgelb.arcs.cube.RubiksCube;
import com.github.sgelb.arcs.input.FaceInputMethod;
import com.github.sgelb.arcs.input.ManualFaceInputMethod;
import com.github.sgelb.arcs.solution.SolutionActivity;

import cs.min2phase.Search;

public class MainActivity extends Activity implements CvCameraViewListener2, Observer {

	private static final String TAG = "ARCS::MainActivity";
	private static final String STATE_FACELETS = "cubeFacelets";

	private static final Scalar BGCOLOR = new Scalar(70, 70, 70);

	public static final String CUBE ="com.github.sgelb.arcs.CUBE";
	public static final String SOLUTION ="com.github.sgelb.arcs.SOLUTION";
	public static final String WIDTH ="com.github.sgelb.arcs.WIDTH";
	public static final String HEIGHT ="com.github.sgelb.arcs.HEIGHT";

	private Mat frame;
	private CameraBridgeViewBase mOpenCvCameraView;
	private FaceInputMethod faceInputMethod = null;
	private SharedPreferences prefs;

	private int width;
	private int height;
	private static Context context;
	private TextView instructionContent;
	private TextView instructionTitle;
	private ImageButton forwardBtn;
	private ImageButton backBtn;
	private ImageButton cameraBtn;

	private RubiksCube cube;
	private Integer currentFace;
	private CubeSolver cubeSolver;
	private int padding;
	private int xOffset;

	public MainActivity() {
		Log.i(TAG, "Instantiated new " + this.getClass());
		faceInputMethod = new ManualFaceInputMethod(this);
	}

	/* Called when activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		MainActivity.context = getApplicationContext();
		setContentView(R.layout.main_activity);

		cube = new RubiksCube();
		// Check if we're recreating a previously destroyed instance
		if (savedInstanceState != null) {
			// Restore cube from saved state
			int[] colors = savedInstanceState.getIntArray(STATE_FACELETS);
			cube.setFaceletColors(colors);
			Log.d(TAG, "Restored state");
		}
		currentFace = Rotator.FRONT;

		instructionTitle = (TextView) findViewById(R.id.instructionTitleText);
		instructionTitle.setText(faceInputMethod.getInstructionTitle(0));

		instructionContent = (TextView) findViewById(R.id.instructionContentText);
		instructionContent.setText(faceInputMethod.getInstructionText(0));

		forwardBtn = (ImageButton) findViewById(R.id.forwardBtn);
		disableButton(forwardBtn);
		forwardBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (currentFace == 5 && !cube.hasUnsetFacelets()) {
					solveCubeAction();
				} else {
					forwardBtnAction();
				}
			}
		});

		backBtn = (ImageButton) findViewById(R.id.backBtn);
		disableButton(backBtn);
		backBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				previousBtnAction();
			}
		});

		cameraBtn = (ImageButton) findViewById(R.id.cameraBtn);
		cameraBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cameraBtnAction();
			}
		});

		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		setCubeInputMethod();

		mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.java_camera_view);
		mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
		// Use back camera
		mOpenCvCameraView.setCameraIndex(0);
		mOpenCvCameraView.setCvCameraViewListener(this);
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		// Save the current cube state
		savedInstanceState.putIntArray(STATE_FACELETS, cube.getFaceletColors());
		super.onSaveInstanceState(savedInstanceState);
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mOpenCvCameraView != null) {
			mOpenCvCameraView.disableView();
		}
		if (cubeSolver != null && !cubeSolver.isCancelled()) {
			cubeSolver.cancel(true);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_9,
				this, mLoaderCallback);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mOpenCvCameraView != null) {
			mOpenCvCameraView.disableView();
		}
		if (cubeSolver != null && !cubeSolver.isCancelled()) {
			cubeSolver.cancel(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == R.id.create_random_cube) {
			cube.randomize();
			resetFaceView();
			return true;
		} else if (itemId == R.id.clear_cube) {
			cube.clear();
			resetFaceView();
			return true;
		} else if (itemId == R.id.about) {
			Intent aboutIntent = new Intent(this, AboutActivity.class);
			startActivity(aboutIntent);
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return faceInputMethod.onTouchEvent(event);
	}

	public static Context getContext() {
		return MainActivity.context;
	}

	private void setCubeInputMethod() {
		switch (prefs.getString("cube_input_method", "manual")) {
		case "manual":
			faceInputMethod = new ManualFaceInputMethod(this);
			break;
		default:
			faceInputMethod = new ManualFaceInputMethod(this);
		}
	}

	@Override
	public void onCameraViewStarted(int width, int height) {
		this.width = width;
		this.height = height;
		LayoutCalculator lc = new LayoutCalculator(width, height);
		faceInputMethod.init(lc.calculateRectanglesCoordinates(), cube.getFaceColor(currentFace));
		xOffset = lc.getXOffset();
		padding = lc.getPadding();
		positionViews();
	}


	@Override
	public void onCameraViewStopped() {
	}

	@Override
	public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
		frame = inputFrame.rgba();
		faceInputMethod.drawOverlay(frame);
		drawViewBackground();
		return frame;
	}

	private void drawViewBackground() {
		// draw solid rect as background for text/button on right side of layout
		Core.rectangle(frame, new Point(xOffset - padding/2, 0), new Point(width, height), BGCOLOR, -1);
	}

	private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
		@Override
		public void onManagerConnected(int status) {
			switch (status) {
			case LoaderCallbackInterface.SUCCESS:
			{
				Log.i(TAG, "OpenCV loaded successfully");
				mOpenCvCameraView.enableView();
			} break;
			default:
			{
				super.onManagerConnected(status);
			} break;
			}
		}
	};


	private void resetFaceView() {
		// reset after creating random cube or cleared cube
		currentFace = Rotator.FRONT;
		faceInputMethod.changeFace(currentFace, cube.getFaceColor(currentFace));
		instructionTitle.setText(faceInputMethod.getInstructionTitle(currentFace));
		instructionContent.setText(faceInputMethod.getInstructionText(currentFace));
		forwardBtn.setImageResource(R.drawable.ic_action_forward);
		if (cube.hasUnsetFacelets()) {
			disableButton(forwardBtn);
		} else {
			enableButton(forwardBtn);
		}
		disableButton(backBtn);
	}

	private void positionViews() {
		// Position views according to calculated size of rectangles
		LinearLayout layout = (LinearLayout) findViewById(R.id.linearView);
		layout.setPadding(xOffset, padding, padding, padding);

		instructionContent.setMaxWidth(width - padding - xOffset);
		instructionContent.setMinWidth(width - padding - xOffset);
	}

	private void forwardBtnAction() {
		currentFace++;
		instructionTitle.setText(faceInputMethod.getInstructionTitle(currentFace));
		instructionContent.setText(faceInputMethod.getInstructionText(currentFace));
		ArrayList<Integer> face = cube.getFaceColor(currentFace);
		faceInputMethod.changeFace(currentFace, face);
		enableButton(backBtn);

		if (face == null || faceInputMethod.currentFaceHasUnsetFacelets()) {
			disableButton(forwardBtn);
		} else {
			enableButton(forwardBtn);
		}

		if (currentFace == 5) {
			forwardBtn.setImageResource(R.drawable.ic_launcher);
			if (cube.hasUnsetFacelets()) {
				disableButton(forwardBtn);
			}
		}
	}

	private void previousBtnAction() {
		currentFace--;
		instructionTitle.setText(faceInputMethod.getInstructionTitle(currentFace));
		instructionContent.setText(faceInputMethod.getInstructionText(currentFace));
		faceInputMethod.changeFace(currentFace, cube.getFaceColor(currentFace));
		forwardBtn.setImageResource(R.drawable.ic_action_forward);
		enableButton(forwardBtn);
		if (currentFace == 0) {
			disableButton(backBtn);
		}
	}

	private void cameraBtnAction() {
		faceInputMethod.startDetectingColors();
	}

	private void solveCubeAction() {
		// Solve
		cubeSolver= new CubeSolver();
		cubeSolver.execute(cube.getSingmasterNotation());
	}

	@Override
	public void update(Observable observable, Object data) {
		if (observable instanceof ManualFaceInputMethod) {
			// got face
			if (data instanceof ArrayList<?>) {
				// checked cast for type safety
				ArrayList<Integer> face = new ArrayList<Integer>();
				for (int i=0; i<((ArrayList<?>) data).size(); i++) {
					Object item = ((ArrayList<?>) data).get(i);
					if (item instanceof Integer) {
						face.add((Integer) item);
					}
				}
				// set face
				cube.setFaceColor(currentFace, face);
				enableButton(forwardBtn);
			}
		}
	}

	private void disableButton(ImageButton button) {
		button.setEnabled(false);
		button.setImageAlpha(100);
	}

	private void enableButton(ImageButton button) {
		button.setEnabled(true);
		button.setImageAlpha(255);
	}

	private class CubeSolver extends AsyncTask<String, Integer, String> {
		long startTime;

		@Override
		protected void onPreExecute() {
			disableButton(forwardBtn);
			startTime = System.currentTimeMillis();
		}

		@Override
		protected String doInBackground(String... singmasterNotation) {
			Search search = new Search();
			String solution = search.solution(singmasterNotation[0], 21, 10000, 0, 0x0);
			return solution;
		}

		@Override
		protected void onPostExecute(String result) {
			enableButton(forwardBtn);
			long runTime = System.currentTimeMillis() - startTime;
			Log.d(TAG, "Found solution in " + runTime + "ms :" + result);
			processResult(result);
		}
	}

	private void processResult(String solution) {

		// error handling
		if (solution.startsWith("error")) {
			int errorcode = Character.getNumericValue(solution.charAt(solution.length() - 1)) - 1;
			solution = getResources().getStringArray(R.array.errors)[errorcode];
			Toast.makeText(this, solution, Toast.LENGTH_LONG).show();
			return;
		}

		// solution is empty aka cube already solved
		if (solution.isEmpty()) {
			Toast.makeText(this, getString(R.string.cube_is_solved), Toast.LENGTH_LONG).show();
			return;
		}

		// got valid solution, start SolutionActivity
		Intent solutionIntent = new Intent(this, SolutionActivity.class);
		solutionIntent.putExtra(CUBE, cube.getFaceletColors());
		solutionIntent.putExtra(SOLUTION, solution.toLowerCase());
		solutionIntent.putExtra(WIDTH, width);
		solutionIntent.putExtra(HEIGHT, height);
		startActivity(solutionIntent);
	}


}
