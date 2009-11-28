package com.zienkikk.android.grapher;

import com.zienkikk.android.grapher.GraphView.ScrollMode;

import android.view.MotionEvent;


/**
 * A class responsible for keeping track of the touch events and applying the correct 
 * transformations to the GraphView. 
 * 
 * @author Krzysztof Zienkiewicz
 *
 */
public class TouchScroller {

	/**
	 * We'll be transforming the viewport via renderer's configuration object
	 */
	private GraphRenderer renderer_;
	
	/**
	 * -1 	= Uninitialized
	 * 0	= x axis
	 * 1	= y axis
	 * 2	= graph area 
	 */
	private int target_;
	
	/**
	 * Event coordinates
	 */
	private float x_, y_;
	
	public TouchScroller(GraphRenderer r) {
		renderer_ = r;
		target_ = -1;
		x_ = y_ = 0;
	}
	
	private int getTarget(float x, float y) {
		int a = (int) x;
		int b = (int) y;
		
		if (renderer_.getAxisX().contains(a, b))
			return 0;
		else if (renderer_.getAxisY().contains(a, b))
			return 1;
		else if (renderer_.getArea().contains(a, b))
			return 2;
		
		return -1;
	}
	
	/**
	 * This should be called in GraphView's onTouchEvent(). This method will perform the necessary
	 * transformations to the view.
	 * @param event
	 */
	public void onTouchEvent(MotionEvent event) {
		if (renderer_.getScrollMode() != ScrollMode.MANUAL)
			return;
		
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			set(event);
			target_ = getTarget(x_, y_);
		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			
			float dx = (event.getX() - x_) / renderer_.getScaleX();
			float dy = -(event.getY() - y_) / renderer_.getScaleY();
			
			transform(target_, dx, dy);
			
			set(event);
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			target_ = -1;
		}
	}
	
	/**
	 * Store the coordinates
	 * @param event
	 */
	private void set(MotionEvent event) {
		x_ = event.getX();
		y_ = event.getY();
	}
	
	
	private void transform(int target, float dx, float dy) {
		GraphConfig config = renderer_.getConfig();
		
		if (target == 0) {
			config.area.max.x -= dx;
		} else if (target == 1) {
			config.area.max.y += dy;
		} else if (target == 2) {
			config.area.shift(-dx, -dy);
		}
		
		renderer_.setConfig(config);
	}
}
