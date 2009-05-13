package edu.vanderbilt.isis.vuphone;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.MotionEvent;

import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;


public class ZoneOverlay extends Overlay{

	private Zone zone_;	// Reference to the zone handled by ZoneMapView
	
	ZoneOverlay(Zone zone){
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