package edu.vanderbilt.isis.vuphone;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

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
		canvas.drawPath(path, paint);
	}
	/*
	public boolean onTouchEvent(MotionEvent e, MapView mapView){
		Log.v("VUPHONE", "ZoneOverlay received a touch event");
		
		return false;
	}*/
    
}