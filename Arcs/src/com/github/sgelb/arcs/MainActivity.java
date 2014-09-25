package com.github.sgelb.arcs;

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
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements CvCameraViewListener2 {

	private static final String TAG = "ARCS::MainActivity";

	private static final Scalar BGCOLOR = new Scalar(70, 70, 70);

	private Mat frame;
	private CameraBridgeViewBase mOpenCvCameraView;
	private CubeInputView inputView = null;

	private int width;
	private int height;
	private static Context context;

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
		this.inputView = new ManualCubeInputView();
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
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return true;
	}

	public void onCameraViewStarted(int width, int height) {
		this.width = width;
		this.height = height;
		// TODO: load inputView from Settings
		inputView.init(width, height);
		positionViews();
	}

	public void onCameraViewStopped() {
	}

	public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
		frame = inputFrame.rgba();

		inputView.drawOverlay(frame);
		drawViewBackground();

		// Flip frame to revert mirrored front camera image
		Core.flip(frame, frame, 1);
		return frame;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return inputView.onTouchEvent(event);
	}

	private void drawViewBackground() {
		// draw solid rect as background for text/button on right side of layout 
		Core.rectangle(frame, new Point(width - inputView.getXOffset() + inputView.getPadding()/2, 0), 
				new Point(0, height), BGCOLOR, -1);
	}


	private void positionViews() {
		// Position views according to calculated size of rectangles
		int xOffset = inputView.getXOffset();
		int padding = inputView.getPadding();

		LinearLayout layout = (LinearLayout) findViewById(R.id.linearView);
		layout.setPadding(xOffset, padding, padding, padding);

		TextView text = (TextView) findViewById(R.id.instructionContentText);
		text.setMaxWidth(width - padding - xOffset);
		text.setMinWidth(width - padding - xOffset);
	}

}
