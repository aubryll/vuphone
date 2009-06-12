package org.vuphone.wwatch.android.mapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.vuphone.wwatch.android.VUphone;
import org.vuphone.wwatch.android.Waypoint;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.util.Log;
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


	private List<GeoPoint> points_ = null;
	
	private long lastTime_ = 0;
	
	private GeoPoint lastPoint_ = null;
	
	private static final String LOG_PREFIX = "PinGroup: ";

	private Bitmap pinIcon_;
	/**
	 * Default constructor.
	 */
	public PinGroup(Bitmap icon){
		points_ = Collections.synchronizedList(new ArrayList<GeoPoint>());
		pinIcon_ = icon;
	}

	/**
	 * Adds the GeoPoint to a list of pins to be drawn.
	 * @param point
	 * @param name
	 */
	public void addPin(Waypoint point){
		if (!points_.contains(point)){
			int lat = (int)(point.getLatitude() * 1E6);
			int lon = (int) (point.getLongitude() * 1E6);
			GeoPoint p = new GeoPoint(lat, lon);
			points_.add(p);
			if (point.getTime() > lastTime_) {
				lastTime_ = point.getTime();
				lastPoint_ = p;
			}
		}
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

		
		synchronized(points_){
			for (GeoPoint point : points_){
				Point scrPt = projection.toPixels(point, null);
				float x = scrPt.x; 
				float y = scrPt.y;
				int radius = 5;
				
				
				if (point.equals(lastPoint_)) {
					canvas.drawCircle(x, y, radius, paint);
				}
				else {
					canvas.drawCircle(x, y, radius, new Paint());
				}
			}
		}
	}

	public boolean onTouchEvent(MotionEvent event, MapView view){

		if (event.getAction() == MotionEvent.ACTION_DOWN && lastPoint_ != null) {
			Log.d(VUphone.tag, LOG_PREFIX + "Touch detected at ("+event.getX()+", "+event.getY()+").");
			Projection projection = view.getProjection();
			Point scrPt = projection.toPixels(lastPoint_, null);
			float x = scrPt.x; 
			float y = scrPt.y;
			int radius = 20;
			if (event.getX() > x-radius && event.getX() < x+radius &&
					event.getY() > y-radius && event.getY() < y+radius) {
				Log.d(VUphone.tag, LOG_PREFIX + "Found the point "+lastPoint_.toString());

				// Add code here to call up whatever we want to display when
				// the pin for the wreck is clicked on.
				
				return true;
			}
		}
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
