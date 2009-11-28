package com.zienkikk.android.grapher;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

// TODO - Maybe the renderer shouldn't be in charge of storing the points. 
// FIXME - This view is not lifecycle safe. It should only be ran as a single activity.

/**
 * A view that graphically represents one or more series contained in GraphDataSource objects. 
 * 
 * @author Krzysztof Zienkiewicz
 *
 */
public class GraphView extends SurfaceView implements SurfaceHolder.Callback {

	/**
	 * An enum that contains different scrolling modes:
	 * MANUAL 	- Scrolling will be handled by user's touch events
	 * SYNC		- The current frame will be shifted to always contains the greatest x coordinate
	 * 			of the data source specified via setScrollSync()
	 */
	public enum ScrollMode {MANUAL, SYNC, NONE};
	
	private static final String TAG = "GraphView";
	private GraphRenderer renderer_;
	private TouchScroller scroller_;
	
	public GraphView(Context context) {
		super(context);
	
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
		
		renderer_ = new GraphRenderer(holder);
		scroller_ = new TouchScroller(renderer_);
	}
	
	/**
	 * Registers the data source with the view and returns a unique identifier.
	 * 
	 * @param src
	 * @return
	 */
	public int addDataSource(GraphDataSource src) {
		return renderer_.addDataSource(src);
	}
	
	/**
	 * Should be called when activity dies. Kills the rendering thread.
	 */
	public void onDestroy() {
		renderer_.kill();
	}
	
	/**
	 * Should be called when activity pauses. Pauses the rendering thread.
	 */
	public void onPause() {
		renderer_.pause();
	}
	
	/**
	 * Should be called when activity resumes. Resumes the rendering thread.
	 */
	public void onResume() {
		renderer_.unpause();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// scroller_ will adjust viewport only if MANUAL scroll mode is set
		scroller_.onTouchEvent(event);

		return true;
	}
	
	/**
	 * Sets the scroll mode
	 * @param mode
	 */
	public void setScrollMode(ScrollMode mode) {
		renderer_.setScrollMode(mode);
	}

	/**
	 * Sets the id of data source to sync the scroll with
	 * @param synId
	 */
	public void setScrollSync(int syncId) {
		renderer_.setScrollSync(syncId);
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// Called after some sort of a configuration change, ie, an orientation change.
		Log.v(TAG, "surfaceChanged(" + width + "," + height +")");
		renderer_.setDimension(width, height);
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// Called when the surface is first created. To find out the dimension, wait for
		// surfaceChanged().
		Log.v(TAG, "surfaceCreated()");
		renderer_.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// Called immediately before the surface is destroyed. Do the necessary cleanup.
		Log.v(TAG, "surfaceDestroyed()");
		renderer_.kill();
	}

}