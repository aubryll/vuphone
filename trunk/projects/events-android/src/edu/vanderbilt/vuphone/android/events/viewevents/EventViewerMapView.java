/**
 * 
 */
package edu.vanderbilt.vuphone.android.events.viewevents;


import android.content.Context;
import android.util.AttributeSet;

import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

import edu.vanderbilt.vuphone.android.events.LocationManager;
import edu.vanderbilt.vuphone.android.events.filters.PositionFilter;
import edu.vanderbilt.vuphone.android.events.filters.TagsFilter;
import edu.vanderbilt.vuphone.android.events.filters.TimeFilter;

/**
 * Contains the View that draws the map, and contains any overlays added to the
 * map
 * 
 * @author Hamilton Turner
 * 
 */
public class EventViewerMapView extends MapView {

	/** Used to draw the current location, accuracy, and compass */
	private MyLocationOverlay currentLocation_;

	/** Draws visuals about the position filter on the map */
	private PositionOverlay positionOverlay_;

	/** Draws the events to the map */
	private EventOverlay eventOverlay_;

	/** Handles to the current filters */
	private PositionFilter positionFilter_;
	private TimeFilter timeFilter_;
	private TagsFilter tagsFilter_;

	/**
	 * @param context
	 * @param attrs
	 */
	public EventViewerMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		setBuiltInZoomControls(true);
		setClickable(true);
		
		getController().setCenter(LocationManager.vandyCenter_);
		getController().setZoom(15);
		currentLocation_ = new MyGPSLocationOverlay(getContext(), this);
		getOverlays().add(currentLocation_);

		eventOverlay_ = new EventOverlay(null, null,
				null, this);
		getOverlays().add(eventOverlay_);
		

		positionOverlay_ = new PositionOverlay();
		getOverlays().add(positionOverlay_);
	}

	/** Used to turn off the various sensors */
	protected void disableMyLocation() {
		currentLocation_.disableMyLocation();
		
		// Are we in emulator or device? 
		if (false == "1".equals(System.getProperty("ro.kernel.qemu"))) { 
			currentLocation_.disableCompass();
		}
	}

	/** Used to turn on the various sensors */
	protected void enableMyLocation() {
		currentLocation_.enableMyLocation();
		
		// Are we in emulator or device? 
		if (false == "1".equals(System.getProperty("ro.kernel.qemu"))) {
			currentLocation_.enableCompass();
		}
	}
	
	/** Used to get a handle to the EventOverlay */
	protected EventOverlay getEventOverlay() {
		return eventOverlay_;
	}
	
	/** Request that all Overlays refresh */
	protected void refreshOverlays() {
		eventOverlay_.receiveNewFilters(positionFilter_, timeFilter_, tagsFilter_);
		positionOverlay_.setPositionFilter(positionFilter_);
	}

	protected void updatePositionFilter(PositionFilter filter) {
		// Turn off any current location threads
		if (positionFilter_ != null)
			positionFilter_.stop();
		positionFilter_ = filter;
		positionOverlay_.setPositionFilter(positionFilter_);
		eventOverlay_.receiveNewFilters(positionFilter_, null, null);
	}
}
