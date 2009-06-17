package org.vuphone.wwatch.android.mapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.vuphone.wwatch.android.VUphone;
import org.vuphone.wwatch.android.Waypoint;

import android.content.Context;
import android.content.Intent;
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
	
	private long[] wreckTimes_ = null;
	
	private GeoPoint[] wrecks_ = null;
	
	private int numWrecks_ = 0;
	
	private static final String LOG_PREFIX = "PinGroup: ";
	
	private Context context_ = null;

	private Bitmap pinIcon_;
	/**
	 * Default constructor.
	 */
	public PinGroup(Bitmap icon){
		points_ = Collections.synchronizedList(new ArrayList<GeoPoint>());
		pinIcon_ = icon;
	}
	
	public PinGroup(Bitmap icon, Context context) {
		points_ = Collections.synchronizedList(new ArrayList<GeoPoint>());
		pinIcon_ = icon;
		context_ = context;
	}

	/**
	 * Adds the GeoPoint to a list of pins to be drawn.
	 * @param point
	 * @param name
	 */
	public void addPin(Waypoint point, int id){
		if (!points_.contains(point)){
			int lat = (int)(point.getLatitude() * 1E6);
			int lon = (int) (point.getLongitude() * 1E6);
			GeoPoint p = new GeoPoint(lat, lon);
			points_.add(p);
			if (id >= numWrecks_) {
				GeoPoint[] temp = new GeoPoint[id+1];
				long[] temp2 = new long[id+1];
				for(int i = 0; i < numWrecks_; i++) {
					temp[i] = wrecks_[i];
					temp2[i] = wreckTimes_[i];
				}
				numWrecks_ = id+1;
				wrecks_ = temp;
				wreckTimes_ = temp2;
			}
			if (point.getTime() > wreckTimes_[id]) {
				wreckTimes_[id] = point.getTime();
				wrecks_[id] = p;
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
				
				boolean found = false;
				for (GeoPoint wreck : wrecks_) {
					if (point.equals(wreck)) {
						found = true;
						canvas.drawCircle(x, y, radius, paint);
						break;
					}
				}
				if (!found) {
					canvas.drawCircle(x, y, radius, new Paint());
				}
			}
		}
	}

	public boolean onTouchEvent(MotionEvent event, MapView view){

		if (event.getAction() == MotionEvent.ACTION_DOWN && numWrecks_ > 0) {
			Log.d(VUphone.tag, LOG_PREFIX + "Touch detected at ("+event.getX()+", "+event.getY()+").");
			
			// Figure out which point was touched.
			Projection projection = view.getProjection();
			for (GeoPoint wreck : wrecks_) {
				Point scrPt = projection.toPixels(wreck, null);
				float x = scrPt.x; 
				float y = scrPt.y;
				int radius = 20;
				if (event.getX() > x-radius && event.getX() < x+radius &&
						event.getY() > y-radius && event.getY() < y+radius) {
					Log.d(VUphone.tag, LOG_PREFIX + "Found the point "+wreck.toString());

					// When the pin is clicked on, we will display the GalleryActivity
					Intent i = new Intent(context_, GalleryActivity.class);
					i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					i.putExtra("org.vuphone.wwatch.android.mapping.GalleryActivity.point", 
							"latitude="+wreck.getLatitudeE6()+
							"&longitude="+wreck.getLongitudeE6());
					context_.startActivity(i);
					//view.getOverlays().add(new GalleryOverlay(context_));
					//view.postInvalidate();
				
					return true;
				}
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
