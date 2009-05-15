// TODO - calibrate the projections based on real life data

package edu.vanderbilt.isis.vuphone;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import edu.vanderbilt.isis.R;

public class MyLocationOverlay extends Overlay{
	
	private OverlayPin pin_ = null;
	private Location lastLoc_ = null;
	private Activity context_ = null;
	private ZoneMapView map_ = null;
	
	public MyLocationOverlay(Context context){
		context_ = (Map) context;
		LocationManager man = (LocationManager) context_.getSystemService(Context.LOCATION_SERVICE);
		
		lastLoc_ = man.getLastKnownLocation(LocationManager.GPS_PROVIDER);	// This could be null.
		man.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
			new MyLocationListener());
		
		if (lastLoc_ != null){
    		int lon = (int) (lastLoc_.getLongitude() * 1E6);
    		int lat = (int) (lastLoc_.getLatitude() * 1E6);
    		GeoPoint point = new GeoPoint(lon, lat);
    		
			pin_ = new OverlayPin(point, "My Location");
		}
	}
	
	public void draw(Canvas canvas, MapView mapView, boolean shadow){
		if (map_ == null)
			map_ = (ZoneMapView) mapView;
		
		if (lastLoc_ != null){
			pin_.draw(canvas, mapView);
			mapView.postInvalidate();
		}
	}
	
	public void update(){
		((ZoneMapView) context_.findViewById(R.id.mapview)).postInvalidate();
	}
	
	
	private class MyLocationListener implements LocationListener{

	    public void onLocationChanged(Location loc) {
	    	if (loc != null) {
	    		lastLoc_ = loc;

	    		int lon = (int) (lastLoc_.getLongitude() * 1E6);
	    		int lat = (int) (lastLoc_.getLatitude() * 1E6);
	    		
	    		GeoPoint point = new GeoPoint(lon, lat);
	    		
	    		MyLocationOverlay.this.pin_.setPoint(point);
	    		
	    		// Redraw
	    		MyLocationOverlay.this.update();
/*
	    		Zone colZone = map_.checkCollision(point);
	    		if (colZone != null){
	    			Toast.makeText(context_, "Collision with " + colZone.getName(), Toast.LENGTH_SHORT).show();
	    		}
*/    		
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