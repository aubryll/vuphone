/**
 * 
 */
package org.vuphone.vandyupon.android.submitevent;

import android.graphics.Paint;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

/**
 * The Activity that holds the MapView we are going to be using to choose a
 * location. Handles keeping track of the last touch, and drawing the pin on the map. 
 * 
 * @author Hamilton Turner
 * 
 */
public class LocationChooserOverlay extends Overlay {
	GeoPoint currentLocation_;

	Point point = new Point(0,0);
	static Paint p = new Paint();
	static {
		p.setStrokeWidth(2);
		p.setAntiAlias(true);
	}
	
	@Override
	public boolean onTap(GeoPoint p, MapView mapView) {
		currentLocation_ = p;
		return true;
	}

	@Override
	public void draw(android.graphics.Canvas canvas, MapView mapView,
			boolean shadow) {
		if (currentLocation_ == null)
			return;
		
		mapView.getProjection().toPixels(currentLocation_, point);
		canvas.drawCircle(point.x, point.y - 25, 8, p);
		canvas.drawLine(point.x, point.y - 25, point.x, point.y, p);
	}
	
	protected GeoPoint getCurrentLocation() {
		return currentLocation_;
	}

}
