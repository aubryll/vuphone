package edu.vanderbilt.isis.vuphone;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.view.MotionEvent;

import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;


public class OverlayZone extends Overlay{

	private Zone zone_;	// Reference to the zone handled by ZoneMapView
	
	OverlayZone(Zone zone){
		zone_ = zone;
	}
	
	public void draw(Canvas canvas, MapView mapView, boolean shadow){
		Path path = zone_.getPath();
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawPath(path, paint);
		
		Point center = zone_.getCenter();
		float x = (float) center.x;
		float y = (float) center.y;
		
		canvas.drawCircle(x, y, 10, new Paint());
		
		//canvas.drawText(zone_.getName(), x, y, paint);
	}

	public boolean onTouchEvent(MotionEvent event, MapView mapView){
		float x = event.getX();
		float y = event.getY();

		if (zone_.contains(new Point((int)x, (int)y))){
			((Map) mapView.getContext()).debug("ZoneOverlay received a touch event");
			
			return true;
		}
		
		
		return false;
	}
    
}