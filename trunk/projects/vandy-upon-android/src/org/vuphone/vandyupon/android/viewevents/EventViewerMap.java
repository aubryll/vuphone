/**
 * 
 */
package org.vuphone.vandyupon.android.viewevents;

import org.vuphone.vandyupon.android.LocationManager;

import android.content.Context;
import android.util.AttributeSet;

import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

/**
 * Contains the View that draws the map, and contains any overlays added to the
 * map
 * 
 * @author Hamilton Turner
 * 
 */
public class EventViewerMap extends MapView {

	/** Used to draw the current location, accuracy, and compass */
	private MyLocationOverlay currentLocation_;

	/**
	 * @param context
	 * @param attrs
	 */
	public EventViewerMap(Context context, AttributeSet attrs) {
		super(context, attrs);
		setBuiltInZoomControls(true);

		getController().setCenter(LocationManager.vandyCenter_);
		getController().setZoom(15);
		currentLocation_ = new MyLocationOverlay(getContext(), this);
		getOverlays().add(currentLocation_);
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

}
