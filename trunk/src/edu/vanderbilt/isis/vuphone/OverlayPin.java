package edu.vanderbilt.isis.vuphone;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class OverlayPin extends Overlay{
	private GeoPoint point_;
	private String name_;
	
	public OverlayPin(GeoPoint pt, String nm){
		point_ = pt;
		name_ = nm;
	}
	
	public OverlayPin(GeoPoint pt){
		this(pt, "Point");
	}
	
	public void draw(Canvas canvas, MapView mapView, boolean shadow){
		Point scrPt = mapView.getProjection().toPixels(point_, null);
		float x = scrPt.x; 
		float y = scrPt.y;
		float r = 5;
		
		Paint paint = new Paint();
		
		canvas.drawCircle(x, y, r, paint);
		canvas.drawText(name_, x, y - r, paint);
	}
}
