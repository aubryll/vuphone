package org.vuphone.grapher.android;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.vuphone.grapher.android.GraphView.ScrollMode;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.util.Log;
import android.view.SurfaceHolder;




/**
 * A thread that will render the GraphView based on the data provided to it and the configurations
 * used. Multiple data series may be graphed as long as each is encapsulated via a GraphDataSource
 * object. Each series may also provide a GraphDrawConfig object to specify how to draw its data.
 * 
 * @author Krzysztof Zienkiewicz
 *
 */
public class GraphRenderer extends Thread {
	private static final String TAG = "GraphRenderer";
	private static final long DELAY = 100;	// The sleep duration between frames.
	private static final long PAUSE_SLEEP = 100;	// Sleep duration for pause.
	private static final boolean SHOW_TOUCH_ZONES = false;
	private static final int TOUCH_BORDER_AXES = 20;
	private static final int TOUCH_BORDER_AREA = 20;
	
	private GraphConfig config_;	// The configuration object used to render the graph. All the
	// drawing code will synchronize on this object.
	
	private int viewWidth_;		// The dimensions of the view that we will be rendering. These will
	private int viewHeight_;	// not be provided until after the surface is created. These will
	// change throughout the execution.
	
	private float scaleX_;		// Screen pixels per one graph unit
	private float scaleY_;
	
	private Paint paint_;	// The paint used for drawing. Its state will not stay constant between
	// different draw calls.
	
	private SurfaceHolder holder_;	// The object that holds our surface.
	
	private boolean isAlive_;	// Life variable.
	private boolean isPaused_;	// Life variable.
	
	private HashMap<Integer, GraphDataSource> dataMap_;	// Map holding our data sources.
	private int lastMappedId_ = 0;	// The last unique id given out
	
	private ScrollMode scrollMode_;
	private int scrollSync_;
	
	public GraphRenderer(SurfaceHolder holder) {
		super(TAG);
		
		config_ = new GraphConfig();	// Use the default configurations
		viewWidth_ = viewHeight_ = 0;	// Unknown view dimensions
		
		scaleX_ = scaleY_ = 0;
		
		paint_ = new Paint();
		
		holder_ = holder;
		
		isAlive_ = true;	// Go ahead and run after a call to start()
		isPaused_ = true;	// Wait until view's onResume() is called to start
		
		dataMap_ = new HashMap<Integer, GraphDataSource>();
		
		scrollMode_ = ScrollMode.NONE;
		scrollSync_ = -1;
	}
	
	/**
	 * Registers the data source with the renderer and returns a unique identifier.
	 * 
	 * @param src
	 * @return
	 */
	public int addDataSource(GraphDataSource src) {
		lastMappedId_++;
		int id = lastMappedId_;
		dataMap_.put(id, src);
		
		Log.v(TAG, "Accepted data source: " + dataMap_.get(id).toString());
		
		return id;
	}
	
	/**
	 * Sets the scale variables. This should be called whenever the configuration or view dimensions 
	 * change.
	 */
	private void computeScales() {
		scaleX_ = (viewWidth_ - config_.offset.x - config_.border.x) / config_.area.width();
		scaleY_ = (viewHeight_ - config_.offset.y - config_.border.y) / config_.area.height();
	}
	
	/**
	 * The first drawing call that will be made on the canvas. Takes care of clearing the screen and
	 * any desired styling. Note that a lock on the configuration object will have already been 
	 * acquired before this call.
	 * 
	 * @param canvas
	 */
	protected void drawBackground(Canvas canvas) {
		canvas.drawColor(Color.BLACK);
	}
	
	/**
	 * Draws the axes and any desired background of the graph area. Called after drawBackground().
	 * Note that a lock on the configuration object will have already been acquired before this call
	 * 
	 * @param canvas
	 */
	protected void drawGraphArea(Canvas canvas) {
		int right = viewWidth_ - config_.border.x;
		int bottom = viewHeight_ - config_.border.y;
		
		paint_.setColor(Color.WHITE);
		paint_.setStyle(Style.STROKE);
		
		canvas.drawRect(config_.offset.x, config_.offset.y, right, bottom, paint_);
		
		int q = (int) (config_.area.min.x / config_.markX) + 1;
		for (float tickX = q * config_.markX; tickX <= config_.area.max.x; tickX += config_.markX) {
			float x = getScreenX(tickX);
			float y = viewHeight_ - config_.border.y;
			canvas.drawLine(x, y, x, y - 3, paint_);
			canvas.drawText(tickX + "", x - 7, y + 10, paint_);
		}
		
		q = (int) (config_.area.min.y / config_.markY) + 1;
		for (float tickY = q * config_.markY; tickY <= config_.area.max.y; tickY += config_.markY) {
			float y = getScreenY(tickY);
			float x = config_.offset.x;
			canvas.drawLine(x, y, x + 4, y, paint_);
			canvas.drawText(tickY + "", x - 20, y + 7, paint_);
		}
		
		if (SHOW_TOUCH_ZONES) {
			paint_.setColor(Color.YELLOW);
			canvas.drawRect(getAxisX(), paint_);
			canvas.drawRect(getAxisY(), paint_);
			canvas.drawRect(getArea(), paint_);
		}
	}
	
	/**
	 * Draws the data points registered with the GraphView. Note that a lock on the configuration 
	 * object will have already been acquired before this call.
	 * 
	 * @param canvas
	 */
	protected void drawPoints(Canvas canvas) {
		paint_.setColor(Color.WHITE);
		paint_.setStyle(Style.FILL_AND_STROKE);
		
		Set<Integer> idSet = dataMap_.keySet();

		for (Iterator<Integer> iter = idSet.iterator(); iter.hasNext();) {
			int id = iter.next();
			GraphDataSource data = dataMap_.get(id);
			
			for (int i = 0; i < data.size(); i++) {
				float x = data.get(i).x;
				float y = data.get(i).y;
				
				if (x > config_.area.max.x) {
					// If this point is too far right, then so will be the following points
					break;
				}
				if (isGraphXVisible(x) && isGraphYVisible(y))
					canvas.drawCircle(getScreenX(x), getScreenY(y), 2, paint_);
			}
			
		}
	}
	
	// TODO - These shouldn't be dynamically created. These should be data members that change when
	// config_ or dimensions change.
	
	/**
	 * Returns a graphics rectangle that contains the graph area.
	 * @return
	 */
	public Rect getArea() {
		int left = config_.offset.x + TOUCH_BORDER_AXES + TOUCH_BORDER_AREA;
		int top = config_.offset.y;
		int right = viewWidth_ - config_.border.x;
		int bottom = viewHeight_ - config_.border.y - TOUCH_BORDER_AXES - TOUCH_BORDER_AREA;
		return new Rect(left, top, right, bottom);
	}
	
	/**
	 * Returns a graphics rectangle that contains the x axis;
	 * @return
	 */
	public Rect getAxisX() {
		int left = config_.offset.x + TOUCH_BORDER_AXES;
		int top = viewHeight_ - config_.border.y - TOUCH_BORDER_AXES;
		int right = viewWidth_ - config_.border.x;		
		int bottom = viewHeight_ - config_.border.y + TOUCH_BORDER_AXES;
		
		return new Rect(left, top, right, bottom);
	}

	/**
	 * Returns a graphics rectangle that contains the y axis;
	 * @return
	 */
	public Rect getAxisY() {
		int left = config_.offset.x - TOUCH_BORDER_AXES;
		int top = config_.offset.y;
		int right = config_.offset.x + TOUCH_BORDER_AXES;
		int bottom = viewHeight_ - config_.border.y - TOUCH_BORDER_AXES;
		return new Rect(left, top, right, bottom);
	}
	
	/**
	 * Returns a copy of the current configuration object
	 * @return
	 */
	public GraphConfig getConfig() {
		return config_.copy();
	}
	
	/**
	 * Returns the x scale of the graph in screen pixels / graph unit
	 * @return
	 */
	public float getScaleX() {
		return scaleX_;
	}

	/**
	 * Returns the y scale of the graph in screen pixels / graph unit
	 * @return
	 */
	public float getScaleY() {
		return scaleY_;
	}

	/**
	 * Converts a Euclidean x coordinate to its corresponding screen x coordinate based on the 
	 * current configuration.
	 * 
	 * @param graphX
	 * @return
	 */
	protected float getScreenX(float graphX) {
		float graphOffset = graphX - config_.area.min.x;
		float screenX = graphOffset * scaleX_ + config_.offset.x;
		
		return screenX;
	}

	/**
	 * Converts a Euclidean y coordinate to its corresponding screen y coordinate based on the 
	 * current configuration.
	 * 
	 * @param graphX
	 * @return
	 */
	protected float getScreenY(float graphY) {
		float graphOffset = graphY - config_.area.min.y;
		float screenY = viewHeight_ - config_.border.y - (graphOffset * scaleY_);
		
		return screenY;
	}
	
	/**
	 * Get the scroll mode
	 * @return
	 */
	public ScrollMode getScrollMode() {
		return scrollMode_;
	}
	
	/**
	 * Returns true if the x graph coordinate is visible using current configuration.
	 * 
	 * @param graphX
	 * @return
	 */
	protected boolean isGraphXVisible(float graphX) {
		return config_.area.min.x <= graphX && graphX <= config_.area.max.x;
	}
	
	/**
	 * Returns true if the y graph coordinate is visible using current configuration.
	 * 
	 * @param graphX
	 * @return
	 */
	protected boolean isGraphYVisible(float graphY) {
		return config_.area.min.y <= graphY && graphY <= config_.area.max.y;
	}
	
	/**
	 * Forces the thread to stop executing after it finished its current loop.
	 */
	public void kill() {
		isAlive_ = false;
	}
	
	/**
	 * Pauses this thread. After this call. The thread sleeps for PAUSE_SLEEP and then rechecks its
	 * status.
	 */
	public void pause() {
		isPaused_ = true;
	}
	
	/**
	 * This method may not be overridden. This defines the behavior of the renderer.
	 */
	@Override
	public final void run() {
		while (isAlive_) {
			if (isPaused_) {
				try {
					Thread.sleep(PAUSE_SLEEP);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {	
				// We can't draw if we don't know our dimensions
				if (!(viewWidth_ == 0 && viewHeight_ == 0)) {
					// Lock the configuration object and start drawing
					synchronized (config_) {
						Canvas canvas = holder_.lockCanvas();
						if (canvas != null) {
							scroll();
							drawBackground(canvas);
							drawGraphArea(canvas);
							drawPoints(canvas);
							holder_.unlockCanvasAndPost(canvas);
						}
					}
				}
				
				
				try {
					Thread.sleep(DELAY);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		Log.v(TAG, TAG + " exiting");
	}
	
	/**
	 * Applies any necessary scrolling
	 */
	protected void scroll() {
		if (scrollMode_ == ScrollMode.SYNC) {
			GraphDataSource data = dataMap_.get(scrollSync_);
			float x = data.get(data.size() - 1).x;
			float dx = x - config_.area.max.x;
			config_.area.shift(dx, 0);
		}
	}
	
	/**
	 * Sets the configuration object used by this renderer. This call may block until the current
	 * frame is finished.
	 * 
	 * @param con
	 */
	public void setConfig(GraphConfig con) {
		// Lock config_ since we don't want to change mid-frame.
		synchronized (config_) {
			config_ = con;
			computeScales();
		}
	}
	
	/**
	 * Sets the dimensions of the surface available to GraphView. This call may block until the
	 * current frame is finished.
	 * 
	 * @param width
	 * @param height
	 */
	public void setDimension(int width, int height) {
		// Lock config_ since we don't want to change the sizes mid-frame.
		synchronized (config_) {
			viewWidth_ = width;
			viewHeight_ = height;
			computeScales();
			Log.v(TAG, "New dimensions set");
		}
	}
	
	/**
	 * Sets the scroll mode
	 * @param mode
	 */
	public void setScrollMode(ScrollMode mode) {
		scrollMode_ = mode;
	}
	
	/**
	 * Sets the id of data source to sync the scroll with
	 * @param syncId
	 */
	public void setScrollSync(int syncId) {
		if (scrollMode_ != ScrollMode.SYNC || !dataMap_.containsKey(syncId))
			throw new IllegalStateException("Non SYNC mode or data source not registered");
		scrollSync_ = syncId;
	}
	
	@Override
	public synchronized void start() {
		Log.v(TAG, TAG + " starting");
		super.start();
	}
	
	/**
	 * Resumes this thread.
	 */
	public void unpause() {
		isPaused_ = false;
	}
}
