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
    	if (squares.isEmpty()) {
    		calculateSquareCoordinates(width, height);
    	}
    	positionViews();
    	
    }

    public void onCameraViewStopped() {
    }

    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
    	frame = inputFrame.rgba();
    	
    	drawSquareDetectionRectangles();
    	drawViewBackground();
    	
    	// Flip frame to revert mirrored front camera image
    	Core.flip(frame, frame, 1);
        return frame;
    }

    
    
    private void drawSquareDetectionRectangles() {
    	// draw nine squares
    	for (HashMap<String, Point> square : squares) {
    		Core.rectangle(frame, square.get("tl"), square.get("br"), RECTCOLOR, 2);
    	}
    }
 
    private void drawViewBackground() {
    	// draw solid rect as background for text/button on right side of layout 
    	Core.rectangle(frame, new Point(width-xOffset+padding/2, 0), 
    			new Point(0, height), BGCOLOR, -1);
    }
    
    private Point movePointDiagonally(Point a, Point b) {
    	return new Point(a.x + b.x, a.y + b.y);
    }
    
    private Point movePointHorizontally(Point a, Point b) {
    	return new Point(a.x + b.x, a.y);
    }
    
    private Point movePointVertically(Point a, Point b) {
    	return new Point(a.x, a.y + b.y);
    }
    
    private void calculateSquareCoordinates(int width, int height) {
    	// Set coordinates of SquareDetectionRectangles
    	
    	// Creates ListArray of HashMaps of square coordinates. 
        // Each HashMap stands for a square and has two keys: 
        // "tl" for top-left coordinates (Point)
        // "br" for bottom-right coordinates of square (Point)
    	
    	// Beware: because front camera output is y-mirrored,
    	// point of origin is on top left corner and some values 
    	// have to be negated for correct calculating

    	// Square positions in ListArray:
    	// |0|1|2|
    	// |3|4|5|
    	// |6|7|8|
    	
    	
    	// spacing between squares
    	int squareSpacing = 7*height/100;
    	
    	// margin around face
    	int faceMargin = 10*height/100;
    	
    	// height/width of single square
    	int squareSize = (height - 2*squareSpacing - 2*faceMargin)/3;
    	
    	// vector from tl to br of square
    	Point squareDiagonal = new Point(-squareSize, squareSize);
    	
    	// vector from tl of square to tl of square on the right
    	Point squareDistance = new Point(-(squareSize + squareSpacing), 
    			squareSize + squareSpacing);
    	
    	// Initial point: top left corner of square 0
    	Point topLeft = new Point(width - faceMargin, faceMargin);
    	
    	// Values needed by positionViews()
    	padding = faceMargin;
    	xOffset = 2*faceMargin + 3*squareSize + 2*squareSpacing;
    	
    	// Create nine squares, three in each row
    	for (int row=0; row<3; row++) {
    		HashMap<String, Point> zero = new HashMap<String, Point>();
    		zero.put("tl", topLeft);
    		zero.put("br", movePointDiagonally(zero.get("tl"), squareDiagonal));
    		squares.add(zero);

    		HashMap<String, Point> one = new HashMap<String, Point>();
    		one.put("tl", movePointHorizontally(topLeft, squareDistance));
    		one.put("br", movePointDiagonally(one.get("tl"), squareDiagonal));
    		squares.add(one);

    		HashMap<String, Point> two = new HashMap<String, Point>();
    		two.put("tl", movePointHorizontally(one.get("tl"), squareDistance));
    		two.put("br", movePointDiagonally(two.get("tl"), squareDiagonal));
    		squares.add(two);

    		// move to next row
    		topLeft = movePointVertically(topLeft, squareDistance);
    	}
    }
 
    private void positionViews() {
    	// Position views according to calculated size of squares
    	
    	LinearLayout layout = (LinearLayout) findViewById(R.id.linearView);
    	layout.setPadding(xOffset, padding, padding, padding);
    	
    	TextView text = (TextView) findViewById(R.id.instructionContentText);
    	text.setMaxWidth(width-padding-xOffset);
    	text.setMinWidth(width-padding-xOffset);
    }
    
}
