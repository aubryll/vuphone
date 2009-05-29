package org.vuphone.wwatch.android.mapping;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.view.MotionEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

/**
 * A wrapper class responsible for holding all the GeoPoint objects and displaying them.
 * @author Krzysztof Zienkiewicz
 *
 */
public class PinGroup extends Overlay{
	
	// TODO - Think about implementing this as a list of pairs...
	private ArrayList<GeoPoint> points_ = null;
	
	/**
	 * Default constructor.
	 */
	public PinGroup(){
		points_ = new ArrayList<GeoPoint>();
	}
	
	/**
	 * Adds the GeoPoint to a list of pins to be drawn.
	 * @param point
	 * @param name
	 */
	public void addPin(GeoPoint point){
		points_.add(point);
	}
	
    /**
     * Draws all the pins
     *  
     * @param	canvas	The Canvas on which to draw
     * @param	mapView	The MapView that requested the draw
     * @param	shadow	Ignored in this implementation 
     */
	public void draw(Canvas canvas, MapView mapView, boolean shadow){
		Projection projection = mapView.getProjection();
		Paint paint = new Paint();
		paint.setColor(Color.RED);
		paint.setTextSize(15);
		paint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
		
		int index = 0;
		
		for (GeoPoint point : points_){
			Point scrPt = projection.toPixels(point, null);
			float x = scrPt.x; 
			float y = scrPt.y;
			float r = 5;
			
			canvas.drawCircle(x, y, r, new Paint());
			
			++index;
		}
	}
	
	public boolean onTouchEvent(MotionEvent event, MapView view){
		// For now don't do anything but propagate.
		return false;
	}
	
	/**
	 * Removes the last added point.
	 */
	public void removeLastPoint(){
		int size = points_.size();
		if (size > 0){
			points_.remove(size - 1);
		}
	}
	
	/**
	 * Get the number of OverlPin objects in this group.
	 * @return	size of this group.
	 */
	public int size(){
		return points_.size();
	}
	
	/**
	 * Return a human readable representation of this object
	 * 
	 * @return
	 */
	public String toString(){
		int index = 0;
		String str = "PinGroup: ";
		for (GeoPoint point : points_){
			str += "[" + point.toString() + "] "; 
			++index;
		}
		return str;
	}
}
