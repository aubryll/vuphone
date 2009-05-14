package edu.vanderbilt.isis.vuphone;

import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
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
		
		List<Overlay> list = mapView.getOverlays();
		if (list.indexOf(this) == 1 || list.indexOf(this) == list.size() - 1){
			paint.setColor(Color.RED);
		}
		
		canvas.drawCircle(x, y, r, paint);
		canvas.drawText(name_, x, y - r, paint);
	}
}
