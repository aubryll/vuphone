package edu.vanderbilt.isis.vuphone;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import edu.vanderbilt.isis.R;

public class MyLocationOverlay extends Overlay{
	
	
	private GeoPoint point_ = null;
	private Location lastLoc_ = null;
	private Activity context_ = null;
	private ZoneMapView map_ = null;
	
	private final float RADIUS = 10;
	
	public MyLocationOverlay(Context context){
		context_ = (Map) context;
		LocationManager man = (LocationManager) context_.getSystemService(Context.LOCATION_SERVICE);
		
		lastLoc_ = man.getLastKnownLocation(LocationManager.GPS_PROVIDER);	// This could be null.
		man.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
			new MyLocationListener());
		
		if (lastLoc_ != null){
    		int lon = (int) (lastLoc_.getLongitude() * 1E6);
    		int lat = (int) (lastLoc_.getLatitude() * 1E6);
    		point_ = new GeoPoint(lon, lat);
		}
	}
	
	public void draw(Canvas canvas, MapView mapView, boolean shadow){
		if (map_ == null)
			map_ = (ZoneMapView) mapView;
		
		if (lastLoc_ != null){
			if (point_ == null)
				return;
			Point scrPt = mapView.getProjection().toPixels(point_, null);
			float x = scrPt.x; 
			float y = scrPt.y;
			
			Paint paint = new Paint();
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth(2);
			
			canvas.drawCircle(x, y, RADIUS, paint);
			canvas.drawLine(x - RADIUS, y, x + RADIUS, y, paint);
			canvas.drawLine(x, y - RADIUS, x, y + RADIUS, paint);
			mapView.postInvalidate();
		}
	}
	
	public void update(){
		((ZoneMapView) context_.findViewById(R.id.mapview)).postInvalidate();
	}
	
	public boolean onTouchEvent(MotionEvent ev, MapView view){
		// Don't do anything unless we release the touch
		if (ev.getAction() == MotionEvent.ACTION_UP){
			// If we click here, zoom in and center.
			if (point_ == null)
				return false;
			
			Point scrPt = view.getProjection().toPixels(point_, null);
			float px = scrPt.x; 
			float py = scrPt.y;
			
			float ex = ev.getX();
			float ey = ev.getY();
			
			float disSq = (ex - px) * (ex - px) + (ey - py) * (ey - py);
			if (disSq < RADIUS * RADIUS){
				MapController control = view.getController();
				control.animateTo(point_);
				control.zoomIn();
				return true;
			}
		}
		
		return false;
	}
	
	
	private class MyLocationListener implements LocationListener{

	    public void onLocationChanged(Location loc) {
	    	if (loc != null) {
	    		lastLoc_ = loc;

	    		int lon = (int) (lastLoc_.getLongitude() * 1E6);
	    		int lat = (int) (lastLoc_.getLatitude() * 1E6);
	    		
	    		GeoPoint point = new GeoPoint(lon, lat);
	    		
	    		MyLocationOverlay.this.point_ = point;
	    		
	    		// Redraw
	    		MyLocationOverlay.this.update();

	    		Zone colZone = map_.checkCollision(point);
	    		if (colZone != null){
	    			Toast.makeText(context_, "Collision with " + colZone.getName(), Toast.LENGTH_SHORT).show();
	    		}
    		
	    		Log.v("VUPHONE", "Location updated with non-null value");
	       }else{
	    	   Log.v("VUPHONE", "Location updated with a null value");
	       }
	    }

	    public void onProviderDisabled(String provider){}
	    public void onProviderEnabled(String provider){}
	    public void onStatusChanged(String provider, int status, Bundle extras){}
	}
}