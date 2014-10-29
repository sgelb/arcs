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
import cs.min2phase.Search;
import cs.min2phase.Tools;

public class MainActivity extends Activity implements CvCameraViewListener2, Observer {

	private static final String TAG = "ARCS::MainActivity";

	private static final Scalar BGCOLOR = new Scalar(70, 70, 70);

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
	
	private RubiksCube cube;
	private Integer currentFace;
	private CubeSolver cubeSolver;

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

	public MainActivity() {
		Log.i(TAG, "Instantiated new " + this.getClass());
		this.faceInputMethod = new ManualFaceInputMethod(this);
	}

	public static Context getContext() {
		return MainActivity.context;
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "called onCreate");
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		MainActivity.context = getApplicationContext();
		setContentView(R.layout.main_activity);

		cube = new RubiksCube();
		currentFace = Rotation.FRONT;
		
		instructionTitle = (TextView) findViewById(R.id.instructionTitleText);
		instructionTitle.setText(faceInputMethod.getInstructionTitle(0));
		
		instructionContent = (TextView) findViewById(R.id.instructionContentText);
		instructionContent.setText(faceInputMethod.getInstructionText(0));
		
		forwardBtn = (ImageButton) findViewById(R.id.forwardBtn);
		disableButton(forwardBtn);
		forwardBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (currentFace == 5 && !cube.hasUnsetSquares()) {
					solveCubeAction();
				} else {
					forwardFaceAction();
				}
			}
		});
		
		backBtn = (ImageButton) findViewById(R.id.backBtn);
		disableButton(backBtn);
		backBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				previousFaceAction();
			}
		});

		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		setCubeInputMethod();
		if (prefs.getBoolean("cube_init_method", false)) {
			initializeRandomCube();
			enableButton(forwardBtn);
		}
		
		mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.java_camera_view);
		mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
		// Use front camera
		mOpenCvCameraView.setCameraIndex(1);
		mOpenCvCameraView.setCvCameraViewListener(this);
	}

	private void initializeRandomCube() {
		Log.d(TAG, "Init random cube");
		String randomCube = Tools.randomCube();

		//URFDLB
		int index = 0;
		ArrayList<Integer> faceColors = new ArrayList<Integer>();
		for (int i=0; i<9; i++) {
			faceColors.add(SquareColor.fromSingmasterToColor(randomCube.charAt(index)));
			index++;
		}
		cube.setFaceColor(Rotation.UP, faceColors);
		faceColors = new ArrayList<Integer>();
		for (int i=0; i<9; i++) {
			faceColors.add(SquareColor.fromSingmasterToColor(randomCube.charAt(index)));
			index++;
		}
		cube.setFaceColor(Rotation.RIGHT, faceColors);
		faceColors = new ArrayList<Integer>();
		for (int i=0; i<9; i++) {
			faceColors.add(SquareColor.fromSingmasterToColor(randomCube.charAt(index)));
			index++;
		}
		cube.setFaceColor(Rotation.FRONT, faceColors);
		faceColors = new ArrayList<Integer>();
		for (int i=0; i<9; i++) {
			faceColors.add(SquareColor.fromSingmasterToColor(randomCube.charAt(index)));
			index++;
		}
		cube.setFaceColor(Rotation.DOWN, faceColors);
		faceColors = new ArrayList<Integer>();
		for (int i=0; i<9; i++) {
			faceColors.add(SquareColor.fromSingmasterToColor(randomCube.charAt(index)));
			index++;
		}
		cube.setFaceColor(Rotation.LEFT, faceColors);
		faceColors = new ArrayList<Integer>();
		for (int i=0; i<9; i++) {
			faceColors.add(SquareColor.fromSingmasterToColor(randomCube.charAt(index)));
			index++;
		}
		cube.setFaceColor(Rotation.BACK, faceColors);
	}

	@Override
	public void onPause()
	{
		super.onPause();
		if (mOpenCvCameraView != null)
			mOpenCvCameraView.disableView();
		if (cubeSolver != null && !cubeSolver.isCancelled()) {
			cubeSolver.cancel(true);
		}
	}

	@Override
	public void onResume()
	{
		super.onResume();
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_9, 
				this, mLoaderCallback);
	}

	public void onDestroy() {
		super.onDestroy();
		if (mOpenCvCameraView != null)
			mOpenCvCameraView.disableView();
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
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case R.id.action_settings:
			Intent settingsIntent = new Intent(this, SettingsActivity.class);
			startActivity(settingsIntent);
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void onCameraViewStarted(int width, int height) {
		this.width = width;
		this.height = height;
		faceInputMethod.init(width, height, cube.getFaceColor(currentFace));
		positionViews();
	}

	public void onCameraViewStopped() {
	}

	public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
		frame = inputFrame.rgba();

		faceInputMethod.drawOverlay(frame);
		drawViewBackground();

		// Flip frame to revert mirrored front camera image
		Core.flip(frame, frame, 1);
		return frame;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return faceInputMethod.onTouchEvent(event);
	}

	private void drawViewBackground() {
		// draw solid rect as background for text/button on right side of layout 
		Core.rectangle(frame, new Point(width - faceInputMethod.getXOffset() + faceInputMethod.getPadding()/2, 0), 
				new Point(0, height), BGCOLOR, -1);
	}

	private void positionViews() {
		// Position views according to calculated size of rectangles
		int xOffset = faceInputMethod.getXOffset();
		int padding = faceInputMethod.getPadding();

		LinearLayout layout = (LinearLayout) findViewById(R.id.linearView);
		layout.setPadding(xOffset, padding, padding, padding);

		instructionContent.setMaxWidth(width - padding - xOffset);
		instructionContent.setMinWidth(width - padding - xOffset);
	}
	
	private void setCubeInputMethod() {
		switch (prefs.getString("cube_input_method", "manual")) {
		case "manual":			
			this.faceInputMethod = new ManualFaceInputMethod(this);
			break;
		default:
			this.faceInputMethod = new ManualFaceInputMethod(this);
		}
	}

	private void forwardFaceAction() {
		currentFace++;
		instructionTitle.setText(faceInputMethod.getInstructionTitle(currentFace));
		instructionContent.setText(faceInputMethod.getInstructionText(currentFace));
		ArrayList<Integer> face = cube.getFaceColor(currentFace);
		faceInputMethod.changeFace(currentFace, face);
		enableButton(backBtn);
		
		if (face == null || faceInputMethod.currentFaceHasUnsetSquares()) {
			disableButton(forwardBtn);
		} else {
			enableButton(forwardBtn);
		}
		
		if (currentFace == 5) {
			forwardBtn.setImageResource(R.drawable.ic_launcher);
			if (cube.hasUnsetSquares()) {
				disableButton(forwardBtn);
				}
		}
	}
	
	private void previousFaceAction() {
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
			startTime = System.currentTimeMillis();
		}

		@Override
		protected String doInBackground(String... singmasterNotation) {
			Search search = new Search();
			String solution = search.solution(singmasterNotation[0], 21, 60, 0, 0);
			return solution;
		}

		@Override
	    protected void onPostExecute(String result) {
			Log.d(TAG, "SOLUTION1: " + result);
			long runTime = System.currentTimeMillis() - startTime;
			Log.d(TAG, "RUNTIME: " + runTime);
			processResult(result);
	    }

	}

	private void processResult(String result) {
		String errorMsg = null;
		if (result.startsWith("Error 1")) {
			errorMsg = "There is not exactly one facelet of each colour";
		} else if (result.startsWith("Error 2")) {
			errorMsg = "Not all 12 edges exist exactly once";
		} else if (result.startsWith("Error 3")) {
			errorMsg = "Flip error: One edge has to be flipped";
		} else if (result.startsWith("Error 4")) {
			errorMsg = "Not all corners exist exactly once";
		} else if (result.startsWith("Error 5")) {
			errorMsg = "Twist error: One corner has to be twisted";
		} else if (result.startsWith("Error 6")) {
			errorMsg = "Parity error: Two corners or two edges have to be exchanged";
		} else if (result.startsWith("Error 7")) {
			errorMsg = "No solution exists for the given maxDepth";
		} else if (result.startsWith("Error 8")) {
			errorMsg = "Probe limit exceeded, no solution within given probMax";
		}
		if (errorMsg != null) {
			Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
		}
	}
	
}
