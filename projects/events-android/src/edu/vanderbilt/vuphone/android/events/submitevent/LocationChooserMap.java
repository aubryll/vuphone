/**
 * 
 */
package edu.vanderbilt.vuphone.android.events.submitevent;


import android.content.Context;
import android.util.AttributeSet;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

import edu.vanderbilt.vuphone.android.events.LocationManager;

/**
 * @author Hamilton Turner
 * 
 */
public class LocationChooserMap extends MapView {

	/** Used to draw the current location, accuracy, and compass */
	private MyLocationOverlay currentLocation_;
	
	private LocationChooserOverlay chosenLocation_;

	/**
	 * @param context
	 * @param attrs
	 */
	public LocationChooserMap(Context context, AttributeSet attrs) {
		super(context, attrs);
		setBuiltInZoomControls(true);

		getController().setCenter(LocationManager.vandyCenter_);
		getController().setZoom(15);
		
		currentLocation_ = new MyLocationOverlay(context, this);
		chosenLocation_ = new LocationChooserOverlay();
		getOverlays().add(chosenLocation_);
	}

	/** Used to turn off the various sensors */
	protected void disableMyLocation() {
		currentLocation_.disableCompass();
		currentLocation_.disableMyLocation();
	}

	/** Used to turn on the various sensors */
	protected void enableMyLocation() {
		currentLocation_.enableMyLocation();
		currentLocation_.enableCompass();
	}
	
	protected GeoPoint getCurrentLocation() {
		return chosenLocation_.getCurrentLocation();
	}

}
