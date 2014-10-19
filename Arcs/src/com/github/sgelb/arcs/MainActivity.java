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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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
	private Button nextBtn;
	
	private RubiksCube cube;
	private Integer currentFace;

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
		
		nextBtn = (Button) findViewById(R.id.nextBtn);
		nextBtn.setEnabled(false);
		nextBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				currentFace++;
				instructionTitle.setText(faceInputMethod.getInstructionTitle(currentFace));
				instructionContent.setText(faceInputMethod.getInstructionText(currentFace));
				faceInputMethod.nextFace(currentFace);
				nextBtn.setEnabled(false);
			}
		});

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
		faceInputMethod.init(width, height);
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
				nextBtn.setEnabled(true);
			}
		}
	}

}
