package com.github.sgelb.arcs;

import java.util.ArrayList;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;

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
    
    // Array holding coordinates of rectangle overlays
    private ArrayList<Rect> rectangles;

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
        this.rectangles = new ArrayList<Rect>(9);
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
    	if (rectangles.isEmpty()) {
    		rectangles = calculateRectanglesCoordinates(width, height);
    	}
    	positionViews();
    	
    }

    public void onCameraViewStopped() {
    }

    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
    	frame = inputFrame.rgba();
    	
    	drawDetectionRectangles();
    	drawViewBackground();
    	
    	// Flip frame to revert mirrored front camera image
    	Core.flip(frame, frame, 1);
        return frame;
    }

    
    
    private void drawDetectionRectangles() {
    	// draw nine rectangles
    	for (Rect rect : rectangles) {
    		Core.rectangle(frame, rect.tl(), rect.br(), RECTCOLOR, 2);
    	}
    }
 
    private void drawViewBackground() {
    	// draw solid rect as background for text/button on right side of layout 
    	Core.rectangle(frame, new Point(width-xOffset+padding/2, 0), 
    			new Point(0, height), BGCOLOR, -1);
    }
    
    
    public Point movePointHorizontally(Point a, Point b, int factor) {
    	// beware: due to y-mirrored output, we have to substract x
    	return new Point(a.x - factor*b.x, a.y);
    }
    
    public Rect getNextRectInRow(Point tl, Point br, Point dist, int factor) {
    	// calculate next rectangle in row 
    	return new Rect(
				movePointHorizontally(tl, dist, factor),
				movePointHorizontally(br, dist, factor));
    }
    
    public Point movePointVertically(Point a, Point b) {
    	return new Point(a.x, a.y + b.y);
    }
    
    public ArrayList<Rect> calculateRectanglesCoordinates(int width, int height) {
    	// Creates ListArray of cv::rects to draw overlay
    	
    	// Beware: because front camera output is y-mirrored,
    	// point of origin is on top left corner and some values 
    	// have to be negated/subtracted for correct calculating

    	// Rectangle positions in ListArray:
    	// |0|1|2|
    	// |3|4|5|
    	// |6|7|8|
    	
    	ArrayList<Rect> tmpRect = new ArrayList<Rect>(9);
    	
    	// spacing between rects
    	int rectSpacing = 7*height/100;
    	
    	// margin around face
    	int faceMargin = 10*height/100;
    	
    	// Size of single rect
    	Size rectSize = new Size(
    			(height - 2*rectSpacing - 2*faceMargin)/3,
    			(height - 2*rectSpacing - 2*faceMargin)/3); 
    	
    	// vector from tl of rect to tl of rect on the right
    	Point rectDistance = new Point(rectSize.width + rectSpacing, 
    			(double) rectSize.height + rectSpacing);
    	
    	// Initial points: top left/bottom right corners of first rectangle
    	Point topLeft = new Point(width - faceMargin, faceMargin);
    	Point bottomRight = new Point(width - faceMargin - rectSize.width,
    			faceMargin + rectSize.height);
    	
    	
    	// Values needed by positionViews()
    	padding = faceMargin;
    	xOffset = (int) (2*faceMargin + 3*rectSize.width + 2*rectSpacing);

    	// Create nine rectangles, three in each row
    	for (int row=0; row < 3; row++) {
    		for (int col=0; col < 3; col++) {
    			tmpRect.add(getNextRectInRow(topLeft, bottomRight, rectDistance, col));
    		}
    		// move to next row
    		topLeft = movePointVertically(topLeft, rectDistance);
    		bottomRight = movePointVertically(bottomRight, rectDistance);
    	}
    	return tmpRect;
    }
 
    private void positionViews() {
    	// Position views according to calculated size of rectangles
    	
    	LinearLayout layout = (LinearLayout) findViewById(R.id.linearView);
    	layout.setPadding(xOffset, padding, padding, padding);
    	
    	TextView text = (TextView) findViewById(R.id.instructionContentText);
    	text.setMaxWidth(width - padding - xOffset);
    	text.setMinWidth(width - padding - xOffset);
    }
    
}
