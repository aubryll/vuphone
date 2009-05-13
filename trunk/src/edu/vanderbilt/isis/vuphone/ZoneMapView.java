package edu.vanderbilt.isis.vuphone;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

class ZoneOverlay extends Overlay{
	Zone zone_;
	
	ZoneOverlay(GeoZone geoZone){
		zone_ = new Zone(geoZone.getPoints(), "Default", false);
	}
	
	public void draw(Canvas canvas, MapView mapView, boolean shadow){
		zone_.setProjection(mapView.getProjection());
		Path path = zone_.getPath();
		Paint paint = new Paint();
		canvas.drawPath(path, paint);
	}
	
	public void setZone(GeoZone geoZone){
		zone_ = new Zone(geoZone.getPoints(), "Default", false);
	}
}

class PinOverlay extends Overlay{
	private GeoPoint point_;
	
	public PinOverlay(GeoPoint pt){
		point_ = pt;
	}
	
	public void draw(Canvas canvas, MapView mapView, boolean shadow){
		Point scrPt = mapView.getProjection().toPixels(point_, null);
		float x = scrPt.x; 
		float y = scrPt.y;
		float r = 5;
		
		canvas.drawCircle(x, y, r, new Paint());
	}
}


// This class is inefficient!!!!!!!!!!!!!!!!!!!!!!!!!
public class ZoneMapView extends MapView {

	private ZoneOverlay zoneOverlay_;
	private GeoZone geoZone_;
	
	public ZoneMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		geoZone_ = new GeoZone();
        setBuiltInZoomControls(true);
        setClickable(true);
        
        zoneOverlay_ = null;
	}
	
	public void addPinEvent(MotionEvent event){
    	float x = event.getX();
    	float y = event.getY();
    	
    	int offset[] = new int[2];
    	getLocationOnScreen(offset);
    	x -= (float) offset[0];
    	y -= (float) offset[1];
    	    	
    	Projection proj = getProjection();
    	GeoPoint pt = proj.fromPixels((int) x, (int) y);
    	
    	addPin(pt);
	}
	
	private void addPin(GeoPoint pt){
		// Index 0 should always be the zone overlay
		geoZone_.addPoint(pt);
		zoneOverlay_ = new ZoneOverlay(geoZone_);
		
		if (getOverlays().size() == 0)
			getOverlays().add(zoneOverlay_);
		else
			getOverlays().set(0, zoneOverlay_);
		
		PinOverlay pin = new PinOverlay(pt);
		getOverlays().add(pin);
		postInvalidate();
	}
}
