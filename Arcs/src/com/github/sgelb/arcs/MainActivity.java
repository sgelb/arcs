package com.github.sgelb.arcs;

import java.util.ArrayList;
import java.util.HashMap;

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
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements CvCameraViewListener2 {

	private static final String TAG = "ARCS::MainActivity";
	private static final Scalar RECTCOLOR = new Scalar(200, 200, 200);
	private static final Scalar BGCOLOR = new Scalar(70, 70, 70); 
	private Mat frame;
    private CameraBridgeViewBase mOpenCvCameraView;
    
    private int padding;
    private int xOffset;
    private int width;
    private int height;
    
    // Array holding coordinates of square overlays
    private ArrayList<HashMap<String, Point>> squares;

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
        squares = new ArrayList<HashMap<String,Point>>();
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

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
    	calculateSquareCoordinates(width, height);
    	positionViews();
    	
    }

    public void onCameraViewStopped() {
    }

    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
    	frame = inputFrame.rgba();
    	
    	paintSquareDetectionRectangles();
    	
    	// Flip frame to revert mirrored front camera image
    	Core.flip(frame, frame, 1);
        return frame;
    }
    
    private Point pointSubtraction(Point a, Point b) {
    	return new Point(a.x - b.x, a.y + b.y);
    }
    
    private void calculateSquareCoordinates(int width, int height) {
    	// Set coordinates of SquareDetectionRectangles
    	
    	// Beware: because front camera output is y-mirrored,
    	// point of origin is on top left corner. 
    	// 
    	// |0|1|2|
    	// |3|4|5|
    	// |6|7|8|
    	
    	// spacing between sqaures to % of height
    	int squareSpacing = 5*height/100;
    	// margin around face
    	int faceMargin = 10*height/100;
    	// height/width of single square
    	int squareSize = (height - 2*squareSpacing - 2*faceMargin)/3;
    	Point squareDiagonal = new Point(squareSize, squareSize);
    	
    	HashMap<String, Point> zero = new HashMap<String, Point>();
    	zero.put("tl", new Point(width - faceMargin, faceMargin));
    	zero.put("br", pointSubtraction(zero.get("tl"), squareDiagonal));
    	squares.add(zero);
    	
    	HashMap<String, Point> one = new HashMap<String, Point>();
    	one.put("tl", new Point(width - (faceMargin + squareSize + squareSpacing), faceMargin));
    	one.put("br", pointSubtraction(one.get("tl"), squareDiagonal));
    	squares.add(one);

    	HashMap<String, Point> two = new HashMap<String, Point>();
    	two.put("tl", new Point(width - (faceMargin + 2*(squareSize  + squareSpacing)), faceMargin));
    	two.put("br", pointSubtraction(two.get("tl"), squareDiagonal));
    	squares.add(two);
    	
    	HashMap<String, Point> three = new HashMap<String, Point>();
    	three.put("tl", new Point(width - faceMargin, faceMargin + squareSize + squareSpacing));
    	three.put("br", pointSubtraction(three.get("tl"), squareDiagonal));
    	squares.add(three);
    	
    	HashMap<String, Point> four = new HashMap<String, Point>();
    	four.put("tl", new Point(width - (faceMargin + squareSize + squareSpacing), faceMargin + squareSize + squareSpacing));
    	four.put("br", pointSubtraction(four.get("tl"), squareDiagonal));
    	squares.add(four);

    	HashMap<String, Point> five = new HashMap<String, Point>();
    	five.put("tl", new Point(width - (faceMargin + 2*(squareSize  + squareSpacing)), faceMargin + squareSize + squareSpacing));
    	five.put("br", pointSubtraction(five.get("tl"), squareDiagonal));
    	squares.add(five);
    	
    	HashMap<String, Point> six = new HashMap<String, Point>();
    	six.put("tl", new Point(width - faceMargin, faceMargin + 2*squareSize + 2*squareSpacing));
    	six.put("br", pointSubtraction(six.get("tl"), squareDiagonal));
    	squares.add(six);
    	
    	HashMap<String, Point> seven = new HashMap<String, Point>();
    	seven.put("tl", new Point(width - (faceMargin + squareSize + squareSpacing), faceMargin + 2*squareSize + 2*squareSpacing));
    	seven.put("br", pointSubtraction(seven.get("tl"), squareDiagonal));
    	squares.add(seven);

    	HashMap<String, Point> eight = new HashMap<String, Point>();
    	eight.put("tl", new Point(width - (faceMargin + 2*(squareSize  + squareSpacing)), faceMargin + 2*squareSize + 2*squareSpacing));
    	eight.put("br", pointSubtraction(eight.get("tl"), squareDiagonal));
    	squares.add(eight);
    }
    
    private void paintSquareDetectionRectangles() {
    	for (HashMap<String, Point> square : squares) {
    		Core.rectangle(frame, square.get("tl"), square.get("br"), RECTCOLOR, 5);
    	}
    	
    }
    
}
