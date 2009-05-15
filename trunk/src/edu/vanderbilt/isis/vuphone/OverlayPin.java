package edu.vanderbilt.isis.vuphone;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

//TODO - Should probably trash this class. Every overlay you add to the map must be
//       blended with every other overlay. This will introduce a large lag if there 
//       are a lot of pins, with one overlay per pin. It would be much better to 
//       have a single overlay, that internally keeps a list of pins, and it's draw 
//       method iterates through those pins and draws them all onto one overlay
public class OverlayPin {
	private GeoPoint point_;
	private String name_;

	public OverlayPin(GeoPoint pt, String nm) {
		point_ = pt;
		name_ = nm;
	}

	public OverlayPin(GeoPoint pt) {
		this(pt, "Point");
	}

	// Called from PinGroup.draw()

	public void draw(Canvas canvas, MapView mapView) {
		Point scrPt = mapView.getProjection().toPixels(point_, null);
		float x = scrPt.x;
		float y = scrPt.y;
		float r = 5;

		Paint paint = new Paint();

		canvas.drawCircle(x, y, r, paint);
		canvas.drawText(name_, x, y - r, paint);
	}

	public void setPoint(GeoPoint pt) {
		Log.v("VUPHONE", "OverlayPin.setpoint");
		point_ = pt;
	}

	public String toString() {
		return "OverlayPin: " + name_;
	}
}
