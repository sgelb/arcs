package com.github.sgelb.arcs;

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
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements CvCameraViewListener2, Observer {

	private static final String TAG = "ARCS::MainActivity";

	private static final Scalar BGCOLOR = new Scalar(70, 70, 70);

	private Mat frame;
	private CameraBridgeViewBase mOpenCvCameraView;
	private CubeInputMethod cubeInputMethod = null;
	private SharedPreferences prefs;

	private int width;
	private int height;
	private static Context context;
	private TextView instructionContent;
	private TextView instructionTitle;

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
		this.cubeInputMethod = new ManualCubeInputMethod(this);
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
		instructionContent = (TextView) findViewById(R.id.instructionContentText);
		instructionTitle = (TextView) findViewById(R.id.instructionTitleText);
		instructionTitle.setText(cubeInputMethod.getInstructionTitle(0));
		instructionContent.setText(cubeInputMethod.getInstructionText(0));

		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		setCubeInputMethod();
		
		mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.java_camera_view);
		mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
		// Use front camera
		mOpenCvCameraView.setCameraIndex(1);
		mOpenCvCameraView.setCvCameraViewListener(this);
	}

	@Override
	public void onPause()
	{
		super.onPause();
		if (mOpenCvCameraView != null)
			mOpenCvCameraView.disableView();
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
		cubeInputMethod.init(width, height);
		positionViews();
	}

	public void onCameraViewStopped() {
	}

	public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
		frame = inputFrame.rgba();

		cubeInputMethod.drawOverlay(frame);
		drawViewBackground();

		// Flip frame to revert mirrored front camera image
		Core.flip(frame, frame, 1);
		return frame;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return cubeInputMethod.onTouchEvent(event);
	}

	private void drawViewBackground() {
		// draw solid rect as background for text/button on right side of layout 
		Core.rectangle(frame, new Point(width - cubeInputMethod.getXOffset() + cubeInputMethod.getPadding()/2, 0), 
				new Point(0, height), BGCOLOR, -1);
	}


	private void positionViews() {
		// Position views according to calculated size of rectangles
		int xOffset = cubeInputMethod.getXOffset();
		int padding = cubeInputMethod.getPadding();

		LinearLayout layout = (LinearLayout) findViewById(R.id.linearView);
		layout.setPadding(xOffset, padding, padding, padding);

		instructionContent.setMaxWidth(width - padding - xOffset);
		instructionContent.setMinWidth(width - padding - xOffset);
	}
	
	private void setCubeInputMethod() {
		switch (prefs.getString("cube_input_method", "manual")) {
		case "manual":			
			this.cubeInputMethod = new ManualCubeInputMethod(this);
			break;
		default:
			this.cubeInputMethod = new ManualCubeInputMethod(this);
		}
	}

	@Override
	public void update(Observable observable, Object data) {
		if (observable instanceof ManualCubeInputMethod) {
			if (data instanceof Integer) {
				// nextBtn clicked. data contains next face, update text
				Integer faceId = (Integer) data;
				instructionTitle.setText(cubeInputMethod.getInstructionTitle(faceId));
				instructionContent.setText(cubeInputMethod.getInstructionText(faceId));

			} else if (data instanceof Square[]) {
				// got all-set squares from CubeInputMethod, start solving
				instructionTitle.setText("Done!");
				instructionContent.setVisibility(View.INVISIBLE);
			}
		}
	}

}
